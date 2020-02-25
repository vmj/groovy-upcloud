/*
 * Groovy UpCloud library - Script Module
 * Copyright (C) 2018  Mikko Värri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fi.linuxbox.upcloud.script

import fi.linuxbox.upcloud.http.spi.HTTP
import fi.linuxbox.upcloud.json.spi.JSON
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.*

@CompileStatic
abstract class UpCloudScript extends Script {
    // Unable to use @Slf4j since this cannot be private for it to be available for scripts
    protected static final Logger log = LoggerFactory.getLogger(UpCloudScript)
    private final ExecutorService executorService
    private final HTTP http
    private final JSON json
    private boolean closing = false

    UpCloudScript() {
        this(new Binding())
    }

    UpCloudScript(final Binding binding) {
        super(binding)
        executorService = Executors.newSingleThreadExecutor(new ScriptThreadFactory())
        this.http = new HTTPDecorator(HTTPFactory.create(), executorService)
        this.json = JSONFactory.create()
    }

    CompletableFutureSession newSession(final String username, final String password) {
        final s = new CompletableFutureSession(http, json, username, password)
        s.whenFinished {
            executorService.submit {
                log.trace "Auto-closing"
                close()
            }
        }
        s
    }

    CompletableFutureSession newSession(final Credentials credentials) {
        newSession(credentials.username, credentials.password)
    }

    @Override
    Object run() {
        try {
            executorService.submit { runScript() }
        } catch (final RejectedExecutionException e) {
            // Somehow the executor service might be already shut down
            log.error("Unable to start the script", e)
        }

        log.trace("Initialization complete")
        boolean ok = false
        try {
            int timeout = 20
            log.trace("Waiting for termination ($timeout days)")
            ok = executorService.awaitTermination(timeout, TimeUnit.DAYS)
        } catch (final InterruptedException ie) {
            log.warn("Executor service interrupted", ie)
        }
        if (ok) {
            log.trace("Terminated normally")
        } else {
            log.debug("Script timed out")
            throw new RuntimeException(new TimeoutException("Script timed out"))
        }
        return null
    }

    void runScript() {
        try {
            log.trace("Executing top-level code of the script")
            runUpCloudScript()
            log.trace("Top-level code finished")
        } catch (final InterruptedException e) {
            // Script called close() from the top level code,
            // or the interrupt signal was received.
            if (!closing)
                log.trace("Interrupted")
            Thread.currentThread().interrupt()
        } catch (final Throwable e) {
            // Script threw an exception from the top level code
            log.error("Shutting down due to unhandled exception", e)
            close()
        }
    }

    abstract Object runUpCloudScript() throws InterruptedException

    void close() throws InterruptedException {
        // This is executed in the script thread
        closing = true
        shutdownExecutorService()
        closeHttp()
        log.trace("Shutting down")
        throw new InterruptedException("shutting down")
    }

    private void closeHttp() {
        try {
            log.trace("Closing HTTP implementation")
            http.close()
        } catch (final IOException e) {
            log.warn("Unable to close HTTP implementation", e)
        }
    }

    private void shutdownExecutorService() {
        log.trace("Disabling new tasks from being submitted")
        executorService.shutdown()
        log.trace("Cancelling currently executing task")
        executorService.shutdownNow()
    }

    /**
     * Otherwise a functional equivalent of DefaultThreadFactory, but uses simpler name.
     */
    private static class ScriptThreadFactory implements ThreadFactory {
        Thread newThread(Runnable r) {
            final SecurityManager s = System.securityManager
            final ThreadGroup group = (s != null) ? s.threadGroup : Thread.currentThread().threadGroup
            final Thread t = new Thread(group, r, "upcloud-script",0)
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t
        }
    }
}

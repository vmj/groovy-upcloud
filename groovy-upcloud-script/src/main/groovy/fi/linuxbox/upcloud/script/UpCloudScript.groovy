/*
 * Groovy UpCloud library - Script Module
 * Copyright (C) 2018  <mikko@varri.fi>
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

    UpCloudScript() {
        this(new Binding())
    }

    UpCloudScript(final Binding binding) {
        super(binding)
        executorService = Executors.newSingleThreadExecutor()
        this.http = new HTTPDecorator(HTTPFactory.create(), executorService)
        this.json = JSONFactory.create()
    }

    CompletableFutureSession newSession(final String username, final String password) {
        new CompletableFutureSession(http, json, username, password)
    }

    @Override
    Object run() {
        log.debug("Initializing")
        try {
            executorService.submit { runScript() }
        } catch (final RejectedExecutionException e) {
            // Somehow the executor service might be already shut down
            log.error("Unable to start the script", e)
        }

        log.debug("Initialization complete")
        boolean ok = false
        try {
            // TODO: make this maximum runtime configurable
            int timeout = 20
            log.debug("Waiting for termination ($timeout seconds)")
            ok = executorService.awaitTermination(timeout, TimeUnit.SECONDS)
        } catch (final InterruptedException ie) {
            log.warn("Executor service interrupted", ie)
        }
        if (ok) {
            log.debug("Terminated normally")
        } else {
            log.debug("Script timed out")
            throw new RuntimeException(new TimeoutException("Script timed out"))
        }
        return null
    }

    void runScript() {
        try {
            log.debug("Script execution beginning")
            runUpCloudScript()
            log.debug("Script top-level code finished")
        } catch (final InterruptedException e) {
            // Script called close() from the top level code
            log.debug("Shutting down")
            Thread.currentThread().interrupt()
        } catch (final Exception e) {
            // Script threw an exception from the top level code
            log.error("Closing due to unhandled exception", e)
            close()
        }
    }

    abstract Object runUpCloudScript() throws InterruptedException

    void close() throws InterruptedException {
        // This is executed in the script thread
        shutdownExecutorService()
        closeHttp()
        log.debug("Shutting down")
        throw new InterruptedException("shutting down")
    }

    private void closeHttp() {
        try {
            log.debug("Closing HTTP implementation")
            http.close()
        } catch (final IOException e) {
            log.warn("Unable to close HTTP", e)
        }
    }

    private void shutdownExecutorService() {
        log.debug("Disabling new tasks from being submitted")
        executorService.shutdown()
        log.debug("Cancelling currently executing task")
        executorService.shutdownNow()
    }
}

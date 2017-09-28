/*
 * Groovy UpCloud library - Script Module
 * Copyright (C) 2017  <mikko@varri.fi>
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
package fi.linuxbox.upcloud.script;

import fi.linuxbox.upcloud.core.Session;
import fi.linuxbox.upcloud.http.spi.HTTP;
import fi.linuxbox.upcloud.json.spi.JSON;
import groovy.lang.Binding;
import groovy.lang.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.*;

public abstract class UpCloudScript extends Script {
    protected static final Logger log = LoggerFactory.getLogger(UpCloudScript.class);
    private final ExecutorService executorService;
    private final HTTP http;
    private final JSON json;

    public UpCloudScript() {
        this(new Binding());
    }

    public UpCloudScript(final Binding binding) {
        super(binding);
        executorService = Executors.newSingleThreadExecutor();
        this.http = new HTTPDecorator(HTTPFactory.create(), executorService);
        this.json = JSONFactory.create();
    }

    public Session newSession(final String username, final String password) {
        return new Session(http, json, username, password);
    }

    @Override
    public Object run() {
        try {
            executorService.submit(() -> runScript());
        } catch (final RejectedExecutionException e) {
            // Somehow the executor service might be already shut down
            log.error("Unable to start the script", e);
        }

        log.info("Initialization complete.");
        boolean ok = false;
        try {
            // TODO: make this maximum runtime configurable
            ok = executorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            log.warn("Executor service interrupted", e);
        }
        if (ok)
            log.info("Shutting down.");
        else
            throw new RuntimeException(new TimeoutException("Script timed out"));
        return null;
    }

    public void runScript() {
        try {
            log.debug("Script execution beginning.");
            runUpCloudScript();
            log.debug("Script top-level code finished.");
        } catch (final InterruptedException e) {
            // Script called close() from the top level code
            log.info("runUpCloudScript interrupted; exiting");
            Thread.currentThread().interrupt();
        } catch (final Exception e) {
            log.error("Unhandled exception", e);
        }
    }

    public abstract Object runUpCloudScript() throws InterruptedException;

    public void close() throws InterruptedException {
        // This is executed in the script thread
        shutdownExecutorService();
        closeHttp();
        throw new InterruptedException("shutting down");
    }

    private void closeHttp() {
        try {
            http.close();
        } catch (final IOException e) {
            log.warn("Unable to close HTTP", e);
        }
    }

    private void shutdownExecutorService() {
        executorService.shutdown(); // Disable new tasks from being submitted
        executorService.shutdownNow(); // Cancel currently executing task (which is this thread!)
    }
}

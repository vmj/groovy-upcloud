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
package fi.linuxbox.upcloud.script;

import fi.linuxbox.upcloud.http.spi.CompletionCallback;
import fi.linuxbox.upcloud.http.spi.HTTP;
import fi.linuxbox.upcloud.http.spi.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class HTTPDecorator implements HTTP {
    private final Logger log = LoggerFactory.getLogger(HTTPDecorator.class);
    private final HTTP http;
    private final ExecutorService executorService;

    public HTTPDecorator(final HTTP http, final ExecutorService executorService) {
        Objects.requireNonNull(http);
        Objects.requireNonNull(executorService);
        this.http = http;
        this.executorService = executorService;
    }

    @Override
    public String getUserAgent() {
        return http.getUserAgent();
    }

    @Override
    public void execute(final Request request, final InputStream body, final CompletionCallback cb) {
        // We are in script thread initiating a request
        http.execute(request, body, (meta, entity, error) -> {
            // We are in HTTP IO thread receiving the response
            try {
                executorService.submit(() -> {
                    // We are in script thread passing the response to the session (and finally to script)
                    try {
                        cb.completed(meta, entity, error);
                    } catch (final InterruptedException ie) {
                        log.debug("Response interrupted; script is shutting down");
                        Thread.currentThread().interrupt();
                    }
                });
            } catch (final RejectedExecutionException e) {
                log.debug("Response rejected; script is shutting down");
                if (entity != null) {
                    try {
                        entity.close();
                    } catch (final IOException ioe) {
                        log.warn("Unable to close the response stream", ioe);
                    }
                }
            }
        });
    }

    @Override
    public void close() throws IOException {
        http.close();
    }
}

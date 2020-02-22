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

import fi.linuxbox.upcloud.http.spi.CompletionCallback
import fi.linuxbox.upcloud.http.spi.HTTP
import fi.linuxbox.upcloud.http.spi.META
import fi.linuxbox.upcloud.http.spi.Request
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.ExecutorService
import java.util.concurrent.RejectedExecutionException

@CompileStatic
@Slf4j
class HTTPDecorator implements HTTP {
    private final HTTP http
    private final ExecutorService executorService

    HTTPDecorator(final HTTP http, final ExecutorService executorService) {
        Objects.requireNonNull(http)
        Objects.requireNonNull(executorService)
        this.http = http
        this.executorService = executorService
    }

    @Override
    String getUserAgent() {
        http.getUserAgent()
    }

    @Override
    void execute(final Request request, final byte[] body, final CompletionCallback cb) {
        // We are in script thread initiating a request
        http.execute(request, body) { META meta, byte[] entity, Throwable error ->
            // Typically, we are in HTTP IO thread receiving the response.
            // But might be in script thread if the HTTP implementation called this completion thread
            // synchronously.
            try {
                executorService.submit {
                    // We are in script thread passing the response to the session (and finally to script)
                    try {
                        cb.completed(meta, entity, error)
                    } catch (final InterruptedException ie) {
                        log.debug("Response interrupted; script is shutting down", ie)
                        Thread.currentThread().interrupt()
                    }
                }
            } catch (final RejectedExecutionException e) {
                log.debug("Response rejected; script is shutting down", e)
            }
        }
    }

    @Override
    void close() {
        http.close()
    }
}

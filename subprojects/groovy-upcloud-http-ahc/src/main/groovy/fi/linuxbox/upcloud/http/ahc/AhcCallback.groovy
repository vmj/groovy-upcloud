/*
 * Groovy UpCloud library - HTTP AHC Module
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
package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.http.spi.CompletionCallback
import fi.linuxbox.upcloud.http.spi.META
import fi.linuxbox.upcloud.http.spi.Request
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.concurrent.FutureCallback

import java.util.concurrent.CancellationException
import java.util.concurrent.CompletionException

/**
 *
 */
@CompileStatic
@Slf4j
class AhcCallback implements FutureCallback<HttpResponse> {
    private final Request req
    private final CompletionCallback cb

    AhcCallback(final Request req, final CompletionCallback cb) {
        this.req = req
        this.cb = cb
    }

    @Override
    void completed(final HttpResponse response) {
        final StatusLine status = response?.statusLine
        if (status == null) {
            failed(new IllegalStateException("null response or null status line"))
            return
        }

        final META meta = new META(status.statusCode, status.reasonPhrase, new AhcHeaders(response), req)

        log.debug("Finished HTTP exchange ($meta)")
        cb.completed(meta, response.entity?.content, null)
    }

    @Override
    void failed(final Exception ex) {
        final msg = "failed to finish HTTP exchange ($req)"
        log.warn(msg, ex)
        cb.completed(null, null, new CompletionException(msg, ex))
    }

    @Override
    void cancelled() {
        final msg = "cancelled HTTP exchange ($req)"
        log.info(msg)
        cb.completed(null, null, new CancellationException(msg))
    }
}

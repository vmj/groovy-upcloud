/*
 * Groovy UpCloud library - HTTP AHC Module
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
package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.http.spi.CompletionCallback
import fi.linuxbox.upcloud.http.spi.ERROR
import fi.linuxbox.upcloud.http.spi.META
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.concurrent.FutureCallback

/**
 *
 */
@CompileStatic
@Slf4j
class AhcCallback implements FutureCallback<HttpResponse> {
    private CompletionCallback cb

    AhcCallback(final CompletionCallback cb) {
        this.cb = cb
    }

    @Override
    void completed(final HttpResponse response) {
        final StatusLine status = response?.statusLine
        if (status == null) {
            failed(new IllegalStateException("invalid response (null or no status line)"))
            return
        }

        META meta = new META(status.statusCode, status.reasonPhrase, new AhcHeaders(response))

        cb.completed(meta, response.entity?.content, null)
    }

    @Override
    void failed(final Exception ex) {
        log.warn("failed to finish HTTP exchange", ex)
        cb.completed(null, null, new ERROR("failed", ex))
    }

    @Override
    void cancelled() {
        log.info("cancelled HTTP exchange")
        cb.completed(null, null, new ERROR("cancelled"))
    }
}

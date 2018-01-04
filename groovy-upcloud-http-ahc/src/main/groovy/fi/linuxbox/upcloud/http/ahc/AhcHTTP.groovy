/*
 * Groovy UpCloud library - HTTP AHC Module
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
package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.http.spi.CompletionCallback
import fi.linuxbox.upcloud.http.spi.ERROR
import fi.linuxbox.upcloud.http.spi.Request
import fi.linuxbox.upcloud.http.spi.HTTP
import fi.linuxbox.upcloud.http.spi.Header
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.http.HttpHost
import org.apache.http.HttpRequest
import org.apache.http.ProtocolVersion
import org.apache.http.RequestLine
import org.apache.http.entity.BasicHttpEntity
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClients
import org.apache.http.message.BasicHttpEntityEnclosingRequest
import org.apache.http.message.BasicHttpRequest
import org.apache.http.message.BasicRequestLine
import org.apache.http.util.VersionInfo

import javax.inject.Inject

/**
 * This is thread safe if client close(), isRunning(), start(), and
 * execute(HttpHost, HttpRequest, FutureCallback<HttpResponse>) are.
 */
@CompileStatic
@Slf4j
class AhcHTTP implements HTTP {
    private final static ProtocolVersion HTTP_1_1 = new ProtocolVersion("HTTP", 1, 1)

    private final CloseableHttpAsyncClient client

    @Inject
    AhcHTTP(final CloseableHttpAsyncClient client) {
        this.client = client
    }

    AhcHTTP() {
        this(new AhcClientProvider().get())
    }

    @Override
    void close() throws IOException {
        client.close()
    }

    @Override
    String getUserAgent() {
        // Following was how the default User-Agent string is formed in HttpAsyncClient version 4.1.1
        // org.apache.http.impl.nio.client.MinimalHttpAsyncClientBuilder#build() lines 133-134.
        VersionInfo.getUserAgent("Apache-HttpAsyncClient", "org.apache.http.nio.client", HttpAsyncClients)
    }

    @Override
    void execute(final Request request, final CompletionCallback cb) {
        if (client == null)
            throw new IllegalStateException("no client provided")
        if (request == null)
            throw new IllegalArgumentException("request is null")
        if (cb == null)
            throw new IllegalArgumentException("cb is null")

        try {
            doExecute(request, cb)
        } catch (final Exception e) {
            log.warn("failed to start HTTP exchange", e)
            cb.completed(null, null, new ERROR("failed to start HTTP exchange", e))
        }
    }

    private void doExecute(final Request request, final CompletionCallback cb) {
        // isRunning() and start() are only available in this CloseableHAC API :/
        if (!client.running)
            client.start()

        HttpRequest req = request.headers.inject(toHttpRequest(request)) { HttpRequest req, Header header ->
            req.addHeader(header.name, header.value)
            req
        }

        client.execute(HttpHost.create(request.host), req, new AhcCallback(cb))
    }

    private HttpRequest toHttpRequest(final Request request) {
        final RequestLine rl = new BasicRequestLine(request.method, request.resource, HTTP_1_1)

        if (request.body) {
            // BasicHTTPEntity will close the content when done with it
            BasicHttpEntity entity = new BasicHttpEntity()
            entity.content = request.body

            BasicHttpEntityEnclosingRequest req = new BasicHttpEntityEnclosingRequest(rl)
            req.entity = entity
            return req
        }
        return new BasicHttpRequest(rl)
    }
}

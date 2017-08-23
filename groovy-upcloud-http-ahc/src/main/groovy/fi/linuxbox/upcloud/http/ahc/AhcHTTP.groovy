package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.http.spi.ERROR
import fi.linuxbox.upcloud.http.spi.Exchange
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
class AhcHTTP implements HTTP, Closeable {
    private final static ProtocolVersion HTTP_1_1 = new ProtocolVersion("HTTP", 1, 1)

    private final CloseableHttpAsyncClient client

    @Inject
    AhcHTTP(final CloseableHttpAsyncClient client) {
        this.client = client
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
    void execute(final Exchange exchange) {
        if (client == null)
            throw new IllegalStateException("no client provided")
        if (exchange == null)
            throw new IllegalArgumentException("exchange is null")
        if (exchange.cb == null)
            throw new IllegalArgumentException("exchange.cb is null")

        try {
            doExecute(exchange)
        } catch (final Exception e) {
            log.warn("failed to start HTTP exchange", e)
            final Closure<Void> cb = exchange.cb
            cb(null, null, new ERROR("failed to start HTTP exchange", e))
        }
    }

    private void doExecute(final Exchange exchange) {
        // isRunning() and start() are only available in this CloseableHAC API :/
        if (!client.running)
            client.start()

        HttpRequest request = exchange.headers.all()
                .inject(request(exchange)) { HttpRequest request, Header header ->
            request.addHeader(header.name, header.value)
            request
        }

        client.execute(HttpHost.create(exchange.host), request, new AhcCallback(exchange.cb))
    }

    private HttpRequest request(final Exchange exchange) {
        final RequestLine rl = new BasicRequestLine(exchange.method, exchange.resource, HTTP_1_1)

        if (exchange.body) {
            BasicHttpEntity entity = new BasicHttpEntity()
            entity.content = exchange.body

            BasicHttpEntityEnclosingRequest req = new BasicHttpEntityEnclosingRequest(rl)
            req.entity = entity
            return req
        }
        return new BasicHttpRequest(rl)
    }
}

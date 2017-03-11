package fi.linuxbox.upcloud.http.ahc

import javax.inject.*
import org.apache.http.*
import org.apache.http.entity.*
import org.apache.http.impl.nio.client.*
import org.apache.http.message.*
import org.apache.http.nio.client.*
import org.apache.http.util.*
import org.slf4j.*
import fi.linuxbox.upcloud.core.http.*

/**
 *
 */
class AhcHTTP implements HTTP, Closeable {
    private final Logger log = LoggerFactory.getLogger(AhcHTTP)

    private final static ProtocolVersion HTTP_1_1 = new ProtocolVersion("HTTP", 1, 1)

    private final HttpAsyncClient client

    @Inject
    AhcHTTP(final HttpAsyncClient client) {
        this.client = client
    }

    @Override
    void close() throws IOException {
        if (client instanceof Closeable)
            client.close()
    }

    @Override
    String getUserAgent() {
        // Following was how the default User-Agent string is formed in HttpAsyncClient version 4.1.1
        // org.apache.http.impl.nio.client.MinimalHttpAsyncClientBuilder#build() lines 133-134.
        VersionInfo.getUserAgent("Apache-HttpAsyncClient", "org.apache.http.nio.client", HttpAsyncClients);
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
            exchange.cb(null, null, new ERROR("failed to start HTTP exchange", e))
        }
    }

    private void doExecute(final Exchange exchange) {
        // isRunning() and start() are only available in this CloseableHAC API :/
        if (client instanceof CloseableHttpAsyncClient && !client.running)
            client.start()

        HttpRequest request = exchange.headers.all().inject(request(exchange)) { HttpRequest request, def header ->
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

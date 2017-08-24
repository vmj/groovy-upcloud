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

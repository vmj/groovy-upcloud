package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.core.http.ERROR
import fi.linuxbox.upcloud.core.http.META
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.concurrent.FutureCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 */
class AhcCallback implements FutureCallback<HttpResponse> {
    private final Logger log = LoggerFactory.getLogger(AhcCallback)

    private Closure<Void> cb

    AhcCallback(final Closure<Void> cb) {
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

        cb(meta, response.entity?.content, null)
    }

    @Override
    void failed(final Exception ex) {
        log.warn("failed to finish HTTP exchange", e)
        cb(null, null, new ERROR("failed", ex))
    }

    @Override
    void cancelled() {
        log.info("cancelled HTTP exchange")
        cb(null, null, new ERROR("cancelled"))
    }
}

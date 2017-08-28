package fi.linuxbox.upcloud.script;

import fi.linuxbox.upcloud.http.spi.CompletionCallback;
import fi.linuxbox.upcloud.http.spi.HTTP;
import fi.linuxbox.upcloud.http.spi.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    public void execute(final Request request, final CompletionCallback cb) {
        // We are in script thread initiating a request
        http.execute(request, (meta, entity, error) -> {
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

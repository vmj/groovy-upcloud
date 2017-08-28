package fi.linuxbox.upcloud.http.spi;

import java.io.Closeable;

/**
 * An interface which is implemented by the HTTP implementation.
 */
public interface HTTP extends Closeable {
    /**
     * Returns the User-Agent string for this implementation.
     *
     * @return User-Agent string for this implementation.
     */
    String getUserAgent();

    /**
     * Execute the HTTP exchange asynchronously.
     *
     * @param request HTTP request to execute.
     * @param cb Completion callback to call when the response is received.
     */
    void execute(final Request request, final CompletionCallback cb);
}

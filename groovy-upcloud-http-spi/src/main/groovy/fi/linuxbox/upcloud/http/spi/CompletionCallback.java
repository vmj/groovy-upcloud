package fi.linuxbox.upcloud.http.spi;

import java.io.InputStream;

/**
 * HTTP completion callback.
 */
public interface CompletionCallback {
    /**
     * Method to invoke when the HTTP exchange is finished.
     * <p>
     *     In case of failed network or cancelled request, the <code>META</code> must be <code>null</code> and the
     *     <code>ERROR</code> should be non-<code>null</code>.  In case the server responds with an HTTP error,
     *     <code>META</code> must be non-<code>null</code> and <code>ERROR</code> must be <code>null</code>.  The
     *     second argument, the <code>InputStream</code>, can be <code>null</code> if the HTTP response
     *     didn't have an entity body.
     * </p>
     * <p>
     *     The Session will close the input stream when done with it.
     * </p>
     *
     * @param meta information about the response, or <code>null</code>
     * @param entity response entity or <code>null</code>
     * @param error information about failure to talk to the server, or <code>null</code>
     * @throws InterruptedException in case the caller is shutting down
     */
    void completed(final META meta, final InputStream entity, final ERROR error) throws InterruptedException;
}

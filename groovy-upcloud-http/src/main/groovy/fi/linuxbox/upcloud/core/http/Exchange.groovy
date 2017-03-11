package fi.linuxbox.upcloud.core.http

/**
 * An HTTP exchange.
 *
 * <p>
 * This class is used between API and HTTP implementations.
 * </p>
 */
class Exchange {
    /**
     * Target host of the HTTP exchange.
     * <p>
     * This must contain the protocol, and the port if needed, but must not end in a slash.
     * </p>
     */
    String host
    /**
     * HTTP method to use in the request.
     */
    String method
    /**
     * The resource ID.
     * <p>
     * This must be the full path to the resource, starting with the slash.
     * </p>
     */
    String resource
    /**
     * Headers to include in the request.
     */
    Headers headers
    /**
     * Request entity, or <code>null</code>.
     */
    InputStream body
    /**
     * Completion callback.
     * <p>
     *     This must be called with three arguments:
     * </p>
     * <ol>
     *     <li>{@link META META} instance or <code>null</code></li>
     *     <li>response entity as an {@link java.io.InputStream InputStream} or <code>null</code>, and
     *     <li>{@link ERROR ERROR} instance or <code>null</code></li>
     * </ol>
     * <p>
     *     In case of failed network or cancelled request, the <code>META</code> must be <code>null</code> and the
     *     <code>ERROR</code> should be non-<code>null</code>.  In case the server responds with an HTTP error,
     *     <code>META</code> must be non-<code>null</code> and <code>ERROR</code> must be <code>null</code>.  The
     *     second argument, the <code>InputStream</code>, can be <code>null</code> if the HTTP response
     *     didn't have an entity body.
     * </p>
     */
    Closure<Void> cb
}

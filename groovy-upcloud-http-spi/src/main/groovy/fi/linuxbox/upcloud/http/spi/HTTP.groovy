package fi.linuxbox.upcloud.http.spi

/**
 * An interface which is implemented by the HTTP implementation.
 */
interface HTTP {
    /**
     * Returns the User-Agent string for this implementation.
     *
     * @return User-Agent string for this implementation.
     */
    String getUserAgent()

    /**
     * Execute the HTTP exchange asynchronously.
     * <p>
     *     The completion callback must be called with three arguments:
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
     *
     * @param request HTTP request to execute.
     * @param cb Completion callback.
     */
    void execute(final Request request, final Closure<Void> cb)
}

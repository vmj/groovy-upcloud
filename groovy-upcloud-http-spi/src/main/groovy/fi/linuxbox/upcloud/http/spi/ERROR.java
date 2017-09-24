package fi.linuxbox.upcloud.http.spi;

/**
 * Error container.
 *
 * <p>
 * This is used only in cases where the communication with the server fails, not when the server responds with an error
 * response.  For example, when the server is unreachable, an instance of this class is instantiated.  But if the server
 * responds with, say, '403 Forbidden' because the username or password was revoked or mistyped, or with, say,
 * '500 Internal Server Error' because the server crashed, then this class is not used.  Instead, the {@link META} in
 * the {@link fi.linuxbox.upcloud.core.Resource} will contain the status code and the reason phrase, and the
 * Resource will probably have an {@code error} property with an instance of
 * {@link fi.linuxbox.upcloud.resource.Error Error} as the value.
 * </p>
 *
 * <p>
 * For example:
 * </p>
 *
 * <pre><code class="groovy">
 *     session.callback(
 *         server_error: { Resource response ->
 *             // Session callback (for any 5xx HTTP response).
 *             // If this is called, it is always called with one argument.
 *             assert response.META.status in (500..599) // 5xx status code
 *             log.warn "${response.error.errorCode}: ${response.error.errorMessage}"
 *         })
 *     session.GET('some-resource',
 *         client_error: { Resource response ->
 *             // Additional request callback (for any 4xx HTTP response).
 *             // If this is called, it is always called with one argument.
 *             assert response.META.status in (400..499) // 4xx status code
 *             log.warn "${response.error.errorCode}: ${response.error.errorMessage}"
 *         }, { Resource response, ERROR err ->
 *             // Default request callback.
 *             // If this is called, it is either with
 *             //    1. non-null Resource and null ERROR, or
 *             //    2. null Resource and non-null ERROR
 *             if (err) {
 *                 log.fatal "Network error: ${err.message}"
 *                 Throwable cause = err.cause
 *                 while (cause != null) {
 *                     log.fatal "Caused by: ${cause.class.simpleName}: ${cause.message}"
 *                     if (cause == cause.cause)
 *                         break
 *                     cause = cause.cause
 *                 }
 *             }
 *         })
 * </code></pre>
 *
 * <p>
 * Because the default callbacks and the additional request callbacks are by definition tied to HTTP response status
 * codes and categories, they will never be invoked for network level errors.  If your default request callback doesn't
 * take two arguments, then in case of network level errors it will just receive a null resource.
 * </p>
 */
public class ERROR {
    private final String message;
    private final Throwable cause;

    /**
     * Constructor that sets the cause to null.
     *
     * @param message Short explanatory description of this error for human consumption.
     */
    public ERROR(final String message) {
        this(message, null);
    }

    /**
     * Designated constructor.
     *
     * @param message Short explanatory description of this error for human consumption.
     * @param cause Exception that caused this error
     */
    public ERROR(final String message, final Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    /**
     * Short explanatory description of this error for human consumption.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Possibly null exception that caused this error.
     */
    public Throwable getCause() {
        return cause;
    }
}

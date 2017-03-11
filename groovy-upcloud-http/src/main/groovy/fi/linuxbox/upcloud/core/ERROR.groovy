package fi.linuxbox.upcloud.core

/**
 * Error container.
 *
 * <p>
 * This is used only in cases where the communication with the server fails, not when the server responds with an error
 * response.  For example, when the server is unreachable, an instance of this class is instantiated.  But if the server
 * responds with, say, '403 Forbidden' because the username or password was revoked or mistyped, or with, say,
 * '500 Internal Server Error' because the server crashed, then this class is not used.  Instead, the {@link META} in
 * the {@link Resource} will contain the status code and the reason phrase, and the Resource will probably have an error
 * property with an instance of {@link fi.linuxbox.upcloud.resource.Error Error} as the value.
 * </p>
 *
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 *     api.callback(
 *         server_error: { Resource response ->
 *             // Default callback (for any 5xx HTTP response).
 *             // If this is called, it is always called with one argument.
 *             assert response.META.status in (500..599) // 5xx status code
 *
 *         })
 *     api.GET('some-resource',
 *         client_error: { Resource response ->
 *             // Additional request callback (for any 4xx HTTP response).
 *             // If this is called, it is always called with one argument.
 *         }, { Resource response, ERROR err ->
 *             // Default request callback.
 *             // If this is called, it is either with
 *             //    1. non-null Resource and null ERROR, or
 *             //    2. null Resource and non-null ERROR
 *         })
 * </pre>
 *
 * <p>
 * Because the default callbacks and the additional request callbacks are by definition tied to HTTP response status
 * codes and categories, they will never be invoked for network level errors.  If your default request callback doesn't
 * take two arguments, then in case of network level errors it will just receive a null resource.
 * </p>
 */
class ERROR {
    /**
     * Short explanatory description of this error for human consumption.
     */
    final String message
    /**
     * Possibly null exception that caused this error.
     */
    final Throwable cause

    /**
     * Constructor that sets the cause to null.
     *
     * @param message Short explanatory description of this error for human consumption.
     */
    ERROR(final String message) {
        this(message, null)
    }

    /**
     * Designated constructor.
     *
     * @param message Short explanatory description of this error for human consumption.
     * @param cause Exception that caused this error
     */
    ERROR(final String message, final Throwable cause) {
        this.message = message
        this.cause = cause
    }
}

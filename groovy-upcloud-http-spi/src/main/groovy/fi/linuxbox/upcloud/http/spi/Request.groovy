package fi.linuxbox.upcloud.http.spi

/**
 * An HTTP request.
 *
 * <p>
 * This class is used between Session and HTTP implementations.
 * </p>
 */
class Request {
    /**
     * Target host of the HTTP request.
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
}

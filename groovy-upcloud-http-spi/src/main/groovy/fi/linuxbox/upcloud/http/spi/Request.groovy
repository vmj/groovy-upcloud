package fi.linuxbox.upcloud.http.spi

import groovy.transform.CompileStatic
import groovy.transform.Immutable

/**
 * An HTTP request.
 *
 * <p>
 * This class is used between Session and HTTP implementations.
 * </p>
 */
@CompileStatic
@Immutable(knownImmutableClasses = [Headers.class, InputStream])
class Request {
    /**
     * Target host of the HTTP request.
     * <p>
     * This must contain the protocol, and the port if needed, but must not end in a slash.
     * </p>
     */
    final String host
    /**
     * HTTP method to use in the request.
     */
    final String method
    /**
     * The resource ID.
     * <p>
     * This must be the full path to the resource, starting with the slash.
     * </p>
     */
    final String resource
    /**
     * Headers to include in the request.
     */
    final Headers headers
    /**
     * Request entity, or <code>null</code>.
     * <p>
     *     The HTTP implementation is responsible for closing this input stream when done with it.
     * <p>
     */
    final InputStream body
}

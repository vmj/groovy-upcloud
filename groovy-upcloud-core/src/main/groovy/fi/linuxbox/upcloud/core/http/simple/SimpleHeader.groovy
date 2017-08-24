package fi.linuxbox.upcloud.core.http.simple

import fi.linuxbox.upcloud.http.spi.HeaderElement
import fi.linuxbox.upcloud.http.spi.Header

/**
 * A simple header.
 *
 * <p>
 * This class does not support header element parsing.  I.e. all you can do is take the header name and raw value.
 * That just happens to be all that the HTTP implementations currently need in order to build the HTTP request.
 * </p>
 *
 * <p>
 * This class is used in communication between Session and the HTTP implementation.  In responses, the HTTP implementation
 * will provide fully functional headers for the application.
 * </p>
 */
class SimpleHeader implements Header {
    private final Map.Entry<String, String> header

    SimpleHeader(final Map.Entry<String, String> header) {
        this.header = header
    }

    @Override
    String getName() {
        header.key
    }

    @Override
    String getValue() {
        header.value
    }

    @Override
    Iterator<HeaderElement> iterator() {
        throw new UnsupportedOperationException("SimpleHeader should not be parsed; just set it to request")
    }
}

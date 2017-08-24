package fi.linuxbox.upcloud.core.http.simple

import fi.linuxbox.upcloud.http.spi.Header
import fi.linuxbox.upcloud.http.spi.HeaderElement
import fi.linuxbox.upcloud.http.spi.Headers


/**
 * A simple header collection.
 *
 * <p>
 * This class does not support header element parsing.  I.e. all you can do is iterate over all the headers and take
 * their names and raw values.  That just happens to be all that the HTTP implementations currently need in order to
 * build the HTTP requests.
 * </p>
 *
 * <p>
 * This class is used in communication between Session and the HTTP implementation.  In responses, the HTTP implementation
 * will provide fully functional headers for the application.
 * </p>
 */
class SimpleHeaders implements Headers {
    private final Map<String, String> headers

    SimpleHeaders(final Map<String, String> headers) {
        this.headers = headers
    }

    @Override
    Iterator<Header> iterator() {
        new SimpleHeaderIterator(headers.iterator())
    }

    @Override
    Iterator<HeaderElement> getAt(final String name) {
        throw new UnsupportedOperationException("SimpleHeaders should not be parsed; just set them to request")
    }
}

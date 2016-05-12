package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.core.http.*
import org.apache.http.*
import org.apache.http.message.*

/**
 *
 */
class AhcHeaders implements Headers {
    private final HttpMessage httpMessage

    AhcHeaders(final HttpMessage httpMessage) {
        this.httpMessage = httpMessage
    }

    @Override
    Iterator<Header> all() {
        new AhcHeaderIterator(httpMessage.headerIterator())
    }

    @Override
    Iterator<HeaderElement> getAt(final String header) {
        new AhcHeaderElementIterator(new BasicHeaderElementIterator(httpMessage.headerIterator(header)))
    }

    @Override
    void putAt(final String name, final String value) {
        httpMessage.addHeader(name, value)
    }

    private static class AhcHeaderIterator implements Iterator<Header> {
        final HeaderIterator it

        AhcHeaderIterator(final HeaderIterator it) {
            this.it = it
        }

        @Override
        boolean hasNext() {
            it.hasNext()
        }

        @Override
        Header next() {
            new AhcHeader(it.nextHeader())
        }
    }

    private static class AhcHeaderElementIterator implements Iterator<HeaderElement> {
        final BasicHeaderElementIterator it

        AhcHeaderElementIterator(final BasicHeaderElementIterator it) {
            this.it = it
        }

        @Override
        boolean hasNext() {
            it.hasNext()
        }

        @Override
        HeaderElement next() {
            new AhcHeaderElement(it.nextElement())
        }
    }}

package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.core.http.HeaderElement
import org.apache.http.Header

/**
 *
 */
class AhcHeader implements fi.linuxbox.upcloud.core.http.Header {
    private final Header header

    AhcHeader(final Header header) {
        this.header = header
    }

    @Override
    String getName() {
        header.name
    }

    @Override
    String getValue() {
        header.value
    }

    @Override
    Iterator<HeaderElement> getElements() {
        new AhcHeaderElementIterator(header.elements.iterator())
    }

    private static class AhcHeaderElementIterator implements Iterator<HeaderElement> {
        private final Iterator<org.apache.http.HeaderElement> it

        AhcHeaderElementIterator(final Iterator<org.apache.http.HeaderElement> it) {
            this.it = it
        }

        @Override
        boolean hasNext() {
            return it.hasNext()
        }

        @Override
        HeaderElement next() {
            new AhcHeaderElement(it.next())
        }
    }
}

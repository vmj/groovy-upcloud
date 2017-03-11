package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.core.http.Header
import fi.linuxbox.upcloud.core.http.HeaderElement
import org.apache.http.Header as HeaderImpl
import org.apache.http.HeaderElement as HeaderElementImpl

/**
 *
 */
class AhcHeader implements Header {
    private final HeaderImpl header

    AhcHeader(final HeaderImpl header) {
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
        private final Iterator<HeaderElementImpl> it

        AhcHeaderElementIterator(final Iterator<HeaderElementImpl> it) {
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

        @Override
        void remove() {
            it.remove()
        }
    }
}

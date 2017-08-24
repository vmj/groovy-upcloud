package fi.linuxbox.upcloud.core.http.simple

import fi.linuxbox.upcloud.http.spi.Header


/**
 * Iterator over simple headers.
 */
class SimpleHeaderIterator implements Iterator<Header> {
    private final Iterator<Map.Entry<String, String>> it

    SimpleHeaderIterator(final Iterator<Map.Entry<String, String>> it) {
        this.it = it
    }

    @Override
    boolean hasNext() {
        it.hasNext()
    }

    @Override
    Header next() {
        new SimpleHeader(it.next())
    }
}

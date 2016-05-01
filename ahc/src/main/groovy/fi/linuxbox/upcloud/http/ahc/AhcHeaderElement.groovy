package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.core.http.Parameter
import org.apache.http.HeaderElement
import org.apache.http.NameValuePair

/**
 *
 */
class AhcHeaderElement implements fi.linuxbox.upcloud.core.http.HeaderElement {
    private final HeaderElement element

    AhcHeaderElement(final HeaderElement element) {
        this.element = element
    }

    @Override
    String getName() { element.name }

    @Override
    String getValue() { element.value }

    @Override
    Iterator<Parameter> getParameters() {
        new AhcHeaderParameterIterator(element.parameters.iterator())
    }

    private static class AhcHeaderParameterIterator implements Iterator<Parameter> {
        private final Iterator<NameValuePair> it

        AhcHeaderParameterIterator(final Iterator<NameValuePair> it) {
            this.it = it
        }

        @Override
        boolean hasNext() {
            return it.hasNext()
        }

        @Override
        Parameter next() {
            final NameValuePair nvp = it.next()
            return new Parameter(nvp.name, nvp.value)
        }
    }
}

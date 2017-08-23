package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.http.spi.HeaderElement
import fi.linuxbox.upcloud.http.spi.Parameter
import groovy.transform.CompileStatic
import org.apache.http.NameValuePair
import org.apache.http.HeaderElement as HeaderElementImpl

/**
 *
 */
@CompileStatic
class AhcHeaderElement implements HeaderElement {
    private final HeaderElementImpl element

    AhcHeaderElement(final HeaderElementImpl element) {
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

        @Override
        void remove() {
            it.remove()
        }
    }
}

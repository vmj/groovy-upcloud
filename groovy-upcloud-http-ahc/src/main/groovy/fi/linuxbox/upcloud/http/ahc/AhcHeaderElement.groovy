/*
 * Groovy UpCloud library - HTTP AHC Module
 * Copyright (C) 2018  <mikko@varri.fi>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    }
}

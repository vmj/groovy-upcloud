/*
 * Groovy UpCloud library - HTTP AHC Module
 * Copyright (C) 2018  Mikko Värri
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

import fi.linuxbox.upcloud.http.spi.Header
import fi.linuxbox.upcloud.http.spi.HeaderElement
import groovy.transform.CompileStatic
import org.apache.http.Header as HeaderImpl
import org.apache.http.HeaderElement as HeaderElementImpl

/**
 *
 */
@CompileStatic
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
    }
}

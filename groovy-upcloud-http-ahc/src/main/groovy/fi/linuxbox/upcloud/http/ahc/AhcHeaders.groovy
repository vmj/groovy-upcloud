/*
 * Groovy UpCloud library - HTTP AHC Module
 * Copyright (C) 2017  <mikko@varri.fi>
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
import fi.linuxbox.upcloud.http.spi.Headers
import groovy.transform.CompileStatic
import org.apache.http.HeaderIterator
import org.apache.http.HttpMessage
import org.apache.http.message.BasicHeaderElementIterator

/**
 *
 */
@CompileStatic
class AhcHeaders implements Headers {
    private final HttpMessage httpMessage

    AhcHeaders(final HttpMessage httpMessage) {
        this.httpMessage = httpMessage
    }

    @Override
    Iterator<Header> iterator() {
        new AhcHeaderIterator(httpMessage.headerIterator())
    }

    @Override
    Iterator<HeaderElement> getAt(final String header) {
        new AhcHeaderElementIterator(new BasicHeaderElementIterator(httpMessage.headerIterator(header)))
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
    }
}

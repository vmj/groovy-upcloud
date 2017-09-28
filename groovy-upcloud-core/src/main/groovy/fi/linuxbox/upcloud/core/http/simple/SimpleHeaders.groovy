/*
 * Groovy UpCloud library - Core Module
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

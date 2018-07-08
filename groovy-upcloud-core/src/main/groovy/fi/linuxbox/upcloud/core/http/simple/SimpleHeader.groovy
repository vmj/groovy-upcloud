/*
 * Groovy UpCloud library - Core Module
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
package fi.linuxbox.upcloud.core.http.simple

import fi.linuxbox.upcloud.http.spi.HeaderElement
import fi.linuxbox.upcloud.http.spi.Header

/**
 * A simple header.
 *
 * <p>
 * This class does not support header element parsing.  I.e. all you can do is take the header name and raw value.
 * That just happens to be all that the HTTP implementations currently need in order to build the HTTP request.
 * </p>
 *
 * <p>
 * This class is used in communication between Session and the HTTP implementation.  In responses, the HTTP implementation
 * will provide fully functional headers for the application.
 * </p>
 */
class SimpleHeader implements Header {
    private final Map.Entry<String, String> header

    SimpleHeader(final Map.Entry<String, String> header) {
        this.header = header
    }

    @Override
    String getName() {
        header.key
    }

    @Override
    String getValue() {
        header.value
    }

    @Override
    Iterator<HeaderElement> getElements() {
        throw new UnsupportedOperationException("SimpleHeader should not be parsed; just set it to request")
    }
}

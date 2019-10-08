/*
 * Groovy UpCloud library - HTTP SPI Module
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
package fi.linuxbox.upcloud.http.spi

import groovy.transform.CompileStatic
import groovy.transform.Immutable

/**
 * An HTTP request.
 *
 * <p>
 * This class is used between Session and HTTP implementations.
 * </p>
 */
@CompileStatic
@Immutable
class Request {
    /**
     * Target host of the HTTP request.
     * <p>
     * This must contain the protocol, and the port if needed, but must not end in a slash.
     * </p>
     */
    String host
    /**
     * HTTP method to use in the request.
     */
    String method
    /**
     * The resource ID.
     * <p>
     * This must be the full path to the resource, starting with the slash.
     * </p>
     */
    String resource
    /**
     * Headers to include in the request.
     */
    Headers headers

    /**
     * Returns a string representation of this instance.
     *
     * @return A string representation.
     */
    @Override
    String toString() {
        "$method $resource"
    }
}

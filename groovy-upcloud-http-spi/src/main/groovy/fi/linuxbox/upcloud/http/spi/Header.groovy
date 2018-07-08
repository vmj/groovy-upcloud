/*
 * Groovy UpCloud library - HTTP SPI Module
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
package fi.linuxbox.upcloud.http.spi

import groovy.transform.CompileStatic

/**
 * An HTTP header.
 *
 * <p>
 * An HTTP header is a name value pair, where the value can have multiple elements embedded in it.
 * </p>
 */
@CompileStatic
interface Header {

    /**
     * Name of this HTTP header.
     *
     * <p>
     * E.g. 'Set-Cookie' in a header 'Set-Cookie: c2=b; path="/", c3=c; domain="localhost"'.
     * </p>
     *
     * @return The name of this HTTP header.
     */
    String getName()

    /**
     * Value of this HTTP header.
     *
     * <p>
     * E.g. given a header 'Set-Cookie: c2=b; path="/", c3=c; domain="localhost"', this will return
     * 'c2=b; path="/", c3=c; domain="localhost"', i.e. two elements in the string.
     * </p>
     *
     * <p>
     * If you need to access each value element of this header separately, use the {#iterator()} method.
     * </p>
     *
     * @return The value of this HTTP header.
     */
    String getValue()

    /**
     * Iterator over separate elements of this HTTP header value.
     *
     * <p>
     * This iterator will allow you to walk through the comma separated parts of the value of this HTTP header.
     * </p>
     *
     * <p>
     * E.g. given a header 'Set-Cookie: c2=b; path="/", c3=c; domain="localhost"', this iterator will first
     * given you the header element corresponding to 'c2=b; path="/"', and then the header element corresponding to
     * 'c3=c; domain="localhost"'.
     *
     * @return Iterator over elements of the value of this HTTP header.
     */
    Iterator<HeaderElement> getElements()
}

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

/**
 * An HTTP header value element.
 *
 * <p>
 * An HTTP header value element can have a semicolon separated list of parameters.
 * </p>
 */
@CompileStatic
interface HeaderElement {
    /**
     * Name of this HTTP header value element.
     *
     * <p>
     * E.g. 'application/json' in header 'Accept: application/json; charset=UTF-8'.
     * </p>
     *
     * @return Name of this element.
     */
    String getName()

    /**
     * Value of this HTTP header value element.
     *
     * <p>
     * E.g. '123' in header 'Set-Cookie: sessionId=123', but <code>null</code> in header 'Accept: test/plain'.
     * </p>
     *
     * @return Value of this element.
     */
    String getValue()

    /**
     * Iterator over the parameters of this HTTP header value element.
     *
     * @return Iterator over parameters of this element.
     */
    Iterator<Parameter> getParameters()
}

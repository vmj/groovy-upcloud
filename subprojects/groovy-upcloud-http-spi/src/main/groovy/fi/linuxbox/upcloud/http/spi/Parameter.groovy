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
 * An HTTP header parameter.
 *
 * <p>
 * HTTP header parameters are a list of name value pairs, and part of the header value.
 * </p>
 */
@CompileStatic
class Parameter {
    final Tuple2<String, String> t

    /**
     * The sole constructor.
     *
     * @param name Name of the parameter.  E.g. "charset" in a header "Accept: application/json; charset=UTF-8"
     * @param value value of the parameter.  E.g. "UTF-8" in a header "Accept: application/json; charset=UTF-8"
     */
    Parameter(final String name, final String value) { t = new Tuple2<>(name, value) }

    /**
     * Name of this HTTP header parameter.
     *
     * <p>
     * E.g. "charset" in a header "Accept: application/json; charset=UTF-8".
     * </p>
     *
     * @return The name of this HTTP header parameter.
     */
    String getName() { t.first }

    /**
     * Value of this HTTP header parameter.
     *
     * <p>
     * E.g. "UTF-8" in a header "Accept: application/json; charset=UTF-8".
     * </p>
     *
     * @return The value of this HTTP header parameter.
     */
    String getValue() { t.second }
}

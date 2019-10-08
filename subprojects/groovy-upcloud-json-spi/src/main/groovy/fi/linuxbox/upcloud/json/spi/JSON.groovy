/*
 * Groovy UpCloud library - JSON SPI Module
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
package fi.linuxbox.upcloud.json.spi

import groovy.transform.CompileStatic

/**
 * An interface which is implemented by the JSON implementation.
 */
@CompileStatic
interface JSON {
    /**
     * Parse the representation from the input stream.
     *
     * @param data The UTF-8 JSON data.
     * @return Java representation of the JSON.
     */
    Map<String, Object> decode(InputStream data)

    /**
     * Write the representation to a stream.
     *
     * @param repr Java representation of the JSON to be written.
     * @return The UTF-8 JSON data as an input stream.
     */
    InputStream encode(Map<String, Object> repr)
}

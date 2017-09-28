/*
 * Groovy UpCloud library - JSON Jackson Module
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
package fi.linuxbox.upcloud.json.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import fi.linuxbox.upcloud.json.spi.JSON
import groovy.transform.CompileStatic

import javax.inject.Inject

/**
 *
 */
@CompileStatic
class JacksonJSON implements JSON {
    private final ObjectMapper mapper

    @Inject
    JacksonJSON(final ObjectMapper mapper) {
        this.mapper = mapper
    }

    JacksonJSON() {
        this(new JacksonParserProvider().get())
    }

    @Override
    Map<String, Object> decode(final InputStream data) {
        mapper.readValue(data, Map)
    }

    @Override
    InputStream encode(final Map<String, Object> repr) {
        new ByteArrayInputStream(mapper.writeValueAsBytes(repr))
    }
}

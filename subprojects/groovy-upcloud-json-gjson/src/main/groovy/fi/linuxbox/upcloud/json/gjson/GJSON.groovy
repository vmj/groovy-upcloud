/*
 * Groovy UpCloud library - JSON GJson Module
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
package fi.linuxbox.upcloud.json.gjson

import fi.linuxbox.upcloud.json.spi.JSON
import groovy.json.JsonOutput
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

import java.nio.charset.Charset

/**
 *
 */
@CompileStatic
class GJSON implements JSON {
    private static final Charset UTF_8 = Charset.forName("UTF-8")

    private final JsonSlurper parser = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)

    @Override
    Map<String, Object> decode(final byte[] data) {
        (Map<String, Object>) parser.parse(data)
    }

    @Override
    byte[] encode(final Map<String, Object> repr) {
        JsonOutput.toJson(repr).getBytes(UTF_8)
    }
}

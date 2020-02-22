/*
 * Groovy UpCloud library - Core Module
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
package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.http.spi.META
import spock.lang.Specification

class ResourceUtilSpec extends Specification {
    def "Resource properties"() {
        given:
        final meta = new META()
        final r = new Resource(META: meta, HTTP: Mock(HTTPFacade), repr: [foo: 1, bar: 2, baz: null])

        when:
        Map<String, Object> m = ResourceUtil.resourceProperties(r)

        then:
        m.containsKey("foo")
        m.containsKey("bar")
        !m.containsKey("baz")
        !m.containsKey("class")
        !m.containsKey("META")
        !m.containsKey("HTTP")
    }
}

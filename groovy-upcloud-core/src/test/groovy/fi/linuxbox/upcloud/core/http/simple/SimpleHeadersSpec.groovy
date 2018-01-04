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

import fi.linuxbox.upcloud.http.spi.Header
import fi.linuxbox.upcloud.http.spi.Headers
import spock.lang.Shared
import spock.lang.Specification

class SimpleHeadersSpec extends Specification {
    @Shared Headers headers = new SimpleHeaders(foo: 'bar')

    def "SimpleHeaders can return an iterator"() {
        expect:
        headers.iterator() != null
    }

    def "SimpleHeaders does not support header elements"() {
        when:
        headers["foo"]

        then:
        thrown(UnsupportedOperationException)
    }

    def "SimpleHeaders -> Iterator<Header> is read-only"() {
        when:
        headers.iterator().remove()

        then:
        thrown(UnsupportedOperationException)
    }

    def "SimpleHeaders header iterator works"() {
        given:
        Iterator<Header> iter = headers.iterator()

        expect:
        iter.hasNext()
        iter.next() != null
        !iter.hasNext()
    }

    def "SimpleHeader name and value works"() {
        given:
        Header header = headers.first()

        expect:
        header.name == 'foo'
        header.value == 'bar'
    }

    def "SimpleHeader does not support header elements"() {
        when:
        headers.first().elements

        then:
        thrown(UnsupportedOperationException)
    }
}

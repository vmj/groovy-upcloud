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

import spock.lang.Specification
import spock.lang.Unroll

class RequestSpec extends Specification {
    @Unroll
    def "ToString: #expected"() {
        given:
        def request = new Request(null, method, resource, null)

        expect:
        "$request" == expected

        where:
        method | resource  | expected
        'GET'  | '/server' | 'GET /server'
        ''     | ''        | ' '
        null   | null      | 'null null'
    }

    def "ToString: null"() {
        given:
        Request request = null

        expect:
        "$request" == 'null'
    }
}

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

import spock.lang.Specification
import spock.lang.Unroll

class METASpec extends Specification {
    @Unroll
    def "ToString: #expected"() {
        given:
        def request = new Request(null, method, resource, null)

        and:
        def meta = new META(status, message, null, request)

        expect:
        "$meta" == expected

        where:
        method | resource  | status | message | expected
        'GET'  | '/server' | 200    | 'OK'    | '200 OK (GET /server)'
        ''     | ''        | 500    | ''      | '500  ( )'
        null   | null      | 0      | null    | '0 null (null null)'
    }

    def "ToString: null request"() {
        expect:
        new META(200).toString() == '200 null (null)'
    }
}

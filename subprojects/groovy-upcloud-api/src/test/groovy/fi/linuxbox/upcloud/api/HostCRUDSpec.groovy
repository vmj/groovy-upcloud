/*
 * Groovy UpCloud library - API Module
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
package fi.linuxbox.upcloud.api


import fi.linuxbox.upcloud.api.spec.ApiSpecification

import static fi.linuxbox.upcloud.builder.ResourceBuilder.build

class HostCRUDSpec extends ApiSpecification {

    def "api can be created from map"() {
        when:
        def api = [HTTP: session, id: 123] as HostApi

        then:
        noExceptionThrown()
        api instanceof HostApi
    }

    // build minimal class that works for the HostApi trait: HTTP and id
    def host = build 'Host', HTTP: session, { id = 123L } withTraits HostApi

    def "load: GET /host/123"() {
        when:
        host.load {}

        then:
        requestIs 'GET', '/host/123'
    }

    def "update: PATCH /host/123"() {
        given:
            def changes = build 'Host', {
                description = "My New Host"
            }

        when:
            host.update changes, {}

        then:
            requestIs 'PATCH', '/host/123',
                    [ "host": [
                            "description": "My New Host"
                        ]
                    ]
    }
}

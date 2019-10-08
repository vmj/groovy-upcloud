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

import fi.linuxbox.upcloud.api.spec.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class ServerCRUDSpec extends ServerApiSpecification {

    def "api can be created from map"() {
        when:
        def api = [HTTP: session, uuid: 'fake-uuid'] as ServerApi

        then:
        noExceptionThrown()
        api instanceof ServerApi
    }

    def "load: GET /server/fake-uuid"() {
        when:
            server.load {}

        then:
            requestIs 'GET', '/server/fake-uuid'
    }

    def "update: PUT /server/fake-uuid"() {
        given:
            def changes = build 'Server', {
                coreNumber = "8"
                memoryAmount = "16384"
                plan = "custom"
            }

        when:
            server.update changes, {}

        then:
            requestIs 'PUT', '/server/fake-uuid',
                    [ "server": [
                            "core_number": "8",
                            "memory_amount": "16384",
                            "plan": "custom"
                        ]
                    ]
    }

    def "delete: DELETE /server/fake-uuid"() {
        when:
            server.delete {}

        then:
            requestIs 'DELETE', '/server/fake-uuid'
    }
}

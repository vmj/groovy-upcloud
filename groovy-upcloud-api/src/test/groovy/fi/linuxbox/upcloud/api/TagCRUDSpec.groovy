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

class TagCRUDSpec extends ApiSpecification {

    def "api can be created from map"() {
        when:
        def api = [HTTP: session, name: 'DEV'] as TagApi

        then:
        noExceptionThrown()
        api instanceof TagApi
    }

    // build minimal class that works for the TagApi trait: HTTP and name
    def tag = build 'Tag', HTTP: session, { name = 'DEV' } withTraits TagApi

    def "update: PUT /tag/DEV"() {
        given:
            def changes = build 'Tag', {
                name = "PROD"
                description = "Production servers"
                servers = [
                        "fake-uuid"
                ]
            }

        when:
            tag.update changes, {}

        then:
            requestIs 'PUT', '/tag/DEV',
                    [ "tag": [
                            "name": "PROD",
                            "description": "Production servers",
                            "servers": [
                                    "server": [
                                            "fake-uuid"
                                    ]
                            ]
                        ]
                    ]
    }

    def "delete: DELETE /tag/DEV"() {
        when:
            tag.delete {}

        then:
            requestIs 'DELETE', '/tag/DEV'
    }
}

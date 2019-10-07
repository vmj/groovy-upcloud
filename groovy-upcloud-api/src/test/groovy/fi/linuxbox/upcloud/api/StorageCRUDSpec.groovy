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

class StorageCRUDSpec extends StorageApiSpecification {

    def "api can be created from map"() {
        when:
        def api = [HTTP: session, uuid: 'fake-uuid'] as StorageApi

        then:
        noExceptionThrown()
        api instanceof StorageApi
    }

    def "load: GET /storage/fake-uuid"() {
        when:
            storage.load {}

        then:
            requestIs 'GET', '/storage/fake-uuid'
    }

    def "update: PUT /storage/fake-uuid"() {
        given:
            def changes = build 'Storage', {
                size = "20"
                title = "A larger storage"
            }

        when:
            storage.update changes, {}

        then:
            requestIs 'PUT', '/storage/fake-uuid',
                    [ "storage": [
                            "size": "20",
                            "title": "A larger storage"
                        ]
                    ]
    }

    def "delete: DELETE /storage/fake-uuid"() {
        when:
            storage.delete {}

        then:
            requestIs 'DELETE', '/storage/fake-uuid'
    }
}

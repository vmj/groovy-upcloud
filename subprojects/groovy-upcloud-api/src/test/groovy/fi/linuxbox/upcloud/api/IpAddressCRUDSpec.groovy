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

class IpAddressCRUDSpec extends ApiSpecification {

    def "api can be created from map"() {
        when:
        def api = [HTTP: session, address: '0.0.0.0'] as IpAddressApi

        then:
        noExceptionThrown()
        api instanceof IpAddressApi
    }

    // build minimal class that works for the IpAddressApi trait: HTTP and address
    def ipAddress = build 'IpAddress', HTTP: session, { address = '0.0.0.0' } withTraits IpAddressApi

    def "load: GET /ip_address/0.0.0.0"() {
        when:
            ipAddress.load {}

        then:
            requestIs 'GET', '/ip_address/0.0.0.0'
    }

    def "update: PUT /ip_address/0.0.0.0"() {
        given:
            def changes = build 'IpAddress', {
                ptrRecord = "hostname.example.com"
            }

        when:
            ipAddress.update changes, {}

        then:
            requestIs 'PUT', '/ip_address/0.0.0.0',
                    [ "ip_address": [
                            "ptr_record": "hostname.example.com"
                        ]
                    ]
    }

    def "delete: DELETE /ip_address/0.0.0.0"() {
        when:
            ipAddress.delete {}

        then:
            requestIs 'DELETE', '/ip_address/0.0.0.0'
    }
}

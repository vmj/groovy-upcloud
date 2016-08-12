package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class IpAddressCRUDSpec extends ApiSpecification {

    // build minimal class that works for the IpAddress trait: API and address
    def ipAddress = build 'IpAddress', API: api, { address = '0.0.0.0' } withTraits IpAddress

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

package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.mock.MockIpAddress
import fi.linuxbox.upcloud.api.spec.*
import fi.linuxbox.upcloud.core.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class IpAddressCRUDSpec extends ApiSpecification {

    IpAddress ipAddress = new MockIpAddress(API: new API(http, json, null, null))

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

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
            requestIs 'GET',  '/ip_address/0.0.0.0'
    }

    def "update: PUT /ip_address/0.0.0.0"() {
        given:
            configure ipAddress, {
                ptrRecord = "hostname.example.com"
            }

        when:
            ipAddress.update {}

        then:
            requestIs 'PUT', '/ip_address/0.0.0.0',
                    [ "mock_ip_address": [
                            "address": "0.0.0.0",
                            "ptr_record": "hostname.example.com"
                    ] ]
    }

    def "delete: DELETE /ip_address/0.0.0.0"() {
        when:
            ipAddress.delete {}

        then:
            requestIs 'DELETE', '/ip_address/0.0.0.0'
    }
}

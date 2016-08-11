package fi.linuxbox.upcloud.api

import spock.lang.*

import fi.linuxbox.upcloud.core.*
import fi.linuxbox.upcloud.core.http.*
import fi.linuxbox.upcloud.core.json.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class IpAddressCRUDSpec extends Specification {

    HTTP http = Mock()
    JSON json = Mock()
    IpAddress ipAddress
    Exchange req

    def setup() {
        ipAddress = new MockIpAddress(API: new API(http, json, null, null))
        1 * http.execute(_) >> { req = it[0] }
        json.encode(_) >> { new MockInputStream(it[0]) } // at most once
    }

    void requestIs(def method, def resource, def repr = null) {
        assert req?.method == method
        assert req.resource.endsWith(resource)
        assert req.body?.repr == repr
    }

    def "create: POST /ip_address/"() {
        given:
            configure ipAddress, {
                address = null
                family = "IPv4"
                server = "fake-uuid"
            }

        when:
            ipAddress.create {}

        then:
            requestIs 'POST', '/ip_address/',
                    [ "mock_ip_address": [
                            "family": "IPv4",
                            "server": "fake-uuid"
                    ] ]
    }

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
                            "address": "0.0.0.0",  // FIXME: update issue: ip address required for URL, but should not be in POSTed repr
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

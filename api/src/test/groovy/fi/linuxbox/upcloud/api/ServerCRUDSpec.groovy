package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class ServerCRUDSpec extends ServerSpecification {

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

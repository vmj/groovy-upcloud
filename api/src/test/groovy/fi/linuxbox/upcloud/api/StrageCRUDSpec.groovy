package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class StrageCRUDSpec extends StorageSpecification {

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

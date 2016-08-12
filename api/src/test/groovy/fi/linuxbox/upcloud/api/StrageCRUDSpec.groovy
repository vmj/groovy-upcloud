package fi.linuxbox.upcloud.api

class StrageCRUDSpec extends StorageSpecification {

    def "create: POST /storage/"() {
        when:
            storage.create {}

        then:
            requestIs 'POST', '/storage/',
                    [ "mock_storage": [ "uuid": "fake-uuid" ] ]
    }

    def "load: GET /storage/fake-uuid"() {
        when:
            storage.load {}

        then:
            requestIs 'GET',  '/storage/fake-uuid'
    }

    def "update: PUT /storage/fake-uuid"() {
        when:
            storage.update {}

        then:
            requestIs 'PUT', '/storage/fake-uuid',
                    [ "mock_storage": [ "uuid": "fake-uuid" ] ]
    }

    def "delete: DELETE /storage/fake-uuid"() {
        when:
            storage.delete {}

        then:
            requestIs 'DELETE', '/storage/fake-uuid'
    }
}

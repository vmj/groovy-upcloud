package fi.linuxbox.upcloud.api

class ServerCRUDSpec extends ServerSpecification {

    def "create: POST /server/"() {
        when:
            server.create {}

        then:
            requestIs 'POST', '/server/',
                    [ "mock_server": [ "uuid": "fake-uuid" ] ]
    }

    def "load: GET /server/fake-uuid"() {
        when:
            server.load {}

        then:
            requestIs 'GET',  '/server/fake-uuid'
    }

    def "update: PUT /server/fake-uuid"() {
        when:
            server.update {}

        then:
            requestIs 'PUT', '/server/fake-uuid',
                    [ "mock_server": [ "uuid": "fake-uuid" ] ]
    }

    def "delete: DELETE /server/fake-uuid"() {
        when:
            server.delete {}

        then:
            requestIs 'DELETE', '/server/fake-uuid'
    }
}

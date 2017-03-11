package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.ServerSpecification

class ServerCommandSpec extends ServerSpecification {

    def "start: POST /server/fake-uuid/start"() {
        when:
            server.start {}

        then:
            requestIs 'POST',  '/server/fake-uuid/start'
    }

    def "stop: POST /server/fake-uuid/stop (server defaults, i.e. soft stop)"() {
        when:
            server.stop {}

        then:
            requestIs 'POST', '/server/fake-uuid/stop',
                    [ "stop_server": [ : ] ]
    }

    def "stop: POST /server/fake-uuid/stop (hard stop)"() {
        when:
            server.stop stop_type: "hard", {}

        then:
            requestIs 'POST', '/server/fake-uuid/stop',
                    [ "stop_server": [ stop_type: 'hard' ] ]
    }

    def "stop: POST /server/fake-uuid/stop (soft stop with timeout)"() {
        when:
            server.stop timeout: 600, {}

        then:
            requestIs 'POST', '/server/fake-uuid/stop',
                    [ "stop_server": [ timeout: 600 ] ]
    }

    def "restart: POST /server/fake-uuid/restart (soft)"() {
        when:
            server.restart {}

        then:
            requestIs 'POST', '/server/fake-uuid/restart',
                    [ "restart_server" : [ : ] ]
    }

    def "restart: POST /server/fake-uuid/restart (hard)"() {
        when:
            server.restart stop_type: "hard",
                    timeout: 60,
                    timeout_action: "destroy", {}

        then:
            requestIs 'POST', '/server/fake-uuid/restart',
                    [ "restart_server" : [
                            "stop_type": "hard",
                            "timeout": 60,
                            "timeout_action": "destroy"
                    ] ]
    }
}

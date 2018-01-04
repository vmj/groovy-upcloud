/*
 * Groovy UpCloud library - API Module
 * Copyright (C) 2018  <mikko@varri.fi>
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

import fi.linuxbox.upcloud.api.spec.ServerSpecification

class ServerCommandSpec extends ServerSpecification {

    def "cancel: POST /server/fake-uuid/cancel"() {
        when:
        server.cancel {}

        then:
        requestIs 'POST',  '/server/fake-uuid/cancel'
    }

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

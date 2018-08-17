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


import fi.linuxbox.upcloud.api.spec.ServerApiSpecification

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class ServerFirewallSpec extends ServerApiSpecification {

    def "filewallRules: GET /server/fake-uuid/firewall_rule"() {
        when:
            server.firewallRules {}

        then:
            requestIs 'GET', '/server/fake-uuid/firewall_rule'
    }

    def "createFirewallRule: POST /server/fake-uuid/firewall_rule"() {
        given:
            def firewallRule = build 'firewallRule', {
                family = "IPv4"
                protocol = "tcp"
                destination_port_start = "22"
                destination_port_end = "22"
                direction = "in"
                action = "accept"
            }

        when:
            server.createFirewallRule firewallRule, {}

        then:
            requestIs 'POST', '/server/fake-uuid/firewall_rule',
                    [ "firewall_rule": [
                            "family": "IPv4",
                            "protocol": "tcp",
                            "destination_port_start": "22",
                            "destination_port_end": "22",
                            "direction": "in",
                            "action": "accept"
                    ] ]
    }

    def "loadFirewallRule: GET /server/fake-uuid/firewall_rule/1"() {
        when:
            server.loadFirewallRule 1, {}

        then:
            requestIs 'GET', '/server/fake-uuid/firewall_rule/1'
    }


    def "deleteFirewallRule: DELETE /server/fake-uuid/firewall_rule/1"() {
        when:
            server.deleteFirewallRule 1, {}

        then:
            requestIs 'DELETE', '/server/fake-uuid/firewall_rule/1'
    }
}

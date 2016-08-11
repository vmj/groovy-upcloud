package fi.linuxbox.upcloud.api

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class ServerFirewallSpec extends ServerSpecification {

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

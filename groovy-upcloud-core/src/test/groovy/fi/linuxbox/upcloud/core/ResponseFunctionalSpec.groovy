package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.json.gjson.GJSON
import fi.linuxbox.upcloud.json.spi.JSON
import fi.linuxbox.upcloud.resource.Firewall
import fi.linuxbox.upcloud.resource.ServerPlan_2xCPU_2GB
import fi.linuxbox.upcloud.resource.Zone
import spock.lang.Specification

class ResponseFunctionalSpec extends Specification {

    JSON parser

    def setup() {
        parser = new GJSON()
    }

    Map<String, ?> parse(final String json) {
        parser.decode(new ByteArrayInputStream(json.getBytes("UTF-8")))
    }

    Resource load(final String json) {
        final Map r = parse(json)
        new Resource(repr: r)
    }

    def "account JSON to Account"() {
        when:
        def resp = load("""
            {
                "account": {
                    "credits": "10000",
                    "username": "username"
                }
            }
            """)

        then:
        resp?.account.class.simpleName == 'Account'
    }

    def "prices JSON to Zone list"() {
        when:
        def resp = load("""
            {
              "prices": {
                "zone": [
                  {
                    "name": "fi-hel1",
                    "firewall": {
                      "amount": 1,
                      "price": 0.5
                    },
                    "io_request_backup": {
                      "amount": 1000000,
                      "price": 10
                    },
                    "io_request_hdd": {
                      "amount": 1000000,
                      "price": 0
                    },
                    "io_request_maxiops": {
                      "amount": 1000000,
                      "price": 0
                    },
                    "ipv4_address": {
                      "amount": 1,
                      "price": 0.3
                    },
                    "ipv6_address": {
                      "amount": 1,
                      "price": 0
                    },
                    "public_ipv4_bandwidth_in": {
                      "amount": 1,
                      "price": 0
                    },
                    "public_ipv4_bandwidth_out": {
                      "amount": 1,
                      "price": 5
                    },
                    "public_ipv6_bandwidth_in": {
                      "amount": 1,
                      "price": 0
                    },
                    "public_ipv6_bandwidth_out": {
                      "amount": 1,
                      "price": 5
                    },
                    "server_core": {
                      "amount": 1,
                      "price": 1.3
                    },
                    "server_memory": {
                      "amount": 256,
                      "price": 0.45
                    },
                    "storage_backup": {
                      "amount": 1,
                      "price": 0.007
                    },
                    "storage_hdd": {
                      "amount": 1,
                      "price": 0.013
                    },
                    "storage_maxiops": {
                      "amount": 1,
                      "price": 0.028
                    },
                    "server_plan_1xCPU-1GB": {
                      "amount": 1,
                      "price": 1.488
                    },
                    "server_plan_2xCPU-2GB": {
                      "amount": 1,
                      "price": 2.976
                    }
                  }
                ]
              }
            }
            """)

        then:
        resp?.prices instanceof List
        resp.prices.every { it.class.simpleName == 'Zone' }
        Zone zone = resp.prices[0]
        zone.name == 'fi-hel1'
        zone.firewall instanceof Firewall
        zone.firewall.amount == 1
        zone.firewall.price == 0.5
        zone.serverPlan_2xCPU_2GB instanceof ServerPlan_2xCPU_2GB
        zone.serverPlan_2xCPU_2GB.price.equals(2.976)
    }

    def "zones JSON to Zone list"() {
        when:
        def resp = load("""
            {
              "zones": {
                "zone": [
                  {
                    "id": "fi-hel1",
                    "description": "Helsinki, Finland, zone 1"
                  },
                  {
                    "id": "uk-lon1",
                    "description": "London, United Kingdom, zone 1"
                  },
                  {
                    "id" : "de-fra1",
                    "description" : "Frankfurt, Germany, zone 1"
                  },
                  {
                    "id" : "us-chi1",
                    "description" : "Chicago, United States, zone 1"      
                  }
                ]
              }
            }
            """)

        then:
        resp?.zones instanceof List
        resp.zones.every { it.class.simpleName == 'Zone' }
        resp.zones[0].id == 'fi-hel1'
        resp.zones[3].id == 'us-chi1'
    }

}

package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.json.gjson.GJSON
import fi.linuxbox.upcloud.json.spi.JSON
import fi.linuxbox.upcloud.resource.Firewall
import fi.linuxbox.upcloud.resource.FirewallRule
import fi.linuxbox.upcloud.resource.IpAddress
import fi.linuxbox.upcloud.resource.Plan
import fi.linuxbox.upcloud.resource.Server
import fi.linuxbox.upcloud.resource.ServerPlan_2xCPU_2GB
import fi.linuxbox.upcloud.resource.ServerSize
import fi.linuxbox.upcloud.resource.Storage
import fi.linuxbox.upcloud.resource.StorageDevice
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

    def "timeszones JSON to string list"() {
        when:
        def resp = load("""
            {
              "timezones": {
                "timezone": [
                  "Africa/Abidjan",
                  "Africa/Accra",
                  "Africa/Addis_Ababa",
                  "Pacific/Truk",
                  "Pacific/Wake",
                  "Pacific/Wallis",
                  "UTC"
                ]
              }
            }
            """)

        then:
        resp?.timezones instanceof List
        resp.timezones.every { it instanceof String }
        resp.timezones[0] == 'Africa/Abidjan'
        resp.timezones[6] == 'UTC'
    }

    def "plans JSON to Plan list"() {
        when:
        def resp = load("""
            {
              "plans" : {
                "plan" : [
                  {
                    "core_number" : 1,
                    "memory_amount" : 1024,
                    "name" : "1xCPU-1GB",
                    "public_traffic_out" : 2048,
                    "storage_size" : 30,
                    "storage_tier" : "maxiops"
                  },
                  {
                    "core_number" : 2,
                    "memory_amount" : 2048,
                    "name" : "2xCPU-2GB",
                    "public_traffic_out" : 3072,
                    "storage_size" : 50,
                    "storage_tier" : "maxiops"
                  }
                ]
              }
            }
            """)

        then:
        resp?.plans instanceof List
        resp.plans.every { it instanceof Plan }
        resp.plans[0].name == '1xCPU-1GB'
        resp.plans[1].name == '2xCPU-2GB'
    }

    def "serverSizes JSON to ServerSize list"() {
        when:
        def resp = load("""
            {
              "server_sizes": {
                "server_size": [
                  {
                    "core_number": "1",
                    "memory_amount": "512"
                  },
                  {
                    "core_number": "1",
                    "memory_amount": "768"
                  },
                  {
                    "core_number": "10",
                    "memory_amount": "65024"
                  },
                  {
                    "core_number": "10",
                    "memory_amount": "65536"
                  }
                ]
              }
            }
            """)

        then:
        resp?.serverSizes instanceof List
        resp.serverSizes.every { it instanceof ServerSize }
        resp.serverSizes[0].coreNumber == '1'
        resp.serverSizes[3].coreNumber == '10'
    }

    def "servers JSON to Server list"() {
        when:
        def resp = load("""
            {
              "servers": {
                "server": [
                  {
                    "core_number": "1",
                    "hostname": "fi.example.com",
                    "licence" : 0,
                    "memory_amount": "1024",
                    "plan" : "1xCPU-1GB",
                    "state": "started",
                    "tags" : {
                      "tag" : [
                        "PROD",
                        "CentOS"
                      ]
                    },
                    "title": "Helsinki server",
                    "uuid": "00798b85-efdc-41ca-8021-f6ef457b8531",
                    "zone": "fi-hel1"
                  },
                  {
                    "core_number": "1",
                    "hostname": "uk.example.com",
                    "licence" : 0,
                    "memory_amount": "512",
                    "plan" : "custom",
                    "state": "stopped",
                    "tags" : {
                      "tag" : [
                        "DEV",
                        "Ubuntu"
                      ]
                    },
                    "title": "London server",
                    "uuid": "009d64ef-31d1-4684-a26b-c86c955cbf46",
                    "zone": "uk-lon1"
                  }
                ]
              }
            }
            """)

        then:
        resp?.servers instanceof List
        resp.servers.every { it instanceof Server }
        resp.servers[0].coreNumber == '1'
        resp.servers[1].tags[1] == 'Ubuntu'
    }

    def "server JSON to Server"() {
        when:
        def resp = load("""
            {
              "server": {
                "boot_order": "disk",
                "core_number": "0",
                "firewall": "on",
                "host" : 7653311107,
                "hostname": "server1.example.com",
                "ip_addresses": {
                  "ip_address": [
                    {
                      "access": "private",
                      "address": "10.0.0.00",
                      "family" : "IPv4"
                    },
                    {
                      "access": "public",
                      "address": "0.0.0.0",
                      "family" : "IPv4"
                    },
                    {
                      "access": "public",
                      "address": "xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx",
                      "family" : "IPv6"
                    }
                  ]
                },
                "license": 0,
                "memory_amount": "1024",
                "nic_model": "virtio",
                "plan" : "1xCPU-1GB",
                "state": "started",
                "storage_devices": {
                  "storage_device": [
                    {
                      "address": "virtio:0",
                      "part_of_plan" : "yes",
                      "storage": "012580a1-32a1-466e-a323-689ca16f2d43",
                      "storage_size": 20,
                      "storage_title": "Storage for server1.example.com",
                      "type": "disk"
                    }
                  ]
                },
                "tags" : {
                   "tag" : [
                      "DEV",
                      "Ubuntu"
                   ]
                },
                "timezone": "UTC",
                "title": "server1.example.com",
                "uuid": "0077fa3d-32db-4b09-9f5f-30d9e9afb565",
                "video_model": "cirrus",
                "vnc" : "on",
                "vnc_host" : "fi-he1l.vnc.upcloud.com",
                "vnc_password": "aabbccdd",
                "vnc_port": "00000",
                "zone": "fi-hel1"
              }
            }
            """)

        then:
        resp?.server instanceof Server
        resp.server.host == 7653311107
        resp.server.ipAddresses instanceof List
        resp.server.ipAddresses.every { it instanceof IpAddress }
        resp.server.ipAddresses[2].family == 'IPv6'
        resp.server.storageDevices instanceof List
        resp.server.storageDevices.every { it instanceof StorageDevice }
        resp.server.storageDevices[0].storageSize == 20
        resp.server.tags instanceof List
        resp.server.tags.every { it instanceof String }
        resp.server.tags[1] == 'Ubuntu'
    }

    def "storages JSON to Storage list"() {
        when:
        def resp = load("""
            {
              "storages": {
                "storage": [
                  {
                    "access": "private",
                    "license": 0,
                    "size": 10,
                    "state": "online",
                    "tier": "hdd",
                    "title": "Operating system disk",
                    "type": "normal",
                    "uuid": "01eff7ad-168e-413e-83b0-054f6a28fa23",
                    "zone": "uk-lon1",
                  },
                  {
                    "access": "private",
                    "license": 0,
                    "part_of_plan": "yes",
                    "size": 50,
                    "state": "online",
                    "tier": "maxiops",
                    "title": "Databases",
                    "type": "normal",
                    "uuid": "01f3286c-a5ea-4670-8121-d0b9767d625b",
                    "zone": "fi-hel1"
                  }
                ]
              }
            }
            """)

        then:
        resp?.storages instanceof List
        resp.storages.every { it instanceof Storage }
        resp.storages[0].size == 10
        resp.storages[1].tier == 'maxiops'
    }

    def "storage JSON to Storage"() {
        when:
        def resp = load("""
            {
              "storage": {
                "access": "private",
                "backup_rule": {
                  "interval": "daily",
                  "time": "0430",
                  "retention": "365"
                },
                "backups": {
                  "backup": []
                },
                "license": 0,
                "servers": {
                  "server": [
                    "00798b85-efdc-41ca-8021-f6ef457b8531"
                  ]
                },
                "size": 10,
                "state": "online",
                "tier": "maxiops",
                "title": "Operating system disk",
                "type": "normal",
                "uuid": "01d4fcd4-e446-433b-8a9c-551a1284952e",
                "zone": "fi-hel1"
              }
            }
            """)

        then:
        resp?.storage instanceof Storage
        resp.storage.access == "private"
        resp.storage.backupRule.time == '0430'
        resp.storage.backups instanceof List
        resp.storage.backups.isEmpty()
        resp.storage.servers instanceof List
        resp.storage.servers.every { it instanceof String }
        resp.storage.servers[0] == '00798b85-efdc-41ca-8021-f6ef457b8531'
    }

    def "ipAddresses JSON to IpAddress list"() {
        when:
        def resp = load("""
            {
              "ip_addresses": {
                "ip_address": [
                  {
                    "access": "private",
                    "address": "10.0.0.0",
                    "family": "IPv4",
                    "ptr_record": "",
                    "server": "0053cd80-5945-4105-9081-11192806a8f7"
                  },
                  {
                    "access": "private",
                    "address": "10.0.0.1",
                    "family": "IPv4",
                    "ptr_record": "",
                    "server": "006b6701-55d2-4374-ac40-56cc1501037f"
                  },
                  {
                    "access": "public",
                    "address": "x.x.x.x",
                    "family": "IPv4",
                    "part_of_plan": "yes",
                    "ptr_record": "x.x.x.x.zone.host.upcloud.com",
                    "server": "0053cd80-5945-4105-9081-11192806a8f7"
                  },
                  {
                    "access": "public",
                    "address": "xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx",
                    "family": "IPv6",
                    "ptr_record": "xxxx-xxxx-xxxx-xxxx.v6.zone.host.upcloud.com",
                    "server": "006b6701-55d2-4374-ac40-56cc1501037f"
                  }
                ]
              }
            }
            """)

        then:
        resp?.ipAddresses instanceof List
        resp.ipAddresses.every { it instanceof IpAddress }
        resp.ipAddresses[0].address == "10.0.0.0"
        resp.ipAddresses[2].ptrRecord == "x.x.x.x.zone.host.upcloud.com"
        resp.ipAddresses[3].family == "IPv6"
    }

    def "ipAddress JSON to IpAddress"() {
        when:
        def resp = load("""
            {
              "ip_address": {
                "access": "public",
                "address": "0.0.0.0",
                "family": "IPv4",
                "part_of_plan": "yes",
                "ptr_record": "test.example.com",
                "server": "009d64ef-31d1-4684-a26b-c86c955cbf46",
              }
            }
            """)

        then:
        resp?.ipAddress instanceof IpAddress
        resp.ipAddress.server == "009d64ef-31d1-4684-a26b-c86c955cbf46"
    }

    def "firewallRules JSON to FirewallRule list"() {
        when:
        def resp = load("""
            {
              "firewall_rules": {
                "firewall_rule": [
                  {
                    "action": "accept",
                    "comment": "Alow HTTP from anywhere",
                    "destination_address_end": "",
                    "destination_address_start": "",
                    "destination_port_end": "80",
                    "destination_port_start": "80",
                    "direction": "in",
                    "family": "IPv4",
                    "icmp_type": "",
                    "position": "1",
                    "protocol": "",
                    "source_address_end": "",
                    "source_address_start": "",
                    "source_port_end": "",
                    "source_port_start": ""
                  },
                  {
                    "action": "accept",
                    "comment": "Allow SSH from a specific network only",
                    "destination_address_end": "",
                    "destination_address_start": "",
                    "destination_port_end": "22",
                    "destination_port_start": "22",
                    "direction": "in",
                    "family": "IPv4",
                    "icmp_type": "",
                    "position": "2",
                    "protocol": "tcp",
                    "source_address_end": "192.168.1.255",
                    "source_address_start": "192.168.1.1",
                    "source_port_end": "",
                    "source_port_start": ""
                  },
                  {
                    "action": "accept",
                    "comment": "Allow SSH over IPv6 from this range",
                    "destination_address_end": "",
                    "destination_address_start": "",
                    "destination_port_end": "22",
                    "destination_port_start": "22",
                    "direction": "in",
                    "family": "IPv6",
                    "icmp_type": "",
                    "position": "3",
                    "protocol": "tcp",
                    "source_address_end": "2a04:3540:1000:aaaa:bbbb:cccc:d001",
                    "source_address_start": "2a04:3540:1000:aaaa:bbbb:cccc:d001",
                    "source_port_end": "",
                    "source_port_start": ""
                  },
                  {
                    "action": "accept",
                    "comment": "Allow ICMP echo request (ping)",
                    "destination_address_end": "",
                    "destination_address_start": "",
                    "destination_port_end": "",
                    "destination_port_start": "",
                    "direction": "in",
                    "family": "IPv4",
                    "icmp_type": "8",
                    "position": "4",
                    "protocol": "icmp",
                    "source_address_end": "",
                    "source_address_start": "",
                    "source_port_end": "",
                    "source_port_start": "",
                  },
                  {
                    "action": "drop",
                    "comment": "",
                    "destination_address_end": "",
                    "destination_address_start": "",
                    "destination_port_end": "",
                    "destination_port_start": "",
                    "direction": "in",
                    "family": "",
                    "icmp_type": "",
                    "position": "5",
                    "protocol": "",
                    "source_address_end": "",
                    "source_address_start": "",
                    "source_port_end": "",
                    "source_port_start": ""
                  }
                ]
              }
            }
            """)

        then:
        resp?.firewallRules instanceof List
        resp.firewallRules.every { it instanceof FirewallRule }
        resp.firewallRules[0].comment == 'Alow HTTP from anywhere'
        resp.firewallRules[4].position == '5'
    }

    def "firewallRule JSON to FirewallRule"() {
        when:
        def resp = load("""
            {
              "firewall_rule": {
                "action": "accept",
                "comment": "Allow HTTP from anywhere",
                "destination_address_end": "",
                "destination_address_start": "",
                "destination_port_end": "80",
                "destination_port_start": "80",
                "direction": "in",
                "family": "IPv4",
                "icmp_type": "",
                "position": "1",
                "protocol": "",
                "source_address_end": "",
                "source_address_start": "",
                "source_port_end": "",
                "source_port_start": ""
              }
            }
            """)

        then:
        resp?.firewallRule instanceof FirewallRule
        resp.firewallRule.position == '1'
    }
}

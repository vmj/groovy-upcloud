package fi.linuxbox.upcloud.api

import spock.lang.*

import fi.linuxbox.upcloud.api.spec.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class UpCloudSpec extends ApiSpecification {

    UpCloud upCloud = new UpCloud(session)

    def "storages type: 'favorite' does GET .../storage/favorite"() {
        when:
            upCloud.storages type: 'favorite', {}

        then:
            requestIs 'GET', '/storage/favorite'
    }

    @Unroll
    def "#methodName does GET ...#resource"() {
        when:
            upCloud."$methodName" {}

        then:
            requestIs 'GET', resource

        where:
            methodName    | resource
            'account'     | '/account'
            'prices'      | '/price'
            'zones'       | '/zone'
            'timezones'   | '/timezone'
            'plans'       | '/plan'
            'serverSizes' | '/server_size'
            'servers'     | '/server'
            'ipAddresses' | '/ip_address'
            'storages'    | '/storage'
            'tags'        | '/tag'
    }

    def "creating an IP address"() {
        given:
            def ipAddress = build 'IpAddress', {
                family = 'IPv4'
                server = 'fake-uuid'
            }

        when:
            upCloud.create ipAddress, {}

        then:
            requestIs 'POST', '/ip_address',
                    [ 'ip_address': [
                            "family": "IPv4",
                            "server": "fake-uuid"
                        ]
                    ]
    }

    def "creating a server (from template)"() {
        given:
            def server = build 'Server', {
                zone = "fi-hel1"
                title = "My Debian server"
                hostname = "debian.example.com"
                plan = "2xCPU-2GB"
                storageDevices = [
                        build('StorageDevice') {
                                action = "clone"
                                storage = "01000000-0000-4000-8000-000020030100"
                                title = "Debian from a template"
                                size = 50
                                tier = "maxiops"
                        }
                ]
                loginUser = build 'LoginUser', {
                    username = "upclouduser"
                    sshKeys = [
                            "ssh-rsa AAAAB3NzaC1yc2EAA[...]ptshi44x user@some.host",
                            "ssh-dss AAAAB3NzaC1kc3MAA[...]VHRzAA== someuser@some.other.host"
                    ]
                }
            }

        when:
            upCloud.create server, {}

        then:
            requestIs 'POST', '/server',
                    [
                            "server": [
                                    "zone": "fi-hel1",
                                    "title": "My Debian server",
                                    "hostname": "debian.example.com",
                                    "plan": "2xCPU-2GB",
                                    "storage_devices": [
                                            "storage_device": [
                                                    [
                                                            "action": "clone",
                                                            "storage": "01000000-0000-4000-8000-000020030100",
                                                            "title": "Debian from a template",
                                                            "size": 50,
                                                            "tier": "maxiops"
                                                    ]
                                            ]
                                    ],
                                    "login_user": [
                                            "username": "upclouduser",
                                            "ssh_keys": [
                                                    "ssh_key": [
                                                            "ssh-rsa AAAAB3NzaC1yc2EAA[...]ptshi44x user@some.host",
                                                            "ssh-dss AAAAB3NzaC1kc3MAA[...]VHRzAA== someuser@some.other.host"
                                                    ]
                                            ]
                                    ]
                            ]
                    ]
    }

    def "creating a storage"() {
        given:
            def storage = build 'Storage', {
                size = "10"
                tier = "maxiops"
                title = "My data collection"
                zone = "fi-hel1"
                backupRule = build 'BackupRule', {
                    interval = "daily"
                    time = "0430"
                    retention = "365"
                }
            }

        when:
            upCloud.create storage, {}

        then:
            requestIs 'POST', '/storage',
                    [
                            "storage": [
                                    "size": "10",
                                    "tier": "maxiops",
                                    "title": "My data collection",
                                    "zone": "fi-hel1",
                                    "backup_rule": [
                                            "interval": "daily",
                                            "time": "0430",
                                            "retention": "365"
                                    ]
                            ]
                    ]
    }

    def "creating a tag"() {
        given:
            def tag = build 'Tag', {
                name = "DEV"
                description = "Development servers"
                servers = [
                        "0077fa3d-32db-4b09-9f5f-30d9e9afb565"
                ]
            }

        when:
            upCloud.create tag, {}

        then:
            requestIs 'POST', '/tag',
                    [
                            "tag": [
                                    "name": "DEV",
                                    "description": "Development servers",
                                    "servers": [
                                            "server": [
                                                    "0077fa3d-32db-4b09-9f5f-30d9e9afb565"
                                            ]
                                    ]
                            ]
                    ]
    }

    @Unroll
    def "Name juggling: #className -> #urlPathSegment"() {
        expect:
            UpCloud.url_path_segment(className) == urlPathSegment

        where:
            className   | urlPathSegment
            'IpAddress' | 'ip_address'
            'Server'    | 'server'
            'Storage'   | 'storage'
            'Tag'       | 'tag'
    }
}

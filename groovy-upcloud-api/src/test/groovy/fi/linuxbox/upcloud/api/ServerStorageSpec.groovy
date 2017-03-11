package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.ServerSpecification

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class ServerStorageSpec extends ServerSpecification {

    def "attach: POST /server/fake-uuid/storage/attach"() {
        given:
            def storageDevice = build 'storageDevice', {
                type = "disk"
                address = "virtio:0"
                storage = "storage-uuid"
            }

        when:
            server.attach storageDevice, {}

        then:
            requestIs 'POST', '/server/fake-uuid/storage/attach',
                    [ "storage_device": [
                            "type": "disk",
                            "address": "virtio:0",
                            "storage": "storage-uuid"
                    ] ]
    }

    def "detach: POST /server/fake-uuid/storage/detach"() {
        given:
            def storageDevice = build 'storageDevice', {
                address = "virtio:0"
            }

        when:
            server.detach storageDevice, {}

        then:
            requestIs 'POST', '/server/fake-uuid/storage/detach',
                    [ "storage_device": [
                            "address": "virtio:0",
                    ] ]
    }

    def "insert: POST /server/fake-uuid/cdrom/load"() {
        given:
            def storageDevice = build 'storageDevice', {
                storage = "storage-uuid"
            }

        when:
            server.insert storageDevice, {}

        then:
            requestIs 'POST', '/server/fake-uuid/cdrom/load',
                    [ "storage_device": [
                            "storage": "storage-uuid"
                    ] ]
    }

    def "eject: POST /server/fake-uuid/cdrom/eject"() {
        when:
            server.eject {}

        then:
            requestIs 'POST', '/server/fake-uuid/cdrom/eject'
    }
}

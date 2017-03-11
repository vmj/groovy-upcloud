package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class StorageCommandSpec extends StorageSpecification {

    def "clone: POST /storage/fake-uuid/clone"() {
        given:
            def newStorage = build 'Storage', {
                zone = 'fi-hel1'
                tier = 'maxiops'
                title = 'Clone of operating system disk'
            }

        when:
            storage.clone newStorage, {}

        then:
            requestIs 'POST',  '/storage/fake-uuid/clone',
                    [ "storage": [
                            "zone": "fi-hel1",
                            "tier": "maxiops",
                            "title": "Clone of operating system disk"
                        ]
                    ]
    }

    def "templatize: POST /storage/fake-uuid/templatize"() {
        given:
            def template = build 'Storage', {
                title = "My server template"
            }

        when:
            storage.templatize template, {}

        then:
            requestIs 'POST', '/storage/fake-uuid/templatize',
                    [ "storage": [
                            "title": "My server template"
                        ]
                    ]
    }

    def "backup: POST /storage/fake-uuid/backup"() {
        given:
            def backup = build 'Storage', {
                title = 'Manually created backup'
            }

        when:
            storage.backup backup, {}

        then:
            requestIs 'POST', '/storage/fake-uuid/backup',
                    [ "storage": [
                            "title": "Manually created backup"
                        ]
                    ]
    }

    def "restore: POST /storage/fake-uuid/restore"() {
        when:
            storage.restore {}

        then:
            requestIs 'POST', '/storage/fake-uuid/restore'
    }

    def "favor: POST /storage/fake-uuid/favorite"() {
        when:
            storage.favor {}

        then:
            requestIs 'POST', '/storage/fake-uuid/favorite'
    }

    def "unfavor: DELETE /storage/fake-uuid/favorite"() {
        when:
            storage.unfavor {}

        then:
            requestIs 'DELETE', '/storage/fake-uuid/favorite'
    }
}

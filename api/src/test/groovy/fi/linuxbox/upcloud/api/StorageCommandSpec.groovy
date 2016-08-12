package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.StorageSpecification

class StorageCommandSpec extends StorageSpecification {

    def "clone: POST /storage/fake-uuid/clone"() {
        when:
            storage.clone {}

        then:
            requestIs 'POST',  '/storage/fake-uuid/clone',
                    [ "mock_storage": [ "uuid": "fake-uuid" ] ]
    }

    def "templatize: POST /storage/fake-uuid/templatize"() {
        when:
            storage.templatize {}

        then:
            requestIs 'POST', '/storage/fake-uuid/templatize',
                    [ "mock_storage": [ "uuid": "fake-uuid" ] ]
    }

    def "backup: POST /storage/fake-uuid/backup"() {
        when:
            storage.backup {}

        then:
            requestIs 'POST', '/storage/fake-uuid/backup',
                    [ "mock_storage": [ "uuid": "fake-uuid" ] ]
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

package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.ServerSpecification

class ServerTagSpec extends ServerSpecification {

    def "addTags: POST /server/fake-uuid/tag/DEV,private,RHEL"() {
        when:
            server.addTags(['DEV', 'private', 'RHEL']) {}

        then:
            requestIs 'POST', '/server/fake-uuid/tag/DEV,private,RHEL'
    }

    def "deleteTag: POST /server/fake-uuid/untag/private"() {
        when:
            server.deleteTag 'private', {}

        then:
            requestIs 'POST', '/server/fake-uuid/untag/private'
    }
}

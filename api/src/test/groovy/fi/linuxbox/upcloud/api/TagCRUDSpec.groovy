package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.*
import fi.linuxbox.upcloud.core.*

class TagCRUDSpec extends ApiSpecification {

    Tag tag = new MockTag(API: new API(http, json, null, null))

    def "update: PUT /tag/DEV"() {
        when:
            tag.update {}

        then:
            requestIs 'PUT', '/tag/DEV',
                    [ "mock_tag": [ "name": "DEV" ] ]
    }

    def "delete: DELETE /tag/DEV"() {
        when:
            tag.delete {}

        then:
            requestIs 'DELETE', '/tag/DEV'
    }
}

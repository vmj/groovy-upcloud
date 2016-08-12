package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.mock.*
import fi.linuxbox.upcloud.api.spec.*
import fi.linuxbox.upcloud.core.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class TagCRUDSpec extends ApiSpecification {

    Tag tag = new MockTag(API: new API(http, json, null, null))

    def "update: PUT /tag/DEV"() {
        given:
            def changes = build 'Tag', {
                name = "PROD"
                description = "Production servers"
                servers = [
                        "fake-uuid"
                ]
            }

        when:
            tag.update changes, {}

        then:
            requestIs 'PUT', '/tag/DEV',
                    [ "tag": [
                            "name": "PROD",
                            "description": "Production servers",
                            "servers": [
                                    "server": [
                                            "fake-uuid"
                                    ]
                            ]
                        ]
                    ]
    }

    def "delete: DELETE /tag/DEV"() {
        when:
            tag.delete {}

        then:
            requestIs 'DELETE', '/tag/DEV'
    }
}

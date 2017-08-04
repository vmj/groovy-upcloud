package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

class TagCRUDSpec extends ApiSpecification {

    // build minimal class that works for the Tag trait: SESSION and name
    Tag tag = build 'Tag', SESSION: session, { name = 'DEV' } withTraits Tag

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

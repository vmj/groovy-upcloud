package fi.linuxbox.upcloud.resource

import spock.lang.Specification

import static fi.linuxbox.upcloud.resource.Builder.server

/**
 *
 */
class BuilderSpec extends Specification {

    def "server builder"() {
        given:
            server {
                hostname = "foo"
                coreNumber = 5
            }
    }
}

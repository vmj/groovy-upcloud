package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.http.spi.HTTP
import spock.lang.*

import fi.linuxbox.upcloud.core.*

import static fi.linuxbox.upcloud.resource.Builder.*

/**
 *
 */
class BuilderSpec extends Specification {

    Session session = new Session(Mock(HTTP), null, null, null)

    def "server builder"() {
        when:
            def server = server {
                hostname = "foo"
                coreNumber = 5
            }

        then:
            server?.class.simpleName == 'Server'
            server.hostname == 'foo'
            server.coreNumber == '5'
    }

    def "server without config"() {
        when:
            def server = server()

        then:
            server?.class.simpleName == 'Server'
    }

    def "server with kwargs and config"() {
        when:
            def server = server SESSION: session, {
                hostname = "foo"
            }

        then:
            server?.class.simpleName == 'Server'
            server.hostname == 'foo'
    }

    def "server with kwargs and no config"() {
        when:
            def server = server SESSION: session

        then:
            server?.class.simpleName == 'Server'
    }
}

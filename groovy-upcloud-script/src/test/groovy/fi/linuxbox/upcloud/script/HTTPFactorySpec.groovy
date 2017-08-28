package fi.linuxbox.upcloud.script

import fi.linuxbox.upcloud.http.spi.HTTP
import spock.lang.Specification

class HTTPFactorySpec extends Specification {
    def "In test scope, test HTTP is returned"() {
        when:
        HTTP http = HTTPFactory.create()

        then:
        noExceptionThrown()
        http != null
        http instanceof HTTPImpl
    }
}

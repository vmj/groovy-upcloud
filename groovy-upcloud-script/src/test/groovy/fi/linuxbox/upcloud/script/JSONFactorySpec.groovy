package fi.linuxbox.upcloud.script

import fi.linuxbox.upcloud.json.spi.JSON
import spock.lang.Specification

class JSONFactorySpec extends Specification {
    def "In test scope, test JSON is returned"() {
        when:
        JSON json = JSONFactory.create()

        then:
        noExceptionThrown()
        json != null
        json instanceof JSONImpl
    }
}

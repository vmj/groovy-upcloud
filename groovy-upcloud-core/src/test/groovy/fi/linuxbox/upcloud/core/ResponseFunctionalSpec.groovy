package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.json.gjson.GJSON
import fi.linuxbox.upcloud.json.spi.JSON
import spock.lang.Specification

class ResponseFunctionalSpec extends Specification {

    JSON parser

    def setup() {
        parser = new GJSON()
    }

    Resource parse(final String json) {
        new Resource(repr: parser.decode(new ByteArrayInputStream(json.getBytes("UTF-8"))))
    }

    def "account JSON to Account"() {
        when:
        def resp = parse("""
            {
                "account": {
                    "credits": "10000",
                    "username": "username"
                }
            }
            """)

        then:
        resp?.account.class.simpleName == 'Account'
    }
}

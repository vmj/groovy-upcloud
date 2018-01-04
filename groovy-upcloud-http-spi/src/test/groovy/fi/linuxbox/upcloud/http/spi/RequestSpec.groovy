package fi.linuxbox.upcloud.http.spi

import spock.lang.Specification
import spock.lang.Unroll

class RequestSpec extends Specification {
    @Unroll
    def "ToString: #expected"() {
        given:
        def request = new Request(null, method, resource, null)

        expect:
        "$request" == expected

        where:
        method | resource  | expected
        'GET'  | '/server' | 'GET /server'
        ''     | ''        | ' '
        null   | null      | 'null null'
    }

    def "ToString: null"() {
        given:
        Request request = null

        expect:
        "$request" == 'null'
    }
}

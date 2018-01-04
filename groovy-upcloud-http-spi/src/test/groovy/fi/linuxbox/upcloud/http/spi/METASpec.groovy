package fi.linuxbox.upcloud.http.spi

import spock.lang.Specification
import spock.lang.Unroll

class METASpec extends Specification {
    @Unroll
    def "ToString: #expected"() {
        given:
        def request = new Request(null, method, resource, null)

        and:
        def meta = new META(status, message, null, request)

        expect:
        "$meta" == expected

        where:
        method | resource  | status | message | expected
        'GET'  | '/server' | 200    | 'OK'    | '200 OK (GET /server)'
        ''     | ''        | 500    | ''      | '500  ( )'
        null   | null      | 0      | null    | '0 null (null null)'
    }

    def "ToString: null request"() {
        expect:
        new META(200).toString() == '200 null (null)'
    }
}

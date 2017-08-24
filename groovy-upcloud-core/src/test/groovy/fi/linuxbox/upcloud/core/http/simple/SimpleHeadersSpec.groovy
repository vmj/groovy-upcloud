package fi.linuxbox.upcloud.core.http.simple

import fi.linuxbox.upcloud.http.spi.Header
import fi.linuxbox.upcloud.http.spi.Headers
import spock.lang.Shared
import spock.lang.Specification

class SimpleHeadersSpec extends Specification {
    @Shared Headers headers = new SimpleHeaders(foo: 'bar')

    def "SimpleHeaders can return an iterator"() {
        expect:
        headers.iterator() != null
    }

    def "SimpleHeaders does not support header elements"() {
        when:
        headers["foo"]

        then:
        thrown(UnsupportedOperationException)
    }

    def "SimpleHeaders -> Iterator<Header> is read-only"() {
        when:
        headers.iterator().remove()

        then:
        thrown(UnsupportedOperationException)
    }

    def "SimpleHeaders header iterator works"() {
        given:
        Iterator<Header> iter = headers.iterator()

        expect:
        iter.hasNext()
        iter.next() != null
        !iter.hasNext()
    }

    def "SimpleHeader name and value works"() {
        given:
        Header header = headers.first()

        expect:
        header.name == 'foo'
        header.value == 'bar'
    }

    def "SimpleHeader does not support header elements"() {
        when:
        headers.first().elements

        then:
        thrown(UnsupportedOperationException)
    }
}

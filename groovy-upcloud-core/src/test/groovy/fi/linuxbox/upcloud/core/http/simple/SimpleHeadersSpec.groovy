package fi.linuxbox.upcloud.core.http.simple

import fi.linuxbox.upcloud.http.spi.Header
import fi.linuxbox.upcloud.http.spi.Headers
import spock.lang.Shared
import spock.lang.Specification

class SimpleHeadersSpec extends Specification {
    @Shared Headers headers = new SimpleHeaders(foo: 'bar')

    def "SimpleHeaders can return an iterator"() {
        expect:
        headers.all() != null
    }

    def "SimpleHeaders does not support header elements"() {
        when:
        headers["foo"]

        then:
        thrown(UnsupportedOperationException)
    }

    def "SimpleHeaders -> Iterator<Header> is read-only"() {
        when:
        headers.all().remove()

        then:
        thrown(UnsupportedOperationException)
    }

    def "SimpleHeaders header iterator works"() {
        given:
        Iterator<Header> iter = headers.all()

        expect:
        iter.hasNext()
        iter.next() != null
        !iter.hasNext()
    }

    def "SimpleHeader name and value works"() {
        given:
        Header header = headers.all().next()

        expect:
        header.name == 'foo'
        header.value == 'bar'
    }

    def "SimpleHeader does not support header elements"() {
        when:
        headers.all().next().elements

        then:
        thrown(UnsupportedOperationException)
    }
}

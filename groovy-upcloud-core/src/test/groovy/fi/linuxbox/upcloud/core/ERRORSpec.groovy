package fi.linuxbox.upcloud.core

import spock.lang.Specification

class ERRORSpec extends Specification {

    def "Error with a message"() {
        when:
        def err = new ERROR("failure")

        then:
        err.message == "failure"
        err.cause == null
    }

    def "Error with a message and a cause"() {
        when:
        def err = new ERROR("not supported", new UnsupportedOperationException())

        then:
        err.message == "not supported"
        err.cause?.class == UnsupportedOperationException
        err.cause.cause == null
    }

    def "Error message is read-only property"() {
        given:
        def err = new Error("foo")

        when:
        err.message = "bar"

        then:
        thrown(ReadOnlyPropertyException)
    }

    def "Error message does not have setter method"() {
        given:
        def err = new Error("baz")

        when:
        err.setMessage("bing")

        then:
        thrown(MissingMethodException)
    }

    def "Error cause is read-only property"() {
        given:
        def err = new Error("test", new IllegalArgumentException())

        when:
        err.cause = new NoRouteToHostException()

        then:
        thrown(ReadOnlyPropertyException)
    }

    def "Error cause does not have setter method"() {
        given:
        def err = new Error("blargh", new Throwable())

        when:
        err.setCause(new RuntimeException())

        then:
        thrown(MissingMethodException)
    }
}

package fi.linuxbox.upcloud.json.gjson

import fi.linuxbox.upcloud.json.spi.JSON
import spock.lang.*

import static com.jayway.jsonpath.matchers.JsonPathMatchers.*
import static org.hamcrest.MatcherAssert.*
import static org.hamcrest.Matchers.*

/**
 *
 */
class GJSONSpec extends Specification {

    static JSON out

    def setupSpec() {
        out = new GJSON()
    }

    def "Decode simple"() {
        given: "a simple JSON"
        def json = """{ "foo": "bar" }"""

        when: "JSON implementation is asked to decode the input stream into a representation"
        def repr = out.decode(inputStream(json))

        then: "the representation is "
        repr instanceof Map<String, Object>
        repr.foo == "bar"
    }

    def "Decode list wrapper"() {
        given:
        def json = """{ "timezones": { "timezone": ["Helsinki", "London", "New York"] } }"""

        when:
        def repr = out.decode(inputStream(json))

        then:
        repr.timezones.timezone == [ "Helsinki", "London", "New York" ]
    }

    def "Decode UTF-8"() {
        given: "JSON that contains characters not representable in ISO-8859-1 (the HTTP default charset)"
        def json = """{ "price": "100 €" }""" // EUR sign is in ISO-8859-9, not in ISO-8859-1

        when: "JSON implementation is asked to decode"
        def repr = out.decode(inputStream(json))

        then: "it decodes UTF-8 correctly"
        repr.price == '100 €'
    }

    def "Encode simple"() {
        given: "a simple resource representation"
        def repr = [foo: "bar"]

        when: "JSON implementation is asked to encode the representation into a stream"
        def json = string(out.encode(repr))

        then:
        assertThat(json, hasJsonPath('$.foo', equalTo('bar')))
    }

    def "Encode list wrapper"() {
        given:
        def repr = [timezones: [timezone: ["Helsinki", "London", "New York"]]]

        when:
        def json = string(out.encode(repr))

        then:
        assertThat(json, hasJsonPath('$.timezones.timezone[*]', hasSize(3)))
    }

    def "Encode UTF-8"() {
        given: "a representation that contains characters not representable in ISO-8859-1 (the HTTP default charset)"
        def repr = [price: '100 €'] // EUR sign is in ISO-8859-9, not in ISO-8859-1

        when: "JSON implementation is asked to encode"
        def json = string(out.encode(repr))

        then: "it encodes UTF-8 correctly"
        assertThat(json, hasJsonPath('$.price', equalTo('100 €')))
    }

    /**
     * Return an InputStream as the HTTP implementation is expected to.
     *
     *
     * @param json
     * @return
     */
    private static InputStream inputStream(String json) {
        new ByteArrayInputStream(json.getBytes("UTF-8"))
    }

    /**
     *
     * The HTTP implementation is expected to write the bytes from the inputStream to the socket as is.  I.e. JSON
     * implementation is responsible for ensuring that the JSON data in the stream is UTF-8 encoded.
     *
     * @param inputStream
     * @return
     */
    private static String string(InputStream inputStream) {
        inputStream.getText("UTF-8")
    }
}

package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.http.spi.Header
import fi.linuxbox.upcloud.http.spi.HeaderElement
import fi.linuxbox.upcloud.http.spi.Headers
import fi.linuxbox.upcloud.http.spi.Parameter

import org.apache.http.HttpResponse
import org.apache.http.message.BasicHttpResponse
import spock.lang.Shared
import spock.lang.Specification

import static org.apache.http.HttpStatus.SC_OK
import static org.apache.http.HttpVersion.HTTP_1_1

/**
 *
 */
class AhcHeadersSpec extends Specification {

    @Shared Headers headers

    def setupSpec() {
        HttpResponse response = new BasicHttpResponse(HTTP_1_1, SC_OK, "OK")
        response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost")
        response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"")
        response.addHeader("Authorization", "Basic asdasdasd")
        response.addHeader("Content-Type", "application/json")
        response.addHeader("Accept", "application/json; charset=UTF-8")
        headers = new AhcHeaders(response)
    }

    def "test"() {
        given:

        when:
        headers.all().each { Header header ->
            header.elements.each { HeaderElement headerElement ->
                println "${header.name}: ${header.value} (HeaderName: HeaderValue)"
                println "  ${headerElement.name} = ${headerElement.value} (ElementName = ElementValue)"
                headerElement.parameters.each { Parameter parameter ->
                    println "    ${parameter.first} = ${parameter.second} (ParameterName = ParameterValue)"
                }
            }
        }
        println "====="
        headers['Set-Cookie'].each { HeaderElement he ->
            println "Set-Cookie: xxx (Hardcoded)"
            println "  ${he.name} = ${he.value} (ElementName = ElementValue)"
            he.parameters.each { Parameter parameter ->
                println "    ${parameter.first} = ${parameter.second} (ParameterName = ParameterValue)"
            }
        }

        then:
        noExceptionThrown()
    }

    def "AhcHeaders -> Iterator<Header> is read-only"() {
        when:
        headers.all().remove()

        then:
        thrown(UnsupportedOperationException)
    }

    def "AhcHeaders -> Iterator<HeaderElement> is read-only"() {
        when:
        headers["Set-Cookie"].remove()

        then:
        thrown(UnsupportedOperationException)
    }

    def "AhcHeader -> Iterator<HeaderElement> is read-only 2"() {
        when:
        headers.all().next().elements.remove()

        then:
        thrown(UnsupportedOperationException)
    }

    def "AhcHeaderElement -> Iterator<Parameter> is read-only"() {
        when:
        headers["Set-Cookie"].next().parameters.remove()

        then:
        thrown(UnsupportedOperationException)
    }
}

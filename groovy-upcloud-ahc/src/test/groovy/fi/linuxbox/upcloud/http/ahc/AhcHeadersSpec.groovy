package fi.linuxbox.upcloud.http.ahc

import fi.linuxbox.upcloud.http.spi.Header
import fi.linuxbox.upcloud.http.spi.HeaderElement
import fi.linuxbox.upcloud.http.spi.Headers
import fi.linuxbox.upcloud.http.spi.Parameter

import org.apache.http.HttpResponse
import org.apache.http.message.BasicHttpResponse

import spock.lang.Specification

import static org.apache.http.HttpStatus.SC_OK
import static org.apache.http.HttpVersion.HTTP_1_1
/**
 *
 */
class AhcHeadersSpec extends Specification {

    def "test"() {
        given:
        HttpResponse response = new BasicHttpResponse(HTTP_1_1, SC_OK, "OK")
        Headers headers = new AhcHeaders(response)
        headers["Set-Cookie"] = "c1=a; path=/; domain=localhost"
        headers["Set-Cookie"] = "c2=b; path=\"/\", c3=c; domain=\"localhost\""
        headers["Authorization"] = "Basic asdasdasd"
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json; charset=UTF-8"

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
}

/*
 * Groovy UpCloud library - HTTP AHC Module
 * Copyright (C) 2018  <mikko@varri.fi>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        when:
        headers.each { Header header ->
            header.each { HeaderElement headerElement ->
                println "${header.name}: ${header.value} (HeaderName: HeaderValue)"
                println "  ${headerElement.name} = ${headerElement.value} (ElementName = ElementValue)"
                headerElement.each { Parameter parameter ->
                    println "    ${parameter.first} = ${parameter.second} (ParameterName = ParameterValue)"
                }
            }
        }

        then:
        noExceptionThrown()
    }

    def "test the cookie example in Headers javadoc"() {
        when:
        headers['Set-Cookie'].each { cookie ->
            println "Cookie ${cookie.name} has a value of ${cookie.value}"
            cookie.each { param ->
                println " - ${param.name} = ${param.value}"
            }
        }

        then:
        noExceptionThrown()
    }

    def "AhcHeaders -> Iterator<Header> is read-only"() {
        when:
        headers.iterator().remove()

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
        headers.first().elements.remove()

        then:
        thrown(UnsupportedOperationException)
    }

    def "AhcHeaderElement -> Iterator<Parameter> is read-only"() {
        when:
        headers["Set-Cookie"][0].parameters.remove()

        then:
        thrown(UnsupportedOperationException)
    }
}

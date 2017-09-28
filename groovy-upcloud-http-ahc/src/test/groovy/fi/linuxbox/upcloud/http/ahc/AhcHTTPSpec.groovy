/*
 * Groovy UpCloud library - HTTP AHC Module
 * Copyright (C) 2017  <mikko@varri.fi>
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

import fi.linuxbox.upcloud.http.spi.ERROR
import fi.linuxbox.upcloud.http.spi.Request
import fi.linuxbox.upcloud.http.spi.META
import fi.linuxbox.upcloud.core.http.simple.SimpleHeaders
import org.apache.http.*
import org.apache.http.entity.BasicHttpEntity
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.message.BasicStatusLine
import spock.lang.*

class AhcHTTPSpec extends Specification {

    CloseableHttpAsyncClient client = Mock()

    AhcHTTP http

    Request request

    def setup() {
        http = new AhcHTTP(client)

        request = new Request(
                // AhcHTTP is using static helper to parse the host, so host has to be valid
                host: 'http://localhost:8080',
                // AhcHTTP is using real HttpRequest instances, so method and resource are parsed by those
                method: 'GET',
                resource: '/',
                headers: new SimpleHeaders([
                        'Accept'        : 'application/json; charset=UTF-8',
                        'Authorization' : 'Basic 1234',
                        'Content-Type'  : 'application/json',
                        'User-Agent'    : 'Groovy Upcloud/1.0'
                ]),
                body: null
        )
    }

    def "Client arguments"() {
        when: "the HTTP implementation is invoked"
            http.execute(request, {})

        then: "it passes the host, method, resource, headers, and a callback to the real client"
            1 * client.execute({ HttpHost host -> host != null  },
                    { HttpRequest req ->
                        req.requestLine.method == 'GET' &&
                                req.requestLine.uri == '/' &&
                                req.getHeaders('Accept').length == 1 &&
                                req.getFirstHeader('Accept').elements.length == 1 &&
                                req.getFirstHeader('Accept').elements[0].name == 'application/json' &&
                                req.getFirstHeader('Accept').elements[0].parameters.length == 1 &&
                                req.getFirstHeader('Accept').elements[0].parameters[0].name == 'charset' &&
                                req.getFirstHeader('Accept').elements[0].parameters[0].value == 'UTF-8' &&
                                req.getHeaders('Authorization').length == 1 &&
                                req.getFirstHeader('Authorization').elements.length == 1 &&
                                req.getFirstHeader('Authorization').elements[0].name == 'Basic 1234' &&
                                req.getHeaders('Content-Type').length == 1 &&
                                req.getFirstHeader('Content-Type').elements.length == 1 &&
                                req.getFirstHeader('Content-Type').elements[0].name == 'application/json'
                    },
                    { it != null })
    }

    // client.execute(*_) >> { args -> args[2].completed(response) -> cb(meta, null, null)

    def "Completion"() {
        given:
            HttpResponse resp = Mock()
            1 * resp.getStatusLine() >> {
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK")
            }
            1 * resp.getEntity() >> {
                def entity = new BasicHttpEntity()
                entity.content = new ByteArrayInputStream(new byte[0])
                entity
            }
            1 * client.execute(*_) >> { args ->
                args[2].completed(resp)
            }

        and:
            boolean ok = false
            Closure<Void> cb = { def meta, def body, def err ->
                ok = meta instanceof META && body instanceof InputStream && err == null
            }

        when:
            http.execute(request, cb)

        then:
            ok
    }

    // Following tests missing, but they would require mocking the CloseableHttpAsyncClient
    //  - when !client.running, calls client.start
    //  - when closed, closes client

    def "Execution exception reported as ERROR"() {
        given:
            1 * client.execute(*_) >> {
                Exception e =  new IllegalArgumentException("TEST EXCEPTION; THIS IS DELIBERATE; DO NOT PANIC")
                e.stackTrace = new StackTraceElement[0]
                throw e
            }

        and:
            boolean ok = false
            Closure<Void> cb = { def meta, def body, def err -> ok = meta == null && err instanceof ERROR }

        when:
            http.execute(request, cb)

        then:
            ok
    }

    def "Background failure reported as ERROR"() {
        given:
            1 * client.execute(*_) >> { args ->
                Exception e =  new IllegalArgumentException("TEST EXCEPTION; THIS IS DELIBERATE; DO NOT PANIC")
                e.stackTrace = new StackTraceElement[0]
                args[2].failed(e)
            }

        and:
            boolean ok = false
            Closure<Void> cb = { def meta, def body, def err -> ok = meta == null && err instanceof ERROR }

        when:
            http.execute(request, cb)

        then:
            ok
    }

    def "Cancellation reported as ERROR"() {
        given:
            1 * client.execute(*_) >> { args ->
                args[2].cancelled()
            }

        and:
            boolean ok = false
            Closure<Void> cb = { def meta, def body, def err -> ok = meta == null && err instanceof ERROR }

        when:
            http.execute(request, cb)

        then:
            ok
    }


}

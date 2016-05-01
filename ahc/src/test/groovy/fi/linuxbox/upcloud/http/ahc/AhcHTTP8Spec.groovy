package fi.linuxbox.upcloud.http.ahc

import java.util.concurrent.*
import spock.lang.*

import fi.linuxbox.upcloud.core.*
import fi.linuxbox.upcloud.core.http.*
import fi.linuxbox.upcloud.core.http.simple.SimpleHeaders

import static java.util.concurrent.TimeUnit.SECONDS

/**
 *
 */
class AhcHTTP8Spec extends Specification {

    @Shared AhcHTTP http

    def setupSpec() {
        http = new AhcHTTP(new AhcClientProvider().get())
    }

    def cleanupSpec() {
        http.close()
    }

    def "test"() {
        given:
            def cv = new CountDownLatch(8)

        when:
            8.times {
                http.execute(new Exchange(
                        host: 'https://www.google.fi',
                        method: 'GET',
                        resource: '/',
                        headers: new SimpleHeaders([ 'X-Test': 'Yes' ]),
                        cb: { META meta, InputStream body, ERROR err ->
                            if (meta.status == "200")
                                cv.countDown()
                        }
                ))
            }
            cv.await(30, SECONDS)

        then:
            noExceptionThrown()
    }

}

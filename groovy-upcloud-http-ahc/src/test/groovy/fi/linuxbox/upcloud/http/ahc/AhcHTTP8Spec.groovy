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

import java.util.concurrent.*
import spock.lang.*
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
                http.execute new Request(
                        host: 'https://www.google.fi',
                        method: 'GET',
                        resource: '/',
                        headers: new SimpleHeaders([ 'X-Test': 'Yes' ])),
                        { META meta, InputStream body, ERROR err ->
                            if (meta.status == 200)
                                cv.countDown()
                        }
            }
            cv.await 30, SECONDS

        then:
            noExceptionThrown()
    }

}

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

import org.apache.http.nio.client.HttpAsyncClient
import spock.lang.Shared
import spock.lang.Specification

class AhcHTTPUserAgentSpec extends Specification {

    @Shared AhcHTTP http

    def setupSpec() {
        http = new AhcHTTP(new AhcClientProvider().get())
    }

    def cleanupSpec() {
        http.close()
    }

    def "User-Agent should be Apache-HttpAsyncClient/4.1.x on Java 8"() {
        expect:
        http.userAgent.matches(/Apache-HttpAsyncClient\/4\.1\.[\d] \(Java\/1\.8\.0_[\d]+\)/)
    }
}

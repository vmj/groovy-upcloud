/*
 * Groovy UpCloud library - Core Module
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
package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.http.spi.ERROR
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

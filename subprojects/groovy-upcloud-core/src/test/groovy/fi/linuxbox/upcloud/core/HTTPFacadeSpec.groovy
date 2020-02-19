/*
 * Groovy UpCloud library - Core Module
 * Copyright (C) 2018  Mikko Värri
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

import spock.lang.Specification

class HTTPFacadeSpec extends Specification {
    private final static Integer SUCCESS = Integer.MAX_VALUE
    private final static Map<?, Closure<Void>> CBS = ["200": {}]
    private final static String PATH = "/some/resource"
    private final static Resource RESOURCE = new Resource()
    private final static Closure<Void> CB = {}

    private static class MySession extends HTTPFacade<Integer> {
        Map<?, Closure<Void>> cbs
        String method
        String path
        Resource resource
        Closure<Void> cb

        @Override
        Integer request(Map<?, Closure<Void>> cbs, String method, String path, Resource resource, Closure<Void> cb) {
            this.cbs = cbs
            this.method = method
            this.path = path
            this.resource = resource
            this.cb = cb

            return SUCCESS
        }
    }

    MySession session

    def setup() {
        session = new MySession()
    }

    def "GET(Map, String, Closure)"() {
        when:
        def ok = session.GET(CBS, PATH, CB)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'GET'
        session.path === PATH
        session.resource == null
        session.cb === CB
    }

    def "GET(Map, String)"() {
        when:
        def ok = session.GET(CBS, PATH)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'GET'
        session.path === PATH
        session.resource == null
        session.cb == null
    }

    def "GET(String, Map, Closure)"() {
        when:
        def ok = session.GET(PATH, CBS, CB)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'GET'
        session.path === PATH
        session.resource == null
        session.cb === CB
    }

    def "GET(String, Map)"() {
        when:
        def ok = session.GET(PATH, CBS)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'GET'
        session.path === PATH
        session.resource == null
        session.cb == null
    }

    def "GET(String, Closure)"() {
        when:
        def ok = session.GET(PATH, CB)

        then:
        ok == SUCCESS
        session.cbs.isEmpty()
        session.method == 'GET'
        session.path === PATH
        session.resource == null
        session.cb === CB
    }

    def "GET(String)"() {
        when:
        def ok = session.GET(PATH)

        then:
        ok == SUCCESS
        session.cbs.isEmpty()
        session.method == 'GET'
        session.path === PATH
        session.resource == null
        session.cb == null
    }

    def "DELETE(Map, String, Closure)"() {
        when:
        def ok = session.DELETE(CBS, PATH, CB)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'DELETE'
        session.path === PATH
        session.resource == null
        session.cb === CB
    }

    def "DELETE(Map, String)"() {
        when:
        def ok = session.DELETE(CBS, PATH)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'DELETE'
        session.path === PATH
        session.resource == null
        session.cb == null
    }

    def "DELETE(String, Map, Closure)"() {
        when:
        def ok = session.DELETE(PATH, CBS, CB)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'DELETE'
        session.path === PATH
        session.resource == null
        session.cb === CB
    }

    def "DELETE(String, Map)"() {
        when:
        def ok = session.DELETE(PATH, CBS)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'DELETE'
        session.path === PATH
        session.resource == null
        session.cb == null
    }

    def "DELETE(String, Closure)"() {
        when:
        def ok = session.DELETE(PATH, CB)

        then:
        ok == SUCCESS
        session.cbs.isEmpty()
        session.method == 'DELETE'
        session.path === PATH
        session.resource == null
        session.cb === CB
    }

    def "DELETE(String)"() {
        when:
        def ok = session.DELETE(PATH)

        then:
        ok == SUCCESS
        session.cbs.isEmpty()
        session.method == 'DELETE'
        session.path === PATH
        session.resource == null
        session.cb == null
    }

    def "PUT(Map, String, Resource, Closure)"() {
        when:
        def ok = session.PUT(CBS, PATH, RESOURCE, CB)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'PUT'
        session.path === PATH
        session.resource === RESOURCE
        session.cb === CB
    }

    def "PUT(Map, String, Resource)"() {
        when:
        def ok = session.PUT(CBS, PATH, RESOURCE)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'PUT'
        session.path === PATH
        session.resource === RESOURCE
        session.cb == null
    }

    def "PUT(String, Resource, Map, Closure)"() {
        when:
        def ok = session.PUT(PATH, RESOURCE, CBS, CB)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'PUT'
        session.path === PATH
        session.resource === RESOURCE
        session.cb === CB
    }

    def "PUT(String, Resource, Map)"() {
        when:
        def ok = session.PUT(PATH, RESOURCE, CBS)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'PUT'
        session.path === PATH
        session.resource === RESOURCE
        session.cb == null
    }

    def "PUT(String, Resource, Closure)"() {
        when:
        def ok = session.PUT(PATH, RESOURCE, CB)

        then:
        ok == SUCCESS
        session.cbs.isEmpty()
        session.method == 'PUT'
        session.path === PATH
        session.resource === RESOURCE
        session.cb === CB
    }

    def "PUT(String, Resource)"() {
        when:
        def ok = session.PUT(PATH, RESOURCE)

        then:
        ok == SUCCESS
        session.cbs.isEmpty()
        session.method == 'PUT'
        session.path === PATH
        session.resource === RESOURCE
        session.cb == null
    }

    def "POST(Map, String, Resource, Closure)"() {
        when:
        def ok = session.POST(CBS, PATH, RESOURCE, CB)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'POST'
        session.path === PATH
        session.resource === RESOURCE
        session.cb === CB
    }

    def "POST(Map, String, Resource)"() {
        when:
        def ok = session.POST(CBS, PATH, RESOURCE)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'POST'
        session.path === PATH
        session.resource === RESOURCE
        session.cb == null
    }

    def "POST(String, Resource, Map, Closure)"() {
        when:
        def ok = session.POST(PATH, RESOURCE, CBS, CB)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'POST'
        session.path === PATH
        session.resource === RESOURCE
        session.cb === CB
    }

    def "POST(String, Resource, Map)"() {
        when:
        def ok = session.POST(PATH, RESOURCE, CBS)

        then:
        ok == SUCCESS
        session.cbs === CBS
        session.method == 'POST'
        session.path === PATH
        session.resource === RESOURCE
        session.cb == null
    }

    def "POST(String, Resource, Closure)"() {
        when:
        def ok = session.POST(PATH, RESOURCE, CB)

        then:
        ok == SUCCESS
        session.cbs.isEmpty()
        session.method == 'POST'
        session.path === PATH
        session.resource === RESOURCE
        session.cb === CB
    }

    def "POST(String, Resource)"() {
        when:
        def ok = session.POST(PATH, RESOURCE)

        then:
        ok == SUCCESS
        session.cbs.isEmpty()
        session.method == 'POST'
        session.path === PATH
        session.resource === RESOURCE
        session.cb == null
    }
}

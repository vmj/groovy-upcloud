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

import fi.linuxbox.upcloud.http.spi.HTTP
import fi.linuxbox.upcloud.http.spi.META
import fi.linuxbox.upcloud.json.spi.JSON
import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import spock.lang.*

class ResourceSpec extends Specification {

    def "Resource can be created without any arguments"() {
        when:
        new Resource()

        then:
        noExceptionThrown()
    }

    def "Empty resource as a Map is an empty Map"() {
        given:
        def resource = new Resource()

        when:
        def repr = resource as Map

        then:
        repr == [:]
    }
    def "Resource can be created with simple properties"() {
        when:
        def resource = new Resource(repr: [ name: 'resource name', description: 'resource description'])

        then:
        resource.hasProperty('name')
        resource.name == 'resource name'
        resource.description == 'resource description'
    }

    def "Simple resource as a Map has simple properties"() {
        given:
        def resource = new Resource(repr: [ name: 'resource name', description: 'resource description'])

        when:
        def repr = resource as Map

        then:
        repr == [name: 'resource name', description: 'resource description']
    }

    def "Resource can be created with nested resource property"() {
        given:
        def meta = new META(status: 200)
        def session = new SimpleSession(Mock(HTTP), Mock(JSON), "foo", "bar")

        when:
        def resource = new Resource(HTTP: session, META: meta, repr: [
                error: [
                        error_code: 'SOME_ERROR',
                        error_message: 'Something went wrong'
                ]
        ])

        then:
        resource.HTTP == session
        resource.META == meta
        resource.error instanceof Resource
        resource.error.class.name == 'fi.linuxbox.upcloud.resource.Error'
        resource.error.HTTP == session
        resource.error.META == meta
        resource.error.errorCode == 'SOME_ERROR'
        resource.error.errorMessage == 'Something went wrong'
        !resource.hasProperty('errorMessage')
        !resource.hasProperty('name')
        resource.toString() == "Resource(error: Error(errorCode: SOME_ERROR, errorMessage: Something went wrong))"
    }

    def "Resource with resource property as a map has map property"() {
        given:
        def resource = new Resource(repr: [
                error: [
                        error_code: 'SOME_ERROR',
                        error_message: 'Something went wrong'
                ]
        ])

        when:
        def repr = resource as Map

        then:
        repr == [error: [error_code: 'SOME_ERROR', error_message: 'Something went wrong']]
    }

    def "Resource can be created with list-of-resources property"() {
        when:
        def resource = new Resource(repr: [
                zones: [
                        zone: [
                                [id: 'fi-hel1', description: 'Helsinki #1'],
                                [id: 'uk-lon1', description: 'London #1'],
                                [id: 'us-chi1', description: 'Chicago #1']
                        ]
                ]
        ])

        then:
        resource.zones instanceof List
        resource.zones.every { it.class.simpleName == 'Zone' }
        resource.zones[0].class == resource.zones[1].class
        resource.zones[0].id == 'fi-hel1'
        resource.zones[0].description == 'Helsinki #1'
        resource.zones[1].id == 'uk-lon1'
        resource.zones[1].description == 'London #1'
        resource.zones[2].id == 'us-chi1'
        resource.zones[2].description == 'Chicago #1'
        resource.toString() == "Resource(zones: [Zone(description: Helsinki #1, id: fi-hel1), Zone(description: London #1, id: uk-lon1), Zone(description: Chicago #1, id: us-chi1)])"
    }

    def "List-of-resources property as a map has list of resources property"() {
        given:
        def resource = new Resource(repr: [
                zones: [
                        zone: [
                                [id: 'fi-hel1', description: 'Helsinki #1'],
                                [id: 'uk-lon1', description: 'London #1'],
                                [id: 'us-chi1', description: 'Chicago #1']
                        ]
                ]
        ])

        when:
        def repr = resource as Map

        then:
        repr == [zones:[zone:[[id: 'fi-hel1', description: 'Helsinki #1'],
                              [id: 'uk-lon1', description: 'London #1'],
                              [id: 'us-chi1', description: 'Chicago #1']]]]
    }

    def "Resource can be created with a list of strings"() {
        when:
        def resource = new Resource(repr: [
                timezones: [
                        timezone: [
                                'America/Chicago',
                                'Europe/Helsinki',
                                'Europe/London',
                                'UTC'
                        ]
                ]
        ])

        then:
        resource.timezones == ["America/Chicago", "Europe/Helsinki", "Europe/London", "UTC"]
        resource.toString() == "Resource(timezones: [America/Chicago, Europe/Helsinki, Europe/London, UTC])"
    }

    def "List-of-strings property as a map has a list of strings property"() {
        given:
        def resource = new Resource(repr: [ ssh_keys: [ ssh_key: [ "foo", "bar", "baz"]]])

        when:
        def repr = resource as Map

        then:
        repr == [ ssh_keys: [ ssh_key: [ "foo", "bar", "baz" ] ] ]
    }

    def "Resource property can have a previously unknown class"() {
        when: "a map property has a previously unknown type"
        def resource = new Resource(repr: [ dynamic_resource: [ :]])

        then: "a class is created dynamically"
        resource.dynamicResource?.class?.simpleName == "DynamicResource"
        resource.toString() == "Resource(dynamicResource: DynamicResource())"
    }

    def "In dynamic resources, properties can be set"() {
        given:
        def resource = new Resource(repr: [ dynamic_property: [ :]])

        when:
        resource.baz = [1,2,3]

        then:
        resource.baz == [1,2,3]
    }

    def "Accessing properties that are not defined throws missing property exception"() {
        given:
        def resource = new Resource(repr: [ missing_property: [ :]])

        when:
        resource.foo == null

        then:
        thrown MissingPropertyException
    }

    def "Properties can not change type"() {
        given:
        def resource = new Resource(repr: [ dynamic_type: [ :]])

        when:
        resource.bar = [1,2,3]
        resource.bar = "foo"

        then:
        thrown GroovyCastException
    }
}

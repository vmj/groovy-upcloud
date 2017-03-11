package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.core.http.HTTP
import fi.linuxbox.upcloud.core.http.META
import org.codehaus.groovy.runtime.typehandling.*
import spock.lang.*
import fi.linuxbox.upcloud.core.json.*

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
        def meta = new META(200, null)
        def api = new API(Mock(HTTP), Mock(JSON), "foo", "bar")

        when:
        def resource = new Resource(API: api, META: meta, repr: [
                error: [
                        error_code: 'SOME_ERROR',
                        error_message: 'Something went wrong'
                ]
        ])

        then:
        resource.API == api
        resource.META == meta
        resource.error instanceof Resource
        resource.error.class.name == 'fi.linuxbox.upcloud.resource.Error'
        resource.error.API == api
        resource.error.META == meta
        resource.error.errorCode == 'SOME_ERROR'
        resource.error.errorMessage == 'Something went wrong'
        !resource.hasProperty('errorMessage')
        !resource.hasProperty('name')
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

    def "In dynamic resources, properties can be set using a method call"() {
        given:
        def resource = new Resource(repr: [ dynamic_method: [ :]])

        when: "a property baz is set with a method call"
        resource.baz([1, 2, 3])

        then: "the property can be accessed via property"
        resource.baz == [1, 2, 3]
        and: "the getter method returns resource"
        resource.baz() is resource
        and: "the setter method allows for chaining, too"
        resource.baz([3,2,1]) is resource
    }

    def "In dynamic resources, missing methods return the resource"() {
        given:
        def resource = new Resource(repr: [ missing_method: [ :]])

        expect: "unset property just returns the resource, allows chaining"
        resource.foo() is resource
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

    def "Properties can change type even if changing it via setter method"() {
        given:
        def resource = new Resource(repr: [ dynamic_type: [ :]])

        when:
        resource.bar([1,2,3])
        resource.bar("foo")

        then:
        thrown GroovyCastException
    }


    def "Wrapped empty resource has resource property"() {
        when:
        def resource = new Resource().wrapper()

        then:
        resource instanceof Resource
        resource.resource instanceof Resource
    }

    def "Wrapped resource property name is proper"() {
        given:
        def resource = new Resource(repr: [ some_thing: [ :]])
        def someThing = resource.someThing

        when:
        def wrappedSomething = someThing.wrapper()

        then:
        wrappedSomething instanceof Resource
        wrappedSomething.someThing instanceof Resource
        wrappedSomething.someThing.class.simpleName == "SomeThing"
    }

    def "Projection"() {
        given:
        def resource = new Resource(repr: [ full_blown_thing: [ first: 1, second: 2, third: 3]])
        def fullBlownThing = resource.fullBlownThing

        when:
        def projection = fullBlownThing.proj(['second', 'third'])

        then:
        projection instanceof Resource
        projection.class.simpleName == "FullBlownThing"
        projection.second == 2
        projection.third == 3
        !projection.hasProperty('first')
    }

    @Unroll
    def "Name juggling: #type_name -> #className"() {
        expect:
        Resource.className(type_name) == className

        where:
        type_name                | className
        'server'                 | 'Server'
        'storage_device'         | 'StorageDevice'
        'destination_port_start' | 'DestinationPortStart'
    }

    @Unroll
    def "Name juggling: #className -> #type_name"() {
        expect:
        Resource.type_name(className) == type_name

        where:
        className              | type_name
        'RESOURCE'             | 'resource'
        'Server'               | 'server'
        'StorageDevice'        | 'storage_device'
        'DestinationPortStart' | 'destination_port_start'
    }

    @Unroll
    def "Name juggling: #type_name -> #propertyName"() {
        expect:
        Resource.propertyName(type_name) == propertyName

        where:
        type_name                | propertyName
        'server'                 | 'server'
        'storage_device'         | 'storageDevice'
        'destination_port_start' | 'destinationPortStart'
    }
}

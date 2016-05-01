package fi.linuxbox.upcloud.core

import org.codehaus.groovy.runtime.typehandling.*
import spock.lang.*

import fi.linuxbox.upcloud.core.http.*

class MODELSpec extends Specification {

    def "Model can be created without any arguments"() {
        when:
        new MODEL()

        then:
        noExceptionThrown()
    }

    def "Empty model as a Map is an empty Map"() {
        given:
        def model = new MODEL()

        when:
        def repr = model as Map

        then:
        repr == [:]
    }
    def "Model can be created with simple properties"() {
        when:
        def model = new MODEL(repr: [name: 'model name', description: 'model description'])

        then:
        model.hasProperty('name')
        model.name == 'model name'
        model.description == 'model description'
    }

    def "Simple model as a Map has simple properties"() {
        given:
        def model = new MODEL(repr: [name: 'model name', description: 'model description'])

        when:
        def repr = model as Map

        then:
        repr == [name: 'model name', description: 'model description']
    }

    def "Model can be created with nested model property"() {
        given:
        def meta = new META(200, null)
        def api = new API(Mock(HTTP), null, null, null)

        when:
        def model = new MODEL(API: api, META: meta, repr: [
                error: [
                        error_code: 'SOME_ERROR',
                        error_message: 'Something went wrong'
                ]
        ])

        then:
        model.API == api
        model.META == meta
        model.error instanceof MODEL
        model.error.class.name == 'fi.linuxbox.upcloud.model.Error'
        model.error.API == api
        model.error.META == meta
        model.error.errorCode == 'SOME_ERROR'
        model.error.errorMessage == 'Something went wrong'
        !model.hasProperty('errorMessage')
        !model.hasProperty('name')
    }

    def "Model with model property as a map has map property"() {
        given:
        def model = new MODEL(repr: [
                error: [
                        error_code: 'SOME_ERROR',
                        error_message: 'Something went wrong'
                ]
        ])

        when:
        def repr = model as Map

        then:
        repr == [error: [error_code: 'SOME_ERROR', error_message: 'Something went wrong']]
    }

    def "Model can be created with list-of-models property"() {
        when:
        def model = new MODEL(repr: [
                zones: [
                        zone: [
                                [id: 'fi-hel1', description: 'Helsinki #1'],
                                [id: 'uk-lon1', description: 'London #1'],
                                [id: 'us-chi1', description: 'Chicago #1']
                        ]
                ]
        ])

        then:
        model.zones instanceof List
        model.zones.every { it.class.simpleName == 'Zone' }
        model.zones[0].class == model.zones[1].class
        model.zones[0].id == 'fi-hel1'
        model.zones[0].description == 'Helsinki #1'
        model.zones[1].id == 'uk-lon1'
        model.zones[1].description == 'London #1'
        model.zones[2].id == 'us-chi1'
        model.zones[2].description == 'Chicago #1'
    }

    def "List-of-models property as a map has list of models property"() {
        given:
        def model = new MODEL(repr: [
                zones: [
                        zone: [
                                [id: 'fi-hel1', description: 'Helsinki #1'],
                                [id: 'uk-lon1', description: 'London #1'],
                                [id: 'us-chi1', description: 'Chicago #1']
                        ]
                ]
        ])

        when:
        def repr = model as Map

        then:
        repr == [zones:[zone:[[id: 'fi-hel1', description: 'Helsinki #1'],
                              [id: 'uk-lon1', description: 'London #1'],
                              [id: 'us-chi1', description: 'Chicago #1']]]]
    }

    def "Model can be created with a list of strings"() {
        when:
        def model = new MODEL(repr: [
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
        model.timezones == ["America/Chicago", "Europe/Helsinki", "Europe/London", "UTC"]
    }

    def "List of strings as a map is not supported"() {
        given:
        def model = new MODEL(repr: [timezones: [timezone: ["foo", "bar", "baz"]]])

        when:
        model as Map

        then:
        thrown UnsupportedOperationException
    }

    def "Model property can have a previously unknown class"() {
        when: "a map property has a previously unknown type"
        def model = new MODEL(repr: [dynamic_model: [:]])

        then: "a class is created dynamically"
        model.dynamicModel?.class?.simpleName == "DynamicModel"
    }

    def "In dynamic models, properties can be set"() {
        given:
        def model = new MODEL(repr: [dynamic_property: [:]])

        when:
        model.baz = [1,2,3]

        then:
        model.baz == [1,2,3]
    }

    def "Accessing properties that are not defined returns null"() {
        given:
        def model = new MODEL(repr: [missing_property: [:]])

        expect:
        model.foo == null
    }

    def "In dynamic models, properties can be set using a method call"() {
        given:
        def model = new MODEL(repr: [dynamic_method: [:]])

        when: "a property baz is set with a method call"
        model.baz([1, 2, 3])

        then: "the property can be accessed via property"
        model.baz == [1, 2, 3]
        and: "the getter method returns model"
        model.baz() is model
        and: "the setter method allows for chaining, too"
        model.baz([3,2,1]) is model
    }

    def "In dynamic models, missing methods return the model"() {
        given:
        def model = new MODEL(repr: [missing_method: [:]])

        expect: "unset property just returns the model, allows chaining"
        model.foo() is model
    }

    def "Properties can not change type"() {
        given:
        def model = new MODEL(repr: [dynamic_type: [:]])

        when:
        model.bar = [1,2,3]
        model.bar = "foo"

        then:
        thrown GroovyCastException
    }

    def "Properties can change type even if changing it via setter method"() {
        given:
        def model = new MODEL(repr: [dynamic_type: [:]])

        when:
        model.bar([1,2,3])
        model.bar("foo")

        then:
        thrown GroovyCastException
    }


    def "Wrapped empty model has model property"() {
        when:
        def model = new MODEL().wrapper()

        then:
        model instanceof MODEL
        model.model instanceof MODEL
    }

    def "Wrapped model property name is proper"() {
        given:
        def model = new MODEL(repr: [some_thing: [:]])
        def someThing = model.someThing

        when:
        def wrappedSomething = someThing.wrapper()

        then:
        wrappedSomething instanceof MODEL
        wrappedSomething.someThing instanceof MODEL
        wrappedSomething.someThing.class.simpleName == "SomeThing"
    }

    def "Projection"() {
        given:
        def model = new MODEL(repr: [full_blown_thing: [first: 1, second: 2, third: 3]])
        def fullBlownThing = model.fullBlownThing

        when:
        def projection = fullBlownThing.proj(['second', 'third'])

        then:
        projection instanceof MODEL
        projection.class.simpleName == "FullBlownThing"
        projection.second == 2
        projection.third == 3
        !projection.hasProperty('first')
    }

    @Unroll
    def "Name juggling: #type_name -> #className"() {
        expect:
        MODEL.className(type_name) == className

        where:
        type_name                | className
        'server'                 | 'Server'
        'storage_device'         | 'StorageDevice'
        'destination_port_start' | 'DestinationPortStart'
    }

    @Unroll
    def "Name juggling: #className -> #type_name"() {
        expect:
        MODEL.type_name(className) == type_name

        where:
        className              | type_name
        'MODEL'                | 'model'
        'Server'               | 'server'
        'StorageDevice'        | 'storage_device'
        'DestinationPortStart' | 'destination_port_start'
    }

    @Unroll
    def "Name juggling: #type_name -> #propertyName"() {
        expect:
        MODEL.propertyName(type_name) == propertyName

        where:
        type_name                | propertyName
        'server'                 | 'server'
        'storage_device'         | 'storageDevice'
        'destination_port_start' | 'destinationPortStart'
    }
}

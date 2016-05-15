package fi.linuxbox.upcloud.core

import spock.lang.*

import fi.linuxbox.upcloud.core.http.*
import fi.linuxbox.upcloud.core.json.*

/**
 *
 */
class APISpec extends Specification {

    HTTP http = Mock()
    JSON json = Mock()

    API api

    def setup() {
        api = new API(http, json, "open", "sesame")
    }

    def "Request method"() {
        when: "calling the API request method with GET"
            api.request('GET', 'some-resource', null, {})

        then: "the HTTP implementation is called with GET"
            1 * http.execute({ it.method == 'GET' })
    }

    def "Request URI"() {
        when: "calling the API request method with a resource"
            api.request('GET', 'some-resource', null, {})

        then: "the HTTP implementation receives both the host and full resource path"
            1 * http.execute({ it.host =~ '^https?://[^/]+$' && it.resource =~ '^/1.[1-9]/some-resource$' })
    }

    def "Request headers"() {
        when: "calling the API request method"
            api.request('GET', 'some-resource', null, {})

        then: "the HTTP implementation receives the correct headers"
            1 * http.execute({
                Headers headers = it.headers
                def ok = headers.all().size() == 5
                ok = ok && headers.all().any { it.name == 'Accept' }
                ok = ok && headers.all().any { it.name == 'Authorization' }
                ok = ok && headers.all().any { it.name == 'Content-Type' }
                ok = ok && headers.all().any { it.name == 'Host' }
                ok = ok && headers.all().any { it.name == 'User-Agent' }
                ok = ok && headers.all().every {
                    switch (it.name) {
                        case 'Accept':
                            return it.value == 'application/json; charset=UTF-8'
                        case 'Authorization':
                            return it.value == 'Basic b3BlbjpzZXNhbWU='
                        case 'Content-Type':
                            return it.value == 'application/json'
                        case 'Host':
                            return it.value == 'api.upcloud.com'
                        case 'User-Agent':
                            return it.value.startsWith('Groovy UpCloud/0.0.2-SNAPSHOT ')
                        default:
                            return false
                    }
                }
                ok
            })
    }

    def "Request body null"() {
        when: "calling the API request method with null resource"
            api.request('GET', 'some-resource', null, {})

        then: "the HTTP implementation receives null entity body"
            1 * http.execute({ it.body == null })
    }

    def "Request body non-null"() {
        given: "a fake JSON serializer"
            def inputStream = new ByteArrayInputStream(new byte[0])
            1 * json.encode(_) >> inputStream

        when: "calling the API request method with non-null resource"
            api.request('GET', 'some-resource', new Resource(), {})

        then: "the HTTP implementation receives the serialized resource"
            1 * http.execute({ it.body == inputStream })
    }

    def "Request with additional callbacks"() {
        when: "calling the API request method with additional callbacks"
            api.request 'GET', 'some-resource', null,
                    200: {},
                    300: {},
                    {}

        then: "the HTTP implementation is still called as before"
            1 * http.execute({
                it.method == 'GET' && it.resource =~ '/some-resource$' && it.body == null
            })
    }

    def "POST request"() {
        when: "calling the API request method with POST"
            api.request('POST', 'other/resource', null, {})

        then: "the HTTP implementation is called with POST"
            1 * http.execute({ it.method == 'POST' && it.resource =~ '/other/resource$' })
    }

    def "Convenience GET method"() {
        when: "calling the API GET method"
            api.GET('something') {}

        then: "the HTTP implementation is called with GET and null body"
            1 * http.execute({ it.method == 'GET' && it.resource =~ '/something$' && it.body == null })
    }

    def "Convenience GET method with additional callbacks"() {
        when: "calling the API GET method with additional callbacks"
            api.GET('something', 404: {}) {}

        then: "the HTTP implementation is called with GET and null body"
            1 * http.execute({ it.method == 'GET' && it.resource =~ '/something$' && it.body == null })
    }


    def "Convenience DELETE method"() {
        when: "calling the API DELETE method"
            api.DELETE('something') {}

        then: "the HTTP implementation is called with DELETE and null body"
            1 * http.execute({ it.method == 'DELETE' && it.resource =~ '/something$' && it.body == null })
    }

    def "Convenience DELETE method with additional callbacks"() {
        when: "calling the API DELETE method with additional callbacks"
            api.DELETE('something', 404: {}) {}

        then: "the HTTP implementation is called with DELETE and null body"
            1 * http.execute({ it.method == 'DELETE' && it.resource =~ '/something$' && it.body == null })
    }

    def "Convenience PUT method"() {
        given: "a fake JSON serializer"
            def inputStream = new ByteArrayInputStream(new byte[0])
            1 * json.encode(_) >> inputStream

        when: "calling the API PUT method"
            api.PUT('something', new Resource()) {}

        then: "the HTTP implementation is called with PUT and non-null body"
            1 * http.execute({ it.method == 'PUT' && it.resource =~ '/something$' && it.body == inputStream })
    }

    def "Convenience PUT method with additional callbacks"() {
        given: "a fake JSON serializer"
            def inputStream = new ByteArrayInputStream(new byte[0])
            1 * json.encode(_) >> inputStream

        when: "calling the API PUT method with additional callbacks"
            api.PUT('something', new Resource(), 404: {}) {}

        then: "the HTTP implementation is called with PUT and non-null body"
            1 * http.execute({ it.method == 'PUT' && it.resource =~ '/something$' && it.body == inputStream })
    }

    def "Convenience POST method"() {
        given: "a fake JSON serializer"
            def inputStream = new ByteArrayInputStream(new byte[0])
            1 * json.encode(_) >> inputStream

        when: "calling the API POST method"
            api.POST('something', new Resource()) {}

        then: "the HTTP implementation is called with POST and non-null body"
            1 * http.execute({ it.method == 'POST' && it.resource =~ '/something$' && it.body == inputStream })
    }

    def "Convenience POST method with additional callbacks"() {
        given: "a fake JSON serializer"
            def inputStream = new ByteArrayInputStream(new byte[0])
            1 * json.encode(_) >> inputStream

        when: "calling the API POST method with additional callbacks"
            api.POST('something', new Resource(), 404: {}, {})

        then: "the HTTP implementation is called with POST and non-null body"
            1 * http.execute({ it.method == 'POST' && it.resource =~ '/something$' && it.body == inputStream })
    }

    def "Default callback"() {
        given: "a success flag"
            boolean ok = false

        and: "an HTTP implementation that calls the API callback"
            1 * http.execute(_) >> { args -> args[0].cb(new META(404, null), null, null) }

        when: "API is invoked with one callback"
            api.request(null, null, null) { ok = true }

        then: "the API invokes that callback"
            ok
    }

    @Unroll
    def "Additional callback #cbname is called for #status"() {
        given: "a success flag"
            boolean ok = false

        and: "an HTTP implementation that calls the API callback"
            1 * http.execute(*_) >> { args -> args[0].cb(new META(status, null), null, null) }

        when: "API is invoked with additional callbacks"
            api.request(null, null, null, (cbname): { ok = true }) {}

        then: "API invokes the correct callback"
            ok

        where:
            status | cbname
            404    | '404'
            302    | 302
            101    | 'info'
            204    | 'success'
            302    | 'redirect'
            405    | 'client_error'
            500    | 'server_error'
            409    | 'error'
            599    | 'error'
    }

    @Unroll
    def "More specific #error is called for #status"() {
        given: "a success flag"
            boolean ok = false

        and: "an HTTP implementation that calls the API callback"
            1 * http.execute(*_) >> { args -> args[0].cb(new META(status, null), null, null) }

        when: "API is invoked with generic error handler and more specific error handler"
            api.request(null, null, null, error: {}, (error): { ok = true }) {}

        then: "API invokes the correct callback"
            ok

        where:
            status | error
            412    | 'client_error'
            596    | 'server_error'
    }

    @Unroll
    def "Additional request callback #cbname is rejected"() {
        when:
            api.request(null, null, null, (cbname): {}) {}

        then:
            thrown(IllegalArgumentException)

        where:
            cbname << [ '000', 9999, 404.5, null, '', true, [foo: 1], (100..199), 'infroooormationaaaaaale' ]
    }

    @Unroll
    def "Global callback #cbname is called for 101"() {
        given: "an HTTP implementation that calls the API callback with 101 status"
            1 * http.execute(*_) >> { args -> args[0].cb(new META(101, null), null, null) }

        and: "a global callback"
            boolean ok = false
            api.callback([ (cbname): { ok = true } ])

        when: "the API is invoked without a callback for 101"
            api.request(null, null, null) {}

        then: "the API invokes the global callback"
            ok

        where:
            cbname << [ '101', 101, 'info' ]
    }

    @Unroll
    def "Global callback for #cbname is not called when overridden"() {
        given: "an HTTP implementation that calls the API callback with status 101"
            1 * http.execute(*_) >> { args -> args[0].cb(new META(101, null), null, null) }

        and: "a global callback"
            boolean ok = false
            api.callback((cbname): {})

        when: "the API is invoked with a callback for 101"
            api.request(null, null, null, (cbname): { ok = true }) {}

        then: "the API invokes the request specific callback"
            ok

        where:
            cbname << [ '101', 'info' ]
    }

    @Unroll
    def "Invalid default callback #cbname is rejected"() {
        when:
            api.callback((cbname): {})

        then:
            thrown(IllegalArgumentException)

        where:
            cbname << ['099', 600, 'foo']
    }

    def "Default request callback with one parameter and error case"() {
        given: "an HTTP implementation that calls the API callback with null META and non-null ERROR"
            1 * http.execute(_) >> { args -> args[0].cb(null, null, new ERROR("foo")) }

        and: "a success flag"
            boolean ok = false

        when: "API is invoked with a default request callback that takes only one argument"
            api.request(null, null, null, null) { meta -> ok = meta == null }

        then: "the default request callback is called with null META"
            ok
    }

    def "Default request callback with two parameters and error case"() {
        given: "an HTTP implementation that calls the API callback with null META and non-null ERROR"
            1 * http.execute(_) >> { args -> args[0].cb(null, null, new ERROR("foo")) }

        and: "a success flag"
            boolean ok = false

        when: "API is invoked with a default request callback that takes two arguments"
            api.request(null, null, null, null) { meta, err -> ok = meta == null && err != null }

        then: "the default request callback is called with null META"
            ok
    }

    def "Default request callback with two parameters and success case"() {
        given: "an HTTP implementation that calls the API callback with non-null META and null ERROR"
            1 * http.execute(_) >> { args -> args[0].cb(new META(200, null), null, null) }

        and: "a success flag"
            boolean ok = false

        when: "API is invoked with a default request callback that takes two arguments"
            api.request(null, null, null, null) { meta, err -> ok = meta != null && err == null }

        then: "the default request callback is called with null META"
            ok
    }

    def "Response body decoding success"() {
        given: "correct headers"
            Headers headers = Mock()
            1 * headers.getAt('Content-Type') >> [
                    // list of HeaderElements; our elements are just maps
                    [name: 'application/json', parameters: [
                            // list of Parameters; our parameters are just maps
                            [name: 'charset', value: 'UTF-8']
                    ]]
            ].iterator()

        and: "an HTTP implementation that calls the API callback with those headers and a non-null entity body"
            1 * http.execute(_) >> { args ->
                args[0].cb(new META(200, headers), new ByteArrayInputStream(new byte[0]), null)
            }

        and: "a JSON implementation that parses the fake input"
            1 * json.decode(_) >> [ server: "ok" ]

        and: "a success flag"
            boolean ok = false

        when: "invoking the API with a callback"
            api.request(null, null, null, null) { resp ->
                ok = resp?.server == "ok"
            }

        then: "the callback is called with the resource whose server property is ok"
            ok
    }

    def "Response body decoding exception"() {
        given: "correct headers"
            Headers headers = Mock()
            1 * headers.getAt('Content-Type') >> [
                    // list of HeaderElements; our elements are just maps
                    [name: 'application/json', parameters: [
                            // list of Parameters; our parameters are just maps
                            [name: 'charset', value: 'UTF-8']
                    ]]
            ].iterator()

        and: "an HTTP implementation that calls the API callback with those headers and a non-null entity body"
            1 * http.execute(_) >> { args ->
                args[0].cb(new META(200, headers), new ByteArrayInputStream(new byte[0]), null)
            }

        and: "a JSON implementation that fails to parse the input"
            1 * json.decode(_) >> {
                // with a clear message, and by explicitly removing the stack trace,
                // I hope the test output is not so unnerving (otherwise I would be tempted to
                // remove the WARN level logging from the real implementation)
                def e = new IllegalStateException("TEST EXCEPTION: THIS IS DELIBERATE: DO NOT PANIC")
                e.stackTrace = new StackTraceElement[0] // zero length array is ok; null was not (NPE)
                throw e
            }

        and: "a success flag"
            boolean ok = false

        when: "invoking the API with a callback"
            api.request(null, null, null, null) { resp ->
                ok = resp != null
            }

        then: "the callback is called with a non-null resource"
            ok
    }

    def "Read method with zero arguments"() {
        when:
            api.GET()

        then:
            thrown(MissingMethodException)
    }

    def "Read method with one argument"() {
        when:
            api.GET({})

        then:
            thrown(MissingMethodException)
    }

    def "Read method with two arguments"() {
        when:
            api.GET 400: {}, {}

        then:
            thrown(MissingMethodException)
    }
}

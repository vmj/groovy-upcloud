package fi.linuxbox.upcloud.api

import spock.lang.*

import fi.linuxbox.upcloud.core.API
import fi.linuxbox.upcloud.core.http.Exchange
import fi.linuxbox.upcloud.core.http.HTTP
import fi.linuxbox.upcloud.core.json.JSON

/**
 * Base class for Server API specifications.
 */
abstract class ServerSpecification extends Specification {

    HTTP http = Mock()
    JSON json = Mock()
    Server server
    Exchange req

    def setup() {
        server = new MockServer(API: new API(http, json, null, null))
        1 * http.execute(_) >> { req = it[0] }
        json.encode(_) >> { new MockInputStream(it[0]) } // at most once
    }

    void requestIs(def method, def resource, def repr = null) {
        assert req?.method == method
        assert req.resource.endsWith(resource)
        assert req.body?.repr == repr
    }
}

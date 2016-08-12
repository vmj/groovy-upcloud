package fi.linuxbox.upcloud.api.spec

import spock.lang.*

import fi.linuxbox.upcloud.api.*
import fi.linuxbox.upcloud.core.http.*
import fi.linuxbox.upcloud.core.json.*

/**
 * Base class for API specifications.
 */
abstract class ApiSpecification extends Specification {

    HTTP http = Mock()
    JSON json = Mock()
    Exchange req

    def setup() {
        (_..1) * http.execute(_) >> { req = it[0] }
        (_..1) * json.encode(_) >> { new MockInputStream(it[0]) }
    }

    void requestIs(def method, def resource, def repr = null) {
        assert req?.method == method
        assert req.resource.endsWith(resource)
        assert req.body?.repr == repr
    }
}

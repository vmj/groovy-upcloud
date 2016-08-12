package fi.linuxbox.upcloud.api

import spock.lang.*

import fi.linuxbox.upcloud.core.*
import fi.linuxbox.upcloud.core.http.*
import fi.linuxbox.upcloud.core.json.*

/**
 * Base class for Storage API specifications.
 */
abstract class StorageSpecification extends Specification {

    HTTP http = Mock()
    JSON json = Mock()
    Storage storage
    Exchange req

    def setup() {
        storage = new MockStorage(API: new API(http, json, null, null))
        1 * http.execute(_) >> { req = it[0] }
        json.encode(_) >> { new MockInputStream(it[0]) } // at most once
    }

    void requestIs(def method, def resource, def repr = null) {
        assert req?.method == method
        assert req.resource.endsWith(resource)
        assert req.body?.repr == repr
    }
}

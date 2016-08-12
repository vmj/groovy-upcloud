package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.*
import fi.linuxbox.upcloud.core.*

/**
 * Base class for Server API specifications.
 */
abstract class ServerSpecification extends ApiSpecification {
    Server server = new MockServer(API: new API(http, json, null, null))
}

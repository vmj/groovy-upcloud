package fi.linuxbox.upcloud.api.spec

import fi.linuxbox.upcloud.api.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

/**
 * Base class for Server API specifications.
 */
abstract class ServerSpecification extends ApiSpecification {
    // build minimal class that works for the Server trait: API and uuid
    Server server = build 'Server', API: api, { uuid = 'fake-uuid' } withTraits Server
}

package fi.linuxbox.upcloud.api.spec

import fi.linuxbox.upcloud.api.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

/**
 * Base class for Server API specifications.
 */
abstract class ServerSpecification extends ApiSpecification {
    // build minimal class that works for the Server trait: SESSION and uuid
    Server server = build 'Server', SESSION: session, { uuid = 'fake-uuid' } withTraits Server
}

package fi.linuxbox.upcloud.api.spec

import fi.linuxbox.upcloud.api.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

/**
 * Base class for Storage API specifications.
 */
abstract class StorageSpecification extends ApiSpecification {
    // build minimal class that works for the Storage trait: SESSION and uuid
    Storage storage = build 'Storage', SESSION: session, { uuid = 'fake-uuid' } withTraits Storage
}

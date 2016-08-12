package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.api.spec.*
import fi.linuxbox.upcloud.core.*
/**
 * Base class for Storage API specifications.
 */
abstract class StorageSpecification extends ApiSpecification {
    Storage storage = new MockStorage(API: new API(http, json, null, null))
}

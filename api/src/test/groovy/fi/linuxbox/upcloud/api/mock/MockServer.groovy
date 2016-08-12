package fi.linuxbox.upcloud.api.mock

import fi.linuxbox.upcloud.api.Server
import fi.linuxbox.upcloud.core.*

/**
 *
 */
class MockServer extends Resource implements Server {
    String uuid = "fake-uuid"

    MockServer(final Map kwargs = [ : ]) {
        super(kwargs)
    }
}

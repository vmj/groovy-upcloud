package fi.linuxbox.upcloud.api

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

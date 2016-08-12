package fi.linuxbox.upcloud.api.mock

import fi.linuxbox.upcloud.api.Storage
import fi.linuxbox.upcloud.core.*

class MockStorage extends Resource implements Storage {
    String uuid = "fake-uuid"

    MockStorage(final Map kwargs = [ : ]) {
        super(kwargs)
    }
}

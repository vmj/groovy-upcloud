package fi.linuxbox.upcloud.api.mock

import fi.linuxbox.upcloud.api.Tag
import fi.linuxbox.upcloud.core.*

class MockTag extends Resource implements Tag {
    String name = "DEV"

    MockTag(final Map kwargs = [ : ]) {
        super(kwargs)
    }
}

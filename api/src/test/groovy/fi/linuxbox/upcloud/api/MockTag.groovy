package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.core.*

class MockTag extends Resource implements Tag {
    String name = "DEV"

    MockTag(final Map kwargs = [ : ]) {
        super(kwargs)
    }
}

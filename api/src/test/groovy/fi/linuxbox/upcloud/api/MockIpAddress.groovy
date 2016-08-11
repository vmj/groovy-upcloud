package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.core.*

class MockIpAddress extends Resource implements IpAddress {
    String address = "0.0.0.0"

    MockIpAddress(final Map kwargs = [ : ]) {
        super(kwargs)
    }
}

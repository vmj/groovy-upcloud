package fi.linuxbox.upcloud.http.ahc

import spock.lang.Specification

class AhcClietProviderSpec extends Specification {

    def "test"() {
        expect:
            new AhcClientProvider().get() != null
    }
}

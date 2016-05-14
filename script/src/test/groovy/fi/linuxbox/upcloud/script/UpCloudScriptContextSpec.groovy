package fi.linuxbox.upcloud.script

import spock.lang.*

class UpCloudScriptContextSpec extends Specification {

    def "Smoke test"() {
        when:
            new UpCloudScriptContext("foo", "bar")

        then:
            noExceptionThrown()
    }
}

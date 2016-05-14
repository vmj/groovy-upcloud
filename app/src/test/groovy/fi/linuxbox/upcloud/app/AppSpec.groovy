package fi.linuxbox.upcloud.app

import spock.lang.*
/**
 *
 */
class AppSpec extends Specification {

    def "Initialization"() {
        when:
            def app = new App(new String[2])

        then:
            app.isRunning()
    }

    def "Close"() {
        given:
            def app = new App(new String[2])

        when:
            app.close()

        then:
            !app.isRunning()
    }

    def "Wrong initialization"() {
        when:
            def app = new App()

        then:
            !app.isRunning()
    }
}

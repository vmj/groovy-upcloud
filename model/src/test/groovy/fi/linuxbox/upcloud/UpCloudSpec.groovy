package fi.linuxbox.upcloud

import fi.linuxbox.upcloud.core.API
import spock.lang.Ignore
import spock.lang.Specification

/**
 *
 */
class UpCloudSpec extends Specification {



    @Ignore("This is not implemented, yet")
    def "basics"() {
        given:
        UpCloud cloud1 = new UpCloud(API: new API())
        UpCloud cloud2 = new UpCloud(API: new API())


        when:
        cloud1.account error: { println "error cb" }, foo: { println "foo cb"}, { println "default cb"}
        cloud1.account { println "only cb" }
        cloud2.account { println "only cb" }

        then:
        noExceptionThrown()
    }
}

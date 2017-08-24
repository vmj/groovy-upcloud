import spock.lang.Specification

class AccountSpec extends Specification {

    def "scripting test"() {
        given:
            StringWriter content = new StringWriter()
            GroovyShell shell = new GroovyShell(new Binding(out: new PrintWriter(content)))

        when:
            shell.evaluate("""
import fi.linuxbox.upcloud.script.*

import java.util.concurrent.*
import static java.util.concurrent.TimeUnit.*
def cv = new CountDownLatch(1)

def ctx = new UpCloudScriptContext("foo", "bar")
ctx.upCloud.account(
        success: { response ->
                println "\${response.account.username}: \${response.account.credits}"
                cv.countDown()
        },
        { response, err ->
                if (err) {
                        println "Network error: \${err.message}"
                        if (err.cause)
                                err.cause.printStackTrace()
                } else {
                        println "HTTP status: \${response.META.status}: \${response.META.message}"
                        if (response.error)
                            println "Detailed error: \${response.error.errorCode}: \${response.error.errorMessage}"
                }
                cv.countDown()
        })

cv.await(30, SECONDS)
ctx.close()
""")

        then:
            content.toString() == """HTTP status: 401: Authorization Required
Detailed error: AUTHENTICATION_FAILED: Authentication failed using the given username and password.
"""
    }
}

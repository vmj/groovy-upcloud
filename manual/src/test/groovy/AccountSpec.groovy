import fi.linuxbox.upcloud.script.UpCloudScript
import org.codehaus.groovy.control.CompilerConfiguration
import spock.lang.Specification

class AccountSpec extends Specification {

    def "scripting test"() {
        given: "a compiler configuration that customizes the script class"
            def config = new CompilerConfiguration()
            config.scriptBaseClass = UpCloudScript.name

        and: "a groovy shell with that config (also capturing System.out)"
            def content = new StringWriter()
            def shell = new GroovyShell(new Binding(out: new PrintWriter(content)), config)

        when:
            shell.evaluate("""
import fi.linuxbox.upcloud.api.UpCloud

def session = newSession("foo", "bar")
def upCloud = new UpCloud(session)

// Add a session wide auth error callback
session.callback(401: { resp ->
    assert resp.META.status == 401
    assert resp.META.message == 'Authorization Required'
    assert resp.error.errorCode == 'AUTHENTICATION_FAILED'
    assert resp.error.errorMessage == 'Authentication failed using the given username and password.'
    println "Got auth error as expected"
    close()
})

upCloud.account(
    success: { resp ->
        println "Unexpectedly got user info: \${resp.account}"
        close()
    },
    { resp, err ->
        if (err) {
            println "Unexpected network error: \${err.message}"
            if (err.cause)
                err.cause.printStackTrace()
        } else {
            println "Unexpected HTTP status: \${resp?.META}"
            println "Detailed error: \${resp?.error}"
        }
        close()
     })
""")

        then:
            content.toString() == "Got auth error as expected\n"
    }
}

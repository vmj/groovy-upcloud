import spock.lang.Ignore
import spock.lang.Specification

class AccountSpec extends Specification {

    def ctx

    def setup() {
        // ctx = new
    }

    @Ignore
    def "test"() {
        expect:
            ctx.upCloud.account { resp ->
                cond.evaluate {
                    assert resp.META.status == 200
                    def account = resp.account
                    assert account.class.simpleName == 'Account'
                    assert account.username != null
                }
            }
    }
}

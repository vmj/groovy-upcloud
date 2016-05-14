package fi.linuxbox.upcloud.api

import spock.lang.*

import fi.linuxbox.upcloud.core.*
import fi.linuxbox.upcloud.core.http.*
import fi.linuxbox.upcloud.core.json.*

/**
 *
 */
class UpCloudSpec extends Specification {

    HTTP http = Mock()
    JSON json = Mock()

    UpCloud upCloud

    def setup() {
        upCloud = new UpCloud(new API(http, json, "foo", "bar"))
    }

    def "storages does GET .../storage"() {
        given:
            boolean ok = false
            1 * http.execute({ it.method == 'GET' && it.resource.endsWith('/storage') }) >> { ok = true }

        when:
            upCloud.storages {}

        then:
            ok
    }

    def "storages type: 'favorite' does GET .../storage/favorite"() {
        given:
            boolean ok = false
            1 * http.execute({ it.resource.endsWith('/storage/favorite') }) >> { ok = true }

        when:
            upCloud.storages type: 'favorite', {}

        then:
            ok
    }

    @Unroll
    def "#methodName does GET .../#resource"() {
        given:
            boolean ok = false
            1 * http.execute({ it.method == 'GET' && it.resource.endsWith("/$resource") }) >> { ok = true }

        when:
            upCloud."$methodName" {}

        then:
            ok

        where:
            methodName    | resource
            'account'     | 'account'
            'prices'      | 'price'
            'zones'       | 'zone'
            'timezones'   | 'timezone'
            'serverSizes' | 'server_size'
            'servers'     | 'server'
            'ipAddresses' | 'ip_address'

    }
}

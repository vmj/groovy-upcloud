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

    def "storages type: 'favorite' does GET .../storage/favorite"() {
        given: "a mock HTTP implementation that saves the given request"
            def req = null
            1 * http.execute(_) >> { req = it[0] }

        when: "UpCloud API is invoked"
            upCloud.storages type: 'favorite', {}

        then: "expected HTTP request is generated"
            req?.method == 'GET'
            req?.resource.endsWith('/storage/favorite')
    }

    @Unroll
    def "#methodName does GET ...#resource"() {
        given: "a mock HTTP implementation that saves the given request"
            def req = null
            1 * http.execute(_) >> { req = it[0] }

        when: "UpCloud API is invoked"
            upCloud."$methodName" {}

        then: "expected HTTP request is generated"
            req?.method == 'GET'
            req.resource.endsWith(resource)

        where:
            methodName    | resource
            'account'     | '/account'
            'prices'      | '/price'
            'zones'       | '/zone'
            'timezones'   | '/timezone'
            'plans'       | '/plan'
            'serverSizes' | '/server_size'
            'servers'     | '/server'
            'ipAddresses' | '/ip_address'
            'storages'    | '/storage'
            'tags'        | '/tag'
    }
}

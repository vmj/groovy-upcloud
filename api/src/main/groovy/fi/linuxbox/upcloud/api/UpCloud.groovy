package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.core.API
import org.slf4j.*

import javax.inject.Inject

/**
 *
 */
class UpCloud {
    private final Logger log = LoggerFactory.getLogger(UpCloud)

    private final API API

    @Inject
    UpCloud(final API API) {
        this.API = API
    }

    private static final Map<String, String> resources = [
            account     : 'account'     ,
            prices      : 'price'       ,
            zones       : 'zone'        ,
            timezones   : 'timezone'    ,
            serverSizes : 'server_size' ,
            servers     : 'server'      ,
            ipAddresses : 'ip_address'  ,
    ]

    def methodMissing(final String name, final def args) {
        def resource = resources[name]
        if (resource)
            return this.API.GET(resource, *args)
        throw new MissingMethodException(name, UpCloud, args)
    }

    def storages(Map kwargs = [:], Closure cb) {
        def type = kwargs?.remove('type')
        if (type)
            this.API.GET("storage/$type", kwargs, cb)
        else
            this.API.GET("storage", kwargs, cb)
    }
}

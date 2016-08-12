package fi.linuxbox.upcloud.api

import javax.inject.*

import fi.linuxbox.upcloud.core.*

/**
 * Top-level APIs.
 * <p>
 *     This class provides following APIs:
 * </p>
 * <ul>
 *     <li>getting account information</li>
 *     <li>listing prices, zones, timezones, plans, and server sizes</li>
 *     <li>listing servers, IP addresses, tags, and storages</li>
 * </ul>
 */
class UpCloud {
    private final API API

    @Inject
    UpCloud(final API API) {
        this.API = API
    }

    def account(...args) {
        this.API.GET('account', *args)
    }

    def prices(...args) {
        this.API.GET('price', *args)
    }

    def zones(...args) {
        this.API.GET('zone', *args)
    }

    def timezones(...args) {
        this.API.GET('timezone', *args)
    }

    def plans(...args) {
        this.API.GET('plan', *args)
    }

    def serverSizes(...args) {
        this.API.GET('server_size', *args)
    }

    def servers(...args) {
        this.API.GET('server', *args)
    }

    def ipAddresses(...args) {
        this.API.GET('ip_address', *args)
    }

    def tags(...args) {
        this.API.GET('tag', *args)
    }

    def storages(Map kwargs = [:], Closure cb) {
        def type = kwargs?.remove('type')
        if (type)
            this.API.GET("storage/$type", kwargs, cb)
        else
            this.API.GET("storage", kwargs, cb)
    }
}

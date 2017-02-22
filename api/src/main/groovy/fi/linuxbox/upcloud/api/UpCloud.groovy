package fi.linuxbox.upcloud.api

import groovy.transform.PackageScope
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
 *     <li>listing and creating servers, IP addresses, tags, and storages</li>
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

    def storages(...args) {
        def type = args.find { it instanceof Map } ?.remove('type')
        if (type)
            this.API.GET("storage/$type", *args)
        else
            this.API.GET("storage", *args)
    }

    def create(Resource resource, ...args) {
        this.API.POST(url_path_segment(resource.class.simpleName), resource.wrapper(), *args)
    }

    /**
     * Converts a class name to a URL path segment.
     *
     * <p>
     * For example, 'IpAddress' becomes 'ip_address'.
     * </p>
     *
     * @param className Simple name of a class, i.e. name without the package.
     * @return URL path style segment.
     */
    @PackageScope
    static String url_path_segment(final String className) {
        className.replaceAll(/([A-Z])([A-Z]+)/, { it[1] + it[2].toLowerCase() }) // RESOURCE -> Resource
                .replaceFirst(/^([A-Z])/, { it[0].toLowerCase() }) // Server -> server
                .replaceAll(/([A-Z])/, { '_' + it[0].toLowerCase() }) // storageDevice -> storage_device
    }
}

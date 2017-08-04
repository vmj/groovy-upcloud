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
    private final Session SESSION

    @Inject
    UpCloud(final Session SESSION) {
        this.SESSION = SESSION
    }

    def account(...args) {
        this.SESSION.GET('account', *args)
    }

    def prices(...args) {
        this.SESSION.GET('price', *args)
    }

    def zones(...args) {
        this.SESSION.GET('zone', *args)
    }

    def timezones(...args) {
        this.SESSION.GET('timezone', *args)
    }

    def plans(...args) {
        this.SESSION.GET('plan', *args)
    }

    def serverSizes(...args) {
        this.SESSION.GET('server_size', *args)
    }

    def servers(...args) {
        this.SESSION.GET('server', *args)
    }

    def ipAddresses(...args) {
        this.SESSION.GET('ip_address', *args)
    }

    def tags(...args) {
        this.SESSION.GET('tag', *args)
    }

    def storages(...args) {
        def type = args.find { it instanceof Map } ?.remove('type')
        if (type)
            this.SESSION.GET("storage/$type", *args)
        else
            this.SESSION.GET("storage", *args)
    }

    def create(Resource resource, ...args) {
        this.SESSION.POST(url_path_segment(resource.class.simpleName), resource.wrapper(), *args)
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

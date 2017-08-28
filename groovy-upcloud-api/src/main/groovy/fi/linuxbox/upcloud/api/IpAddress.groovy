package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.core.*

/**
 * IP address related APIs.
 * <p>
 *     This class provides following APIs:
 * </p>
 * <lu>
 *     <li>loading, modifying, and deleting IP addresses</li>
 * </ul>
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null SESSION property, which can be read-only</li>
 *     <li>non-null address property, which can be read-only</li>
 * </ul>
 */
trait IpAddress {

    def load(...args) {
        this.SESSION.GET(ipAddressPath(), *args)
    }

    def update(Resource resource, ...args) {
        this.SESSION.PUT(ipAddressPath(), resource.wrapper(), *args)
    }

    def delete(...args) {
        this.SESSION.DELETE(ipAddressPath(), *args)
    }

    private String ipAddressPath() { "ip_address/$address" }
}

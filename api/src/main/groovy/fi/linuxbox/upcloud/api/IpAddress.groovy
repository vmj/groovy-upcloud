package fi.linuxbox.upcloud.api

import groovy.transform.*

import fi.linuxbox.upcloud.core.*

/**
 * IP address related APIs.
 * <p>
 *     This class provides following APIs:
 * </p>
 * <lu>
 *     <li>modifying and deleting tags</li>
 * </ul>
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null API property, which can be read-only</li>
 *     <li>non-null address property, which can be read-only</li>
 * </ul>
 */
@SelfType(Resource) // must have address property
trait IpAddress {

    def load(...args) {
        this.API.GET(ipAddressPath(), *args)
    }

    def update(Resource resource, ...args) {
        this.API.PUT(ipAddressPath(), resource.wrapper(), *args)
    }

    def delete(...args) {
        this.API.DELETE(ipAddressPath(), *args)
    }

    private String ipAddressPath() { "ip_address/$address" }
}

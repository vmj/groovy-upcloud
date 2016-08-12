package fi.linuxbox.upcloud.api

import groovy.transform.*

import fi.linuxbox.upcloud.core.*

/**
 * IP address related APIs.
 * <p>
 *     This class provides following APIs:
 * </p>
 * <lu>
 *     <li>creating, modifying, and deleting tags</li>
 * </ul>
 */
@SelfType(Resource) // must have address property
trait IpAddress {

    def create(...args) {
        this.API.POST(ipAddressesPath(), wrapper(), *args)
    }

    def load(...args) {
        this.API.GET(ipAddressPath(), *args)
    }

    def update(...args) {
        this.API.PUT(ipAddressPath(), wrapper(), *args)  // FIXME: update issue: ip address required for URL, but should not be in PUT repr
    }

    def delete(...args) {
        this.API.DELETE(ipAddressPath(), *args)
    }

    private String ipAddressesPath() { 'ip_address/' }
    private String ipAddressPath() { "${ipAddressesPath()}$address" }
}

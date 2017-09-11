package fi.linuxbox.upcloud.resource

import groovy.transform.InheritConstructors

import fi.linuxbox.upcloud.core.*

/**
 * IP address of a {@link Server}.
 * <p>
 *     A list of these is available in the {@link Server#ipAddresses} property when fetching the detailed server
 *     information from the {@link fi.linuxbox.upcloud.api.Server#load(def)} API.
 * </p>
 */
@InheritConstructors
class IpAddress extends Resource {
    /**
     * Whether this IP address is for {@code public} or {@code private} network.
     * <p>
     *     {@code IPv6} addresses can not be private.
     * </p>
     */
    String access
    /**
     * String representation of this IP address.
     * <p>
     *     Either in dotted decimal notation (for {@code IPv4} {@link #family}),
     *     or hexadecimal representation (for {@code IPv6} {@link #family}).
     * </p>
     * <p>
     *     This is available in the server details response.  This can not be set when creating IP addresses for a
     *     new server.
     * </p>
     */
    String address
    /**
     * Address family of this IP address, either {@code IPv4} or {@code IPv6}.
     * <p>
     *     {@code IPv6} addresses can not be private.
     * </p>
     */
    String family
}

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
     * Whether this IP address is {@code public} or {@code private}.
     */
    String access
    /**
     * String representation of this IP address.
     * <p>
     *     Either in dotted decimal notation (for {@code IPv4} {@link #family}),
     *     or hexadecimal representation (for {@code IPv6} {@link #family}).
     * </p>
     */
    String address
    /**
     * Address family of this IP address, either {@code IPv4} or {@code IPv6}.
     */
    String family
}

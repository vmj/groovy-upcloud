/*
 * Groovy UpCloud library - Resource Module
 * Copyright (C) 2018  Mikko Värri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fi.linuxbox.upcloud.resource

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import fi.linuxbox.upcloud.core.*

/**
 * IP address of a {@link Server}.
 * <p>
 *     The UpCloud network has public, private, and utility IP addresses.  Public addresses are exposed to the
 *     Internet, and utility addresses are only visible to servers under the same UpCloud account.
 *     TODO: describe private addresses.
 * </p>
 * <p>
 *     When a server is created, UpCloud will assign one {@code utility} {@code IPv4} address to the server.
 *     That address can not be released, except by deleting the server, and additional {@code utility} addresses
 *     can not be added.
 * </p>
 * <p>
 *     In addition to the {@code utility} address, one server can have 1-5 {@code public} addresses assigned to it.
 *     These can be {@code IPv4} or {@code IPv6} addresses.
 * </p>
 * <p>
 *     TODO: describe floating private addresses.
 * </p>
 *
 * <h4>Listing IP addresses</h4>
 * <p>
 *     A list of all IP addresses associated with an UpCloud API user account is available via
 *     {@link fi.linuxbox.upcloud.api.UpCloudApi#ipAddresses(def) IP Addresses} API.
 * </p>
 * <p>
 *     A list of IP addresses associated with a specific server is available in the {@link Server#ipAddresses}
 *     property when using the {@link fi.linuxbox.upcloud.api.ServerApi#load(def) Server details} API.
 * </p>
 *
 * <h4>Creating IP addresses</h4>
 * <p>
 *     When creating a server, a list of IP addresses can be specified in the {@link Server#ipAddresses} property.
 *     The list can not be empty.
 * </p>
 * <pre><code class="groovy">
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def newServer = server {
 *         // skipping mandatory properties and showing only
 *         // optional ipAddresses property:
 *         ipAddresses = [
 *              ipAddress {
 *                  access = 'public' // mandatory
 *                  family = 'IPv4' // mandatory
 *                  // partOfPlan not allowed
 *                  // ptrRecord not allowed???
 *                  // server not allowed
 *              },
 *              ipAddress {
 *                  access = 'public'
 *                  family = 'IPv6'
 *              }
 *         ]
 *     }
 *
 *     upcloud.create newServer { resp, err ->
 *         ...
 *     }
 * </code></pre>
 * <p>
 *     After server creation, additional IP addresses can be assigned to a server.  Only {@link #family} and
 *     {@link #server} properties are allowed here, since additional {@code private} addresses can not be added.
 *     TODO: is this still true?
 * </p>
 * <p>
 *     Note that the server must be stopped while adding IP addresses.
 * </p>
 * <pre><code class="groovy">
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def additionalAddress = ipAddress {
 *         // access not allowed, since only public addresses can be added
 *         family = 'IPv4' // mandatory
 *         // partOfPlan not allowed
 *         // ptrRecord not allowed???
 *         server = existingServer.uuid // mandatory
 *     }
 *
 *     upcloud.create additionalAddress { resp, err ->
 *         ...
 *     }
 * </code></pre>
 *
 * <h4>Modifying IP addresses</h4>
 * <p>
 *     The only property of an IP address that can be modified is the reverse DNS PTR record.
 * </p>
 * <pre><code class="groovy">
 *     import fi.linuxbox.upcloud.api.IpAddressApi
 *     import static fi.linuxbox.upcloud.resource.Builder.ipAddress
 *
 *     def ptrRecord = ipAddress {
 *         ptrRecord = 'hostname.example.com'
 *     }
 *
 *     def ipAddressApi = someIpAddress.withTraits(IpAddressApi)
 *
 *     ipAddressApi.update ptrRecord { resp, err ->
 *         ...
 *     }
 * </code></pre>
 *
 * <h4>Deleting IP addresses</h4>
 * <p>
 *     While IP addresses of a server are released automatically when a server is deleted, IP addresses can also be
 *     released one by one (from whatever server they were assigned to).
 * </p>
 * <pre><code class="groovy">
 *     import fi.linuxbox.upcloud.api.IpAddressApi
 *     import static fi.linuxbox.upcloud.resource.Builder.ipAddress
 *
 *     def unneededAddress = ipAddress HTTP: session {
 *         address = '10.0.0.1'
 *     }
 *
 *     def ipAddressApi = unneededAddress.withTraits(IpAddressApi)
 *
 *     ipAddressApi.delete { resp, err ->
 *         assert !err // resp contains headers but not much more
 *     }
 * </code></pre>
 */
@CompileStatic
@InheritConstructors
class IpAddress extends Resource {
    /**
     * Whether this IP address is for {@code public} or {@code private} network.
     * <p>
     *     {@code IPv6} addresses can not be private.
     * </p>
     * <p>
     *     This is available in all IP address related responses.  This must be set when creating an IP address as
     *     part of server creation.  After server creation, this can not be specified when creating additional IP
     *     addresses or modifying them.
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
     *     This is available in all IP address related responses.  This can not be set when creating or modifying IP
     *     addresses, since UpCloud assigns the addresses automatically.  This can be used as an identifier, but
     *     note that IP addresses are reused after released.
     * </p>
     */
    String address
    /**
     * Address family of this IP address, either {@code IPv4} or {@code IPv6}.
     * <p>
     *     {@code IPv6} addresses can not be private.
     * </p>
     * <p>
     *     This is available in all IP address related responses.  This must be set when creating an IP address,
     *     either as part of server creation or when assigning an additional new address to an existing server.
     *     This can not be modified after the creation.
     * </p>
     */
    String family
    /**
     * Whether this IP address is included in fixed plan price of the server ({@code yes}).
     * <p>
     *     This is available when listing all IP addresses associated with an UpCloud API account, and when fetching
     *     details of a single IP address.  This can not be set from client side.
     * </p>
     */
    String partOfPlan
    /**
     * The reverse DNS PTR record for this IP address.
     * <p>
     *     Only {@code public} IP addresses can have a reverse DNS PTR record.
     * </p>
     * <p>
     *     This is available when listing all IP addresses associated with an UpCloud API account, and when fetching
     *     details of a single IP address.  This is the only property that can be modified.
     * </p>
     */
    String ptrRecord
    /**
     * The unique identifier of the server to which this IP address is assigned.
     * <p>
     *     This is available when listing all IP addresses associated with an UpCloud API account, and when fetching
     *     details of a single IP address.  This must be specified when assigning an additional new address to an
     *     existing server.  This is not available in the server details response, and this can not be specified
     *     when creating an IP address as part of creating a server, or when modifying an IP address.
     * </p>
     */
    String server
}

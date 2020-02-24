/*
 * Groovy UpCloud library - API Module
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
package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.core.*

import static fi.linuxbox.upcloud.core.ResourceUtil.wrapped

/**
 * IP address related APIs.
 * <p>
 *     This trait provides APIs for
 *     {@link #load([Ljava.lang.Object;) loading},
 *     {@link #update(Resource,[Ljava.lang.Object;) modifying}, and
 *     {@link #delete([Ljava.lang.Object;) deleting}
 *     IP addresses.
 *     For listing all IP addresses associated with the UpCloud account, see
 *     {@link fi.linuxbox.upcloud.api.UpCloudApi#ipAddresses([Ljava.lang.Object;) UpCloudApi::ipAddresses} method documentation.
 * </p>
 * <p>
 *     To obtain an instance of this trait, one would typically take an instance of
 *     {@link fi.linuxbox.upcloud.resource.IpAddress}
 * </p>
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null HTTP property, which can be read-only</li>
 *     <li>non-null address property, which can be read-only</li>
 * </ul>
 */
trait IpAddressApi {
    abstract HTTPFacade<?> getHTTP()
    abstract String getAddress()

    /**
     * Fetch detailed information about a specific {@link fi.linuxbox.upcloud.resource.IpAddress}.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.IpAddress}
     *     in the {@code ipAddress} property.
     * </p>
     * <pre><code class="groovy">
     *     ipAddressApi.load { resp, err ->
     *         assert resp?.ipAddress instanceof IpAddress
     *     }
     * </code></pre>
     *
     * @param args Request callbacks for the {@code GET /ip_address/&#36;&#123;ipAddress.address&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/10-ip-addresses/#get-ip-address-details" target="_top">UpCloud API docs for GET /ip_address/&#36;{ipAddress.address}</a>
     */
    def load(...args) {
        HTTP.GET(ipAddressPath(), *args)
    }

    /**
     * Modifies the reverse DNS PTR record corresponding to an IP address.
     * <p>
     *     A {@code 202 Accepted} response will include an instance of {@link fi.linuxbox.upcloud.resource.IpAddress}
     *     in the {@code ipAddress} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.*
     *
     *     def ptrRecord = ipAddress {
     *         ptrRecord = "hostname.example.com"
     *     }
     *
     *     ipAddressApi.update ptrRecord { resp, err ->
     *         assert resp?.ipAddress instanceof IpAddress
     *     }
     * </code></pre>
     *
     * @param resource Specification of the update
     * @param args Request callbacks for the {@code PUT /ip_address/&#36;&#123;ipAddress.address&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/10-ip-addresses/#modify-ip-address" target="_top">UpCloud API docs for PUT /ip_address/&#36;{ipAddress.address}</a>
     */
    def update(Resource resource, ...args) {
        HTTP.PUT(ipAddressPath(), wrapped(resource), *args)
    }

    /**
     * Removes an IP address from a server.
     * <p>
     *     A {@code 204 No Content} response signifies success.
     * </p>
     * @param args Request callbacks for the {@code DELETE /ip_address/&#36;&#123;ipAddress.address&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/10-ip-addresses/#release-ip-address" target="_top">UpCloud API docs for DELETE /ip_address/&#36;{ipAddress.address}</a>
     */
    def delete(...args) {
        HTTP.DELETE(ipAddressPath(), *args)
    }

    private String ipAddressPath() { "ip_address/$address" }
}

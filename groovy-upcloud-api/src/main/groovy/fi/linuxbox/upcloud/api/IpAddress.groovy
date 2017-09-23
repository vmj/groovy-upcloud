package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.core.*

/**
 * IP address related APIs.
 * <p>
 *     This class provides following APIs:
 * </p>
 * <ul>
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
     * @see <a href="https://www.upcloud.com/api/1.2.4/10-ip-addresses/#get-ip-address-details" target="_top">UpCloud API docs for GET /ip_address/&#36;{ipAddress.address}</a>
     */
    def load(...args) {
        this.SESSION.GET(ipAddressPath(), *args)
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
     * @see <a href="https://www.upcloud.com/api/1.2.4/10-ip-addresses/#modify-ip-address" target="_top">UpCloud API docs for PUT /ip_address/&#36;{ipAddress.address}</a>
     */
    def update(Resource resource, ...args) {
        this.SESSION.PUT(ipAddressPath(), resource.wrapper(), *args)
    }

    /**
     * Removes an IP address from a server.
     * <p>
     *     A {@code 204 No Content} response signifies success.
     * </p>
     * @param args Request callbacks for the {@code DELETE /ip_address/&#36;&#123;ipAddress.address&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/10-ip-addresses/#release-ip-address" target="_top">UpCloud API docs for DELETE /ip_address/&#36;{ipAddress.address}</a>
     */
    def delete(...args) {
        this.SESSION.DELETE(ipAddressPath(), *args)
    }

    private String ipAddressPath() { "ip_address/$address" }
}

package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.core.HTTPFacade
import fi.linuxbox.upcloud.core.Resource

import static fi.linuxbox.upcloud.core.ResourceUtil.wrapped

/**
 * Private cloud host related APIs.
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null HTTP property, which can be read-only</li>
 *     <li>non-null id property, which can be read-only</li>
 * </ul>
 */
trait HostApi {
    abstract HTTPFacade<?> getHTTP()
    abstract Long getId()

    /**
     * Modifies the description of a private cloud host.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Host}
     *     in the {@code host} property.
     * </p>
     * <pre><code class="groovy">
     *     import fi.linuxbox.upcloud.api.HostApi
     *     import fi.linuxbox.upcloud.resource.Host
     *     import static fi.linuxbox.upcloud.resource.Builder.host
     *
     *     // Obtain HostApi somehow.  For example:
     *     final hostApi = [HTTP: session, id: 123] as HostApi
     *
     *     // Configure the new host (only description can be changed)
     *     final description = host {
     *         description = "My New Host"
     *     }
     *
     *     // Update the host description
     *     hostApi.update description, { resp, err ->
     *         assert resp?.host instanceof Host
     *     }
     * </code></pre>
     *
     * @param resource Specification of the update
     * @param args Request callbacks for the {@code PATCH /host/&#36;&#123;host.id&#125;} call.
     * @return Whatever is returned by the {@link fi.linuxbox.upcloud.core.Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/14-hosts/#modify-host-details" target="_top">UpCloud API docs for PATCH /host/&#36;{host.id}</a>
     */
    def update(Resource resource, ...args) {
        HTTP.PATCH(hostPath(), wrapped(resource), *args)
    }


    private String hostPath() { "host/$id" }
}

/*
 * Groovy UpCloud library - API Module
 * Copyright (C) 2017  <mikko@varri.fi>
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
    private final AbstractSession SESSION

    @Inject
    UpCloud(final Session SESSION) {
        this.SESSION = SESSION
    }

    /**
     * Fetch UpCloud API account information for the session.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Account} in the
     *     {@code account} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.account { resp, err ->
     *         assert resp?.account instanceof Account
     *     }
     * </code></pre>
     *
     * @param args Request callbacks for the {@code GET /account} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/3-accounts/#get-account-information" target="_top">UpCloud API docs for GET /account</a>
     */
    def account(...args) {
        this.SESSION.GET('account', *args)
    }

    /**
     * Fetch UpCloud prices for all zones.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.Zone} instances in
     *     the {@code prices} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.prices { resp, err ->
     *         assert resp?.prices instanceof List
     *         assert rest.prices.every { it instanceof Zone }
     *     }
     * </code></pre>
     * <p>
     *     When zones are listed via this API, they will <b>not</b> include
     *     {@link fi.linuxbox.upcloud.resource.Zone#id} properties, but {@link fi.linuxbox.upcloud.resource.Zone#name}
     *     properties instead.  They are the same.
     * </p>
     *
     * @param args Request callbacks for the {@code GET /price} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/4-pricing/" target="_top">UpCloud API docs for GET /price</a>
     */
    def prices(...args) {
        this.SESSION.GET('price', *args)
    }

    /**
     * Fetch UpCloud zone list.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.Zone} instances in
     *     the {@code zones} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.zones { resp, err ->
     *         assert resp?.zones instanceof List
     *         assert rest.zones.every { it instanceof Zone }
     *     }
     * </code></pre>
     * <p>
     *     When zones are listed via this API, they will <b>not</b> include
     *     {@link fi.linuxbox.upcloud.resource.Zone#name} properties, but {@link fi.linuxbox.upcloud.resource.Zone#id}
     *     properties instead.  They are the same.
     * </p>
     *
     * @param args Request callbacks for the {@code GET /zone} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/5-zones/" target="_top">UpCloud API docs for GET /zone</a>
     */
    def zones(...args) {
        this.SESSION.GET('zone', *args)
    }

    /**
     * Fetch a list of available timezones.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link String}s in the {@code timezones} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.timezones { resp, err ->
     *         assert resp?.timezones instanceof List
     *         assert rest.timezones.every { it instanceof String }
     *     }
     * </code></pre>
     *
     * @param args Request callbacks for the {@code GET /timezone} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/6-timezones/" target="_top">UpCloud API docs for GET /timezone</a>
     */
    def timezones(...args) {
        this.SESSION.GET('timezone', *args)
    }

    /**
     * Fetch a list of available plans.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.Plan} instances in
     *     the {@code plans} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.plans { resp, err ->
     *         assert resp?.plans instanceof List
     *         assert rest.plans.every { it instanceof Plan }
     *     }
     * </code></pre>
     *
     * @param args Request callbacks for the {@code GET /plan} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/7-plans/" target="_top">UpCloud API docs for GET /plan</a>
     */
    def plans(...args) {
        this.SESSION.GET('plan', *args)
    }

    /**
     * Fetch a list of available server configurations.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.ServerSize} instances
     *     in the {@code serverSizes} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.serverSizes { resp, err ->
     *         assert resp?.serverSizes instanceof List
     *         assert rest.serverSizes.every { it instanceof ServerSize }
     *     }
     * </code></pre>
     *
     * @param args Request callbacks for the {@code GET /server_size} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#list-server-configurations" target="_top">UpCloud API docs for GET /server_size</a>
     */
    def serverSizes(...args) {
        this.SESSION.GET('server_size', *args)
    }

    /**
     * Fetch a list of all servers associated with this session.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.Server} instances
     *     in the {@code servers} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.servers { resp, err ->
     *         assert resp?.servers instanceof List
     *         assert rest.servers.every { it instanceof Server }
     *     }
     * </code></pre>
     * <p>
     *     Only the servers' most relevant information is returned by this operation. Further details on individual
     *     servers can be requested with the {@link Server#load(def)} API.
     * </p>
     *
     * @param args Request callbacks for the {@code GET /server} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#list-servers" target="_top">UpCloud API docs for GET /server</a>
     */
    def servers(...args) {
        this.SESSION.GET('server', *args)
    }

    /**
     * Fetch a list of all IP addresses associated with this session.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.IpAddress} instances
     *     in the {@code ipAddresses} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.ipAddresses { resp, err ->
     *         assert resp?.ipAddresses instanceof List
     *         assert rest.ipAddresses.every { it instanceof IpAddress }
     *     }
     * </code></pre>
     * @param args Request callbacks for the {@code GET /ip_address} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/10-ip-addresses/#list-ip-addresses" target="_top">UpCloud API docs for GET /ip_address</a>
     */
    def ipAddresses(...args) {
        this.SESSION.GET('ip_address', *args)
    }

    /**
     * Fetch a list of all tags associated with this account.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.Tag} instances in the
     *     {@code tags} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.tags { resp, err ->
     *         assert resp?.tags instanceof List
     *         assert resp.tags.every { it instanceof Tag }
     *     }
     * </code></pre>
     * @param args Request callbacks for the {@code GET /tag} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/12-tags/#list-existing-tags" target="_top">UpCloud API docs for GET /tag</a>
     */
    def tags(...args) {
        this.SESSION.GET('tag', *args)
    }

    /**
     * Fetch a list of all storages.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.Storage} instances
     *     in the {@code storages} property.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.storages { resp, err ->
     *         assert resp?.storages instanceof List
     *         assert resp.storages.every { it instanceof Storage }
     *     }
     * </code></pre>
     * <p>
     *     A keyword argument {@code type} can be used to narrow down the list. The type can be access type
     *     ({@code public} or {@code private}), or storage type ({@code normal}, {@code backup}, {@code cdrom}, or
     *     {@code template}), or {@code favorite}.
     * </p>
     * <pre><code class="groovy">
     *     upcloud.storages type: "template" { resp, err ->
     *     }
     * </code></pre>
     * <p>
     *     Only the storages' most relevant information is returned by this operation. Further details on individual
     *     storage can be requested with the {@link Storage#load(def)} API.
     * </p>
     *
     * @param args.type Optional filter for storages: {@code public}, {@code private}, {@code normal}, {@code backup},
     * {@code cdrom}, {@code template}, or {@code favorite}.
     * @param args Request callbacks for the {@code GET /storage} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#list-storages" target="_top">UpCloud API docs for GET /storage</a>
     */
    def storages(...args) {
        def type = args.find { it instanceof Map } ?.remove('type')
        if (type)
            this.SESSION.GET("storage/$type", *args)
        else
            this.SESSION.GET("storage", *args)
    }

    /**
     * Create a resource.
     * <p>
     *     For {@link fi.linuxbox.upcloud.resource.Server} creation, a {@code 202 Accepted} response will include an
     *     instance of {@link fi.linuxbox.upcloud.resource.Server} in the {@code server} property. The
     *     {@link fi.linuxbox.upcloud.resource.Server#state} property will be {@code maintenance} since it will take
     *     awhile to create the server.
     * </p>
     * <p>
     *     For {@link fi.linuxbox.upcloud.resource.Storage} creation, a {@code 201 Created} response will include an
     *     instance of {@link fi.linuxbox.upcloud.resource.Storage} in the {@code storage} property.
     * </p>
     * <p>
     *     For {@link fi.linuxbox.upcloud.resource.IpAddress} creation, a {@code 201 Created} response will include an
     *     instance of {@link fi.linuxbox.upcloud.resource.IpAddress} in the {@code ipAddress} property.
     * </p>
     * <p>
     *     For {@link fi.linuxbox.upcloud.resource.Tag} creation, a {@code 200 OK} response will include an instance
     *     of {@link fi.linuxbox.upcloud.resource.Tag} in the {@code tag} property.
     * </p>
     * @param resource Resource to create.
     * @param args Request callbacks for the {@code POST} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#create-server" target="_top">UpCloud API docs for POST /server</a>
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#create-storage" target="_top">UpCloud API docs for POST /storage</a>
     * @see <a href="https://www.upcloud.com/api/1.2.4/10-ip-addresses/#assign-ip-address" target="_top">UpCloud API docs for POST /ip_address</a>
     * @see <a href="https://www.upcloud.com/api/1.2.4/12-tags/#create-a-new-tag" target="_top">UpCloud API docs for POST /tag</a>
     */
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

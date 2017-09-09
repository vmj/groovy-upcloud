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
    private final Session SESSION

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
     * <pre>
     *     upcloud.account { resp, err ->
     *         assert resp?.account instanceof Account
     *     }
     * </pre>
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
     * <pre>
     *     upcloud.prices { resp, err ->
     *         assert resp?.prices instanceof List
     *         assert rest.prices.every { it instanceof Zone }
     *     }
     * </pre>
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
     * <pre>
     *     upcloud.zones { resp, err ->
     *         assert resp?.zones instanceof List
     *         assert rest.zones.every { it instanceof Zone }
     *     }
     * </pre>
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
     * <pre>
     *     upcloud.timezones { resp, err ->
     *         assert resp?.timezones instanceof List
     *         assert rest.timezones.every { it instanceof String }
     *     }
     * </pre>
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
     * <pre>
     *     upcloud.plans { resp, err ->
     *         assert resp?.plans instanceof List
     *         assert rest.plans.every { it instanceof Plan }
     *     }
     * </pre>
     *
     * @param args Request callbacks for the {@code GET /plan} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/7-plans/" target="_top">UpCloud API docs for GET /plan</a>
     */
    def plans(...args) {
        this.SESSION.GET('plan', *args)
    }

    /**
     * Fetch a of available server configurations.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.ServerSize} instances
     *     in the {@code serverSizes} property.
     * </p>
     * <pre>
     *     upcloud.serverSizes { resp, err ->
     *         assert resp?.serverSizes instanceof List
     *         assert rest.serverSizes.every { it instanceof ServerSize }
     *     }
     * </pre>
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

    def ipAddresses(...args) {
        this.SESSION.GET('ip_address', *args)
    }

    def tags(...args) {
        this.SESSION.GET('tag', *args)
    }

    def storages(...args) {
        def type = args.find { it instanceof Map } ?.remove('type')
        if (type)
            this.SESSION.GET("storage/$type", *args)
        else
            this.SESSION.GET("storage", *args)
    }

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

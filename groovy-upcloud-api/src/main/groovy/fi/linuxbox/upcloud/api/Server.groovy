package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.core.*
/**
 * Server related APIs.
 *
 * <p>
 *     This class provides the following APIs:
 * </p>
 * <ul>
 *     <li>loading, modifying, and deleting  servers</li>
 *     <li>starting, stopping, and restarting a server, including soft and hard stops</li>
 *     <li>attaching and detaching storage devices to a server</li>
 *     <li>inserting and ejecting CD-roms</li>
 *     <li>managing firewall rules for a server (list rules, getting details, adding, and deletion of individual rules)</li>
 *     <li>adding and removing tags from a server</li>
 * </ul>
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null SESSION property, which can be read-only</li>
 *     <li>non-null uuid property, which can be read-only</li>
 * </ul>
 */
trait Server {

    /**
     * Fetch detailed information about a specific {@link fi.linuxbox.upcloud.resource.Server}.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * <pre><code class="groovy">
     *     serverApi.load { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * <p>
     *     While this operation returns details of a single server, a less details list of all servers can be
     *     requested with the {@link UpCloud#servers(def)} API.
     * </p>
     *
     * @param args Request callbacks for the {@code GET /server/&#36;&#123;server.uuid&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#get-server-details" target="_top">UpCloud API docs for GET /server/&#36;{server.uuid}</a>
     */
    def load(...args) {
        this.SESSION.GET(serverPath(), *args)
    }

    /**
     * Modifies the configuration of an existing server.
     * <p>
     *     A {@code 202 Accepted} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.*
     *
     *     def web1 = server {
     *         firewall = "on"
     *     }
     *
     *     serverApi.update web1 { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * @param resource Updated server resource.
     * @param args Request callbacks for the {@code PUT /server/&#36;&#123;server.uuid&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#modify-server" target="_top">UpCloud API docs for PUT /server/&#36;{server.uuid}</a>
     */
    def update(Resource resource, ...args) {
        this.SESSION.PUT(serverPath(), resource.wrapper(), *args)
    }

    def delete(...args) {
        this.SESSION.DELETE(serverPath(), *args)
    }

    /**
     * Starts a stopped server.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * <pre><code class="groovy">
     *     serverApi.start { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * @param args Request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/start} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#start-server" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/start</a>
     */
    def start(...args) {
        this.SESSION.POST(cmdPath('start'), null, *args)
    }

    /**
     * Stops a started server.
     * <p>
     *     By default, a soft stop is performed.  In soft stop, an ACPI signal is sent to the server.
     *     Optionally, a {@code timeout} keyword argument can be added to specify the time in seconds after which
     *     the server is hard stopped if it hasn't stopped by itself.  If no timeout is given, the server is not
     *     stopped if doesn't stop by itself.
     * </p>
     * <p>
     *     In hard stop, the server is basically just killed right away. An optional {@code stop_type: "hard"}
     *     keyword argument can be used to perform the hard stop without first doing the soft stop.
     * </p>
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.  Note that the {@link fi.linuxbox.upcloud.resource.Server#state} is still
     *     {@code started} in the response, and will need to be polled until it changes to {@code stopped} once the
     *     server has shut down.
     * </p>
     * <pre><code class="groovy">
     *     serverApi.stop timeout: "60" { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * @param args.stop_type Either {@code soft} (default) or {@code hard}.
     * @param args.timeout If, after soft stop, this many seconds pass and the server hasn't stopped, a hard stop is performed.
     * @param args Stop type arguments and request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/stop} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#stop-server" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/stop</a>
     */
    def stop(...args) {
        def stop = cmd('stop_server', ['stop_type', 'timeout'], args)

        this.SESSION.POST(cmdPath('stop'), stop, *args)
    }

    /**
     * Restarts a started server.
     * <p>
     *     By default, a soft stop is performed.  In soft stop, an ACPI signal is sent to the server. Keyword
     *     arguments {@code timeout} and {@code timeout_action} can be used to configure what happens after the soft
     *     stop attempt.  If the server stops before the timeout, it is started.  If the server has not stopped by
     *     itself after {@code timeout} seconds, {@code timeout_action: "ignore"} can be used to cancel the restart
     *     operation, and {@code timeout_action: "destroy"} can be used to hard stop the server before starting it.
     * </p>
     * <p>
     *     Alternatively, keyword argument {@code stop_type: "hard"} can be used to basically kill the server and then
     *     start it.
     * </p>
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * <pre><code class="groovy">
     *     serverApi.restart timeout: "60", timeout_action: "destroy" { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * @param args.stop_type Either {@code soft} (default) or {@code hard}.
     * @param args.timeout Number of seconds to wait for the server to shutdown after ACPI signal, before performing {@code timeout_action}.
     * @param args.timeout_action Either {@code destroy} (hard stop and start) or {@code ignore} (do not restart).
     * @param args Stop type arguments and request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/restart} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#restart-server" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/restart</a>
     */
    def restart(...args) {
        def restart = cmd('restart_server', ['stop_type', 'timeout', 'timeout_action'], args)

        this.SESSION.POST(cmdPath('restart'), restart, *args)
    }

    def attach(Resource storageDevice, ...args) {
        this.SESSION.POST(storagePath('attach'), storageDevice.wrapper(), *args)
    }

    def detach(Resource storageDevice, ...args) {
        this.SESSION.POST(storagePath('detach'), storageDevice.wrapper(), *args)
    }

    def insert(Resource storageDevice, ...args) {
        this.SESSION.POST(cdromPath('load'), storageDevice.wrapper(), *args)
    }

    def eject(...args) {
        this.SESSION.POST(cdromPath('eject'), null, *args)
    }

    def firewallRules(...args) {
        this.SESSION.GET(firewallRulesPath(), *args)
    }

    def createFirewallRule(Resource firewallRule, ...args) {
        this.SESSION.POST(firewallRulesPath(), firewallRule.wrapper(), *args)
    }

    def loadFirewallRule(def position, ...args) {
        this.SESSION.GET(firewallRulePath(position), *args)
    }

    def deleteFirewallRule(def position, ...args) {
        this.SESSION.DELETE(firewallRulePath(position), *args)
    }

    def addTags(def tags, ...args) {
        this.SESSION.POST(tagPath(tags), null, *args)
    }

    def deleteTag(def tag, ...args) {
        this.SESSION.POST(untagPath(tag), null, *args)
    }


    private def cmd(final String name, final List<String> options, final Object[] args) {
        Map kwargs = args.find { it instanceof Map }
        options.inject(new Resource()."$name"(new Resource())) { Resource cmd, String option ->
            def value = kwargs?.remove(option)
            if (value)
                cmd."$name"."$option" = value
            cmd
        }
    }


    private String serverPath() { "server/$uuid" }
    private String cmdPath(String cmd)     { "${serverPath()}/$cmd" }
    private String storagePath(String cmd) { "${serverPath()}/storage/$cmd" }
    private String cdromPath(String cmd)   { "${serverPath()}/cdrom/$cmd" }
    private String firewallRulesPath()     { "${serverPath()}/firewall_rule" }
    private String firewallRulePath(def position) { "${firewallRulesPath()}/$position" }
    private String tagPath(def tags) { "${serverPath()}/tag/${tags.join(',')}" }
    private String untagPath(def tag) { "${serverPath()}/untag/$tag" }
}

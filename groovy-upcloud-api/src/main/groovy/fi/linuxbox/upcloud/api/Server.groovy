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

    /**
     * Deletes an existing server.
     * <p>
     *     A {@code 204 No Content} response will signify that the server is deleted.
     * </p>
     * <p>
     *     The {@link fi.linuxbox.upcloud.resource.Server#state} of the server must be {@code stopped}.
     * <p>
     * <p>
     *     The storage devices of the server are automatically detached, and IP addresses are released.
     * </p>
     * @param args Request callbacks for the {@code DELETE /server/&#36;&#123;server.uuid&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#delete-server" target="_top">UpCloud API docs for DELETE /server/&#36;{server.uuid}</a>
     */
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

    /**
     * Attaches a storage as a device to a server.
     * <p>
     *     SCSI and virtio devices can be attached to a running server, but for IDE devices the server must be
     *     stopped.
     * </p>
     * <p>
     *     When attaching a CD-ROM device (with or without a storage), the server must be stopped.  Storage can then
     *     be {@link #insert(fi.linuxbox.upcloud.core.Resource, def) inserted} and {@link #eject(def) ejected} while
     *     the server is running.
     * </p>
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.storageDevice
     *
     *     def dataDisk = storageDevice {
     *         type = 'disk'
     *         address = 'scsi:0:0'
     *         storage = '00798b85-efdc-41ca-8021-f6ef457b8531'
     *     }
     *     serverApi.attach dataDisk, { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * @param storageDevice Storage device to attach
     * @param args Request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/storage/attach} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#attach-storage" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/storage/attach</a>
     */
    def attach(Resource storageDevice, ...args) {
        this.SESSION.POST(storagePath('attach'), storageDevice.wrapper(), *args)
    }

    /**
     * Detaches a storage device from a server.
     * <p>
     *     For IDE devices, the server must be stopped.
     * </p>
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.storageDevice
     *
     *     def dataDisk = storageDevice {
     *         address = 'scsi:0:0'
     *     }
     *     serverApi.detach dataDisk, { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * @param storageDevice Storage device to detach
     * @param args Request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/storage/detach} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#detach-storage" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/storage/detach</a>
     */
    def detach(Resource storageDevice, ...args) {
        this.SESSION.POST(storagePath('detach'), storageDevice.wrapper(), *args)
    }

    /**
     * Loads a storage as a CD-ROM in the CD-ROM device of a server.
     * <p>
     *     This requires that the CD-ROM device is already
     *     {@link #insert(fi.linuxbox.upcloud.core.Resource, def) attached} to the server.
     * </p>
     * <p>
     *     Storages of type {@code normal}, {@code cdrom}, and {@code backup} can be inserted as CD-ROMs.
     * </p>
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.storageDevice
     *
     *     def knoppixInstallCD = storageDevice {
     *         storage = '01000000-0000-4000-8000-000060010101'
     *     }
     *     serverApi.insert knoppixInstallCD, { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * @param storageDevice Storage device to insert as CD-ROM
     * @param args Request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/cdrom/load} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#load-cd-rom" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/cdrom/load</a>
     */
    def insert(Resource storageDevice, ...args) {
        this.SESSION.POST(cdromPath('load'), storageDevice.wrapper(), *args)
    }

    /**
     * Ejects the storage from the CD-ROM device of a server.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * @param args Request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/cdrom/eject} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#eject-cd-rom" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/cdrom/eject</a>
     */
    def eject(...args) {
        this.SESSION.POST(cdromPath('eject'), null, *args)
    }

    /**
     * Fetch firewall rules for a server.
     * <p>
     *     A {@code 200 OK} response will include a list of {@link fi.linuxbox.upcloud.resource.FirewallRule}
     *     instances in the {@code firewallRules} property.
     * </p>
     * <pre><code class="groovy">
     *     serverApi.firewallRules { resp, err ->
     *         assert resp?.firewallRules instanceof List
     *         assert resp.firewallRules.every { it instanceof FirewallRule }
     *     }
     * </code></pre>
     * @param args Request callbacks for the {@code GET /server/&#36;&#123;server.uuid&#125;/firewall_rule} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/11-firewall/#list-firewall-rules" target="_top">UpCloud API docs for GET /server/&#36;{server.uuid}/firewall_rule</a>
     */
    def firewallRules(...args) {
        this.SESSION.GET(firewallRulesPath(), *args)
    }

    /**
     * Create a firewall rule for a server.
     * <p>
     *     If a {@link fi.linuxbox.upcloud.resource.FirewallRule#position position} property is not set in the given
     *     firewall rule, then the rule is appended to the list of firewall rules.  If the position property is set,
     *     then the rule is inserted at that position in the rule list, and the position properties of any following
     *     rules are increased by one.
     * </p>
     * <p>
     *     A {@code 201 Created} response will include an instance of {@link fi.linuxbox.upcloud.resource.FirewallRule}
     *     in the {@code firewallRule} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.firewallRule
     *
     *     def acceptHttp = firewallRule {
     *         action = 'accept'
     *         comment = 'Allow HTTP from anywhere'
     *         destinationPortEnd = '80'
     *         destinationPortStart = '80'
     *         direction = 'in'
     *         family = 'IPv4'
     *         protocol = 'tcp'
     *     }
     *
     *     serverApi.createFirewallRule acceptHttp, { resp, err ->
     *         assert resp?.firewallRule instanceof FirewallRule
     *     }
     * </code></pre>
     * @param firewallRule Description of the firewall rule
     * @param args Request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/firewall_rule} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/11-firewall/#create-firewall-rule" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/firewall_rule</a>
     */
    def createFirewallRule(Resource firewallRule, ...args) {
        this.SESSION.POST(firewallRulesPath(), firewallRule.wrapper(), *args)
    }

    /**
     * Fetch detailed information about a specific firewall rule.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.FirewallRule}
     *     in the {@code firewallRule} property.
     * </p>
     * <pre><code class="groovy">
     *     serverApi.loadFirewallRule 1, { resp, err ->
     *         assert resp?.firewallRule instanceof FirewallRule
     *     }
     * </code></pre>
     * @param position The position number of the rule in the firewall rules list
     * @param args Request callbacks for the {@code GET /server/&#36;&#123;server.uuid&#125;/firewall_rule/&#36;position} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/11-firewall/#get-firewall-rule-details" target="_top">UpCloud API docs for GET /server/&#36;{server.uuid}/firewall_rule/&#36;position</a>
     */
    def loadFirewallRule(def position, ...args) {
        this.SESSION.GET(firewallRulePath(position), *args)
    }

    /**
     * Removes a firewall rule from a server.
     * <p>
     *     The {@link fi.linuxbox.upcloud.resource.FirewallRule#position position} properties of any following rules
     *     are decreased by one.
     * </p>
     * <p>
     *     A {@code 204 No Content} response signifies success.
     * </p>
     * @param position The position number of the rule in the firewall rules list
     * @param args Request callbacks for the {@code DELETE /server/&#36;&#123;server.uuid&#125;/firewall_rule/&#36;position} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/11-firewall/#remove-firewall-rule" target="_top">UpCloud API docs for DELETE /server/&#36;{server.uuid}/firewall_rule/&#36;position</a>
     */
    def deleteFirewallRule(def position, ...args) {
        this.SESSION.DELETE(firewallRulePath(position), *args)
    }

    /**
     * Assigns existing tags to an existing server.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * <pre><code class="groovy">
     *     serverApi.addTags(['DEV','private','RHEL']) { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * @param tags List of tags to assign
     * @param args Request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/tag/&#36;&#123;tags.join(',')&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/12-tags/#assign-tag-to-a-server" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/tag/&#36;{tags.join(',')}</a>
     */
    def addTags(def tags, ...args) {
        this.SESSION.POST(tagPath(tags), null, *args)
    }

    /**
     * Removes existing tags from an existing server.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Server}
     *     in the {@code server} property.
     * </p>
     * <pre><code class="groovy">
     *     serverApi.deleteTags(['DEV','private','RHEL']) { resp, err ->
     *         assert resp?.server instanceof Server
     *     }
     * </code></pre>
     * @param tags List of tags to remove
     * @param args Request callbacks for the {@code POST /server/&#36;&#123;server.uuid&#125;/untag/&#36;&#123;tags.join(',')&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/12-tags/#remove-tag-from-server" target="_top">UpCloud API docs for POST /server/&#36;{server.uuid}/untag/&#36;{tags.join(',')}</a>
     */
    def deleteTags(def tags, ...args) {
        this.SESSION.POST(untagPath(tags), null, *args)
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
    private String untagPath(def tags) { "${serverPath()}/untag/${tags.join(',')}" }
}

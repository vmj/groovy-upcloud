package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Server related APIs.
 *
 * <p>
 *     This class provides the following APIs:
 * </p>
 * <ul>
 *     <li>CRUD operations for servers (create, get details, update, and delete)</li>
 *     <li>starting, stopping, and restarting a server, including soft and hard stops</li>
 *     <li>attaching and detaching storage devices to a server</li>
 *     <li>inserting and ejecting CD-roms</li>
 *     <li>managing firewall rules for a server (list rules, getting details, adding, and deletion of individual rules)</li>
 * </ul>
 * <p>
 *
 * </p>
 */
class Server extends Resource {
    private final Logger log = LoggerFactory.getLogger(Server)

    //private API (GET, DELETE, PUT, POST)
    //private Resource (wrapper(), uuid)

    def create(...args) {
        API.POST(serversPath(), this.wrapper(), *args)
    }

    def load(...args) {
        API.GET(serverPath(), *args)
    }

    def update(...args) {
        API.PUT(serverPath(), this.wrapper(), *args)
    }

    def delete(...args) {
        API.DELETE(serverPath(), *args)
    }

    def start(...args) {
        API.POST(serverPath(), null, *args)
    }

    def stop(Map cbs, Closure cb) {
        def stop = cmd('stop_server', ['stop_type', 'timeout'],  cbs)

        API.POST(cmdPath('stop'), stop, cbs, cb)
    }

    def restart(Map cbs, Closure cb) {
        def restart = cmd('restart_server', ['stop_type', 'timeout', 'timeout_action'], cbs)

        API.POST(cmdPath('restart'), restart, cbs, cb)
    }

    def attach(Resource storageDevice, ...args) {
        API.POST(storagePath('attach'), storageDevice.wrapper(), *args)
    }

    def detach(Resource storageDevice, ...args) {
        API.POST(storagePath('detach'), storageDevice.wrapper(), *args)
    }

    def insert(Resource storageDevice, ...args) {
        API.POST(cdromPath('load'), storageDevice.wrapper(), *args)
    }

    def eject(Resource storageDevice, ...args) {
        API.POST(cdromPath('eject'), null, *args)
    }

    def firewallRules(...args) {
        API.GET(firewallRulesPath(), *args)
    }

    def createFirewallRule(Resource firewallRule, ...args) {
        API.POST(firewallRulesPath(), firewallRule.wrapper(), *args)
    }

    def loadFirewallRule(def firewallRule, ...args) {
        API.GET(firewallRulePath(firewallRule.position), *args)
    }

    def deleteFirewallRule(def firewallRule, ...args) {
        API.DELETE(firewallRulePath(firewallRule.position), *args)
    }


    private def cmd(final String name, final List<String> options, final Map kwargs) {
        options.inject(new Resource()."$name"(new Resource())) { Resource cmd, String option ->
            def value = kwargs.remove(option)
            if (value)
                cmd."$name"."$option" = value
        }
    }


    private String serversPath() { 'server/' }
    private String serverPath() {
        serversPath() + this.uuid // throws MissingPropertyExcepton
    }
    private String cmdPath(String cmd)     { serverPath() + '/$cmd' }
    private String storagePath(String cmd) { serverPath() + '/storage/$cmd' }
    private String cdromPath(String cmd)   { serverPath() + '/cdrom/$cmd' }
    private String firewallRulesPath()     { serverPath() + '/firewall_rule' }

    private String firewallRulePath(def position) { firewallRulesPath() + '/$position' }

}

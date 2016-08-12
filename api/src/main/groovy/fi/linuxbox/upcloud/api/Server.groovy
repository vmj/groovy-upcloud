package fi.linuxbox.upcloud.api

import groovy.transform.SelfType

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
 *     <li>non-null API property, which can be read-only</li>
 *     <li>non-null uuid property, which can be read-only</li>
 * </ul>
 */
@SelfType(Resource) // must have uuid property
trait Server {

    def load(...args) {
        this.API.GET(serverPath(), *args)
    }

    def update(Resource resource, ...args) {
        this.API.PUT(serverPath(), resource.wrapper(), *args)
    }

    def delete(...args) {
        this.API.DELETE(serverPath(), *args)
    }

    def start(...args) {
        this.API.POST(cmdPath('start'), null, *args)
    }

    def stop(Map cbs = [:], Closure cb) { // FIXME: server stop/restart: ...args
        def stop = cmd('stop_server', ['stop_type', 'timeout'],  cbs)

        this.API.POST(cmdPath('stop'), stop, cbs, cb)
    }

    def restart(Map cbs = [:], Closure cb) {
        def restart = cmd('restart_server', ['stop_type', 'timeout', 'timeout_action'], cbs)

        this.API.POST(cmdPath('restart'), restart, cbs, cb)
    }

    def attach(Resource storageDevice, ...args) {
        this.API.POST(storagePath('attach'), storageDevice.wrapper(), *args)
    }

    def detach(Resource storageDevice, ...args) {
        this.API.POST(storagePath('detach'), storageDevice.wrapper(), *args)
    }

    def insert(Resource storageDevice, ...args) {
        this.API.POST(cdromPath('load'), storageDevice.wrapper(), *args)
    }

    def eject(...args) {
        this.API.POST(cdromPath('eject'), null, *args)
    }

    def firewallRules(...args) {
        this.API.GET(firewallRulesPath(), *args)
    }

    def createFirewallRule(Resource firewallRule, ...args) {
        this.API.POST(firewallRulesPath(), firewallRule.wrapper(), *args)
    }

    def loadFirewallRule(def position, ...args) { // FIXME: firewall load/detele: position as Resource?
        this.API.GET(firewallRulePath(position), *args)
    }

    def deleteFirewallRule(def position, ...args) {
        this.API.DELETE(firewallRulePath(position), *args)
    }

    def addTags(def tags, ...args) { // FIXME: server tags add/delete: tag(s) as Resource?
        this.API.POST(tagPath(tags), null, *args)
    }

    def deleteTag(def tag, ...args) {
        this.API.POST(untagPath(tag), null, *args)
    }


    private def cmd(final String name, final List<String> options, final Map kwargs) {
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

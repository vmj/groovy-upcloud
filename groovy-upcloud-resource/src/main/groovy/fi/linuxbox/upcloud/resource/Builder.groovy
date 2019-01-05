/*
 * Groovy UpCloud library - Resource Module
 * Copyright (C) 2018  <mikko@varri.fi>
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

import fi.linuxbox.upcloud.builder.ResourceBuilder
import groovy.transform.CompileStatic

import static groovy.lang.Closure.*
import static java.util.Collections.EMPTY_MAP

/**
 * Builder for concrete UpCloud resources.
 * <p>
 *     This class is basically just syntactic sugar for the more generic {@link ResourceBuilder}.
 * </p>
 */
@CompileStatic
class Builder extends ResourceBuilder {
    static Account account(Map kwargs, @DelegatesTo(strategy = DELEGATE_FIRST, value = Account) Closure closure = null) {
        configure(new Account(kwargs), closure)
    }

    static Account account(@DelegatesTo(strategy = DELEGATE_FIRST, value = Account) Closure closure = null) {
        account(EMPTY_MAP, closure)
    }

    static BackupRule backupRule(Map kwargs,
                                 @DelegatesTo(strategy = DELEGATE_FIRST, value = BackupRule) Closure closure = null) {
        configure(new BackupRule(kwargs), closure)
    }

    static BackupRule backupRule(@DelegatesTo(strategy = DELEGATE_FIRST, value = BackupRule) Closure closure = null) {
        backupRule(EMPTY_MAP, closure)
    }

    static Firewall firewall(Map kwargs, @DelegatesTo(strategy = DELEGATE_FIRST, value = Firewall) Closure closure = null) {
        configure(new Firewall(kwargs), closure)
    }

    static Firewall firewall(@DelegatesTo(strategy = DELEGATE_FIRST, value = Firewall) Closure closure = null) {
        firewall(EMPTY_MAP, closure)
    }

    static FirewallRule firewallRule(Map kwargs,
                                     @DelegatesTo(strategy = DELEGATE_FIRST, value = FirewallRule) Closure closure = null) {
        configure(new FirewallRule(kwargs), closure)
    }

    static FirewallRule firewallRule(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = FirewallRule) Closure closure = null) {
        firewallRule(EMPTY_MAP, closure)
    }

    static IoRequestBackup ioRequestBackup(Map kwargs,
                                           @DelegatesTo(strategy = DELEGATE_FIRST, value = IoRequestBackup) Closure closure = null) {
        configure(new IoRequestBackup(kwargs), closure)
    }

    static IoRequestBackup ioRequestBackup(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = IoRequestBackup) Closure closure = null) {
        ioRequestBackup(EMPTY_MAP, closure)
    }

    static IoRequestHdd ioRequestHdd(Map kwargs,
                                     @DelegatesTo(strategy = DELEGATE_FIRST, value = IoRequestHdd) Closure closure = null) {
        configure(new IoRequestHdd(kwargs), closure)
    }

    static IoRequestHdd ioRequestHdd(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = IoRequestHdd) Closure closure = null) {
        ioRequestHdd(EMPTY_MAP, closure)
    }

    static IoRequestMaxiops ioRequestMaxiops(Map kwargs,
                                             @DelegatesTo(strategy = DELEGATE_FIRST, value = IoRequestMaxiops) Closure closure = null) {
        configure(new IoRequestMaxiops(kwargs), closure)
    }

    static IoRequestMaxiops ioRequestMaxiops(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = IoRequestMaxiops) Closure closure = null) {
        ioRequestMaxiops(EMPTY_MAP, closure)
    }

    static IpAddress ipAddress(Map kwargs,
                               @DelegatesTo(strategy = DELEGATE_FIRST, value = IpAddress) Closure closure = null) {
        configure(new IpAddress(kwargs), closure)
    }

    static IpAddress ipAddress(@DelegatesTo(strategy = DELEGATE_FIRST, value = IpAddress) Closure closure = null) {
        ipAddress(EMPTY_MAP, closure)
    }

    static Ipv4Address ipv4Address(Map kwargs,
                                   @DelegatesTo(strategy = DELEGATE_FIRST, value = Ipv4Address) Closure closure = null) {
        configure(new Ipv4Address(kwargs), closure)
    }

    static Ipv4Address ipv4Address(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = Ipv4Address) Closure closure = null) {
        ipv4Address(EMPTY_MAP, closure)
    }

    static Ipv6Address ipv6Address(Map kwargs,
                                   @DelegatesTo(strategy = DELEGATE_FIRST, value = Ipv6Address) Closure closure = null) {
        configure(new Ipv6Address(kwargs), closure)
    }

    static Ipv6Address ipv6Address(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = Ipv6Address) Closure closure = null) {
        ipv6Address(EMPTY_MAP, closure)
    }

    static LoginUser loginUser(Map kwargs,
                               @DelegatesTo(strategy = DELEGATE_FIRST, value = LoginUser) Closure closure = null) {
        configure(new LoginUser(kwargs), closure)
    }

    static LoginUser loginUser(@DelegatesTo(strategy = DELEGATE_FIRST, value = LoginUser) Closure closure = null) {
        loginUser(EMPTY_MAP, closure)
    }

    static Plan plan(Map kwargs, @DelegatesTo(strategy = DELEGATE_FIRST, value = Plan) Closure closure = null) {
        configure(new Plan(kwargs), closure)
    }

    static Plan plan(@DelegatesTo(strategy = DELEGATE_FIRST, value = Plan) Closure closure = null) {
        plan(EMPTY_MAP, closure)
    }

    static PublicIpv4BandwidthIn publicIpv4BandwidthIn(Map kwargs,
                                                       @DelegatesTo(strategy = DELEGATE_FIRST, value = PublicIpv4BandwidthIn) Closure closure = null) {
        configure(new PublicIpv4BandwidthIn(kwargs), closure)
    }

    static PublicIpv4BandwidthIn publicIpv4BandwidthIn(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = PublicIpv4BandwidthIn) Closure closure = null) {
        publicIpv4BandwidthIn(EMPTY_MAP, closure)
    }

    static PublicIpv4BandwidthOut publicIpv4BandwidthOut(Map kwargs,
                                                         @DelegatesTo(strategy = DELEGATE_FIRST, value = PublicIpv4BandwidthOut) Closure closure = null) {
        configure(new PublicIpv4BandwidthOut(kwargs), closure)
    }

    static PublicIpv4BandwidthOut publicIpv4BandwidthOut(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = PublicIpv4BandwidthOut) Closure closure = null) {
        publicIpv4BandwidthOut(EMPTY_MAP, closure)
    }

    static PublicIpv6BandwidthIn publicIpv6BandwidthIn(Map kwargs,
                                                       @DelegatesTo(strategy = DELEGATE_FIRST, value = PublicIpv6BandwidthIn) Closure closure = null) {
        configure(new PublicIpv6BandwidthIn(kwargs), closure)
    }

    static PublicIpv6BandwidthIn publicIpv6BandwidthIn(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = PublicIpv6BandwidthIn) Closure closure = null) {
        publicIpv6BandwidthIn(EMPTY_MAP, closure)
    }

    static PublicIpv6BandwidthOut publicIpv6BandwidthOut(Map kwargs,
                                                         @DelegatesTo(strategy = DELEGATE_FIRST, value = PublicIpv6BandwidthOut) Closure closure = null) {
        configure(new PublicIpv6BandwidthOut(kwargs), closure)
    }

    static PublicIpv6BandwidthOut publicIpv6BandwidthOut(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = PublicIpv6BandwidthOut) Closure closure = null) {
        publicIpv6BandwidthOut(EMPTY_MAP, closure)
    }

    /**
     * Builder method for a {@link fi.linuxbox.upcloud.resource.Server}.
     * <p>
     *     See {@link fi.linuxbox.upcloud.resource.Server} class documentation for information about server creation.
     * </p>
     * <p>
     *     This overload allows passing additional keyword arguments.  See {@link fi.linuxbox.upcloud.core.Resource}
     *     constructor for information about those.
     * </p>
     * @param kwargs Additional keyword arguments for {@link fi.linuxbox.upcloud.core.Resource} constructor.
     * @param closure Server configuration closure
     * @return A configured server resource.
     */
    static Server server(Map kwargs, @DelegatesTo(strategy = DELEGATE_FIRST, value = Server) Closure closure = null) {
        configure(new Server(kwargs), closure)
    }

    /**
     * Builder method for a {@link fi.linuxbox.upcloud.resource.Server}.
     * <p>
     *     See {@link fi.linuxbox.upcloud.resource.Server} class documentation for information about server creation.
     * </p>
     * @param closure Server configuration closure
     * @return A configured server resource.
     */
    static Server server(@DelegatesTo(strategy = DELEGATE_FIRST, value = Server) Closure closure = null) {
        server(EMPTY_MAP, closure)
    }

    static ServerCore serverCore(Map kwargs,
                                 @DelegatesTo(strategy = DELEGATE_FIRST, value = ServerCore) Closure closure = null) {
        configure(new ServerCore(kwargs), closure)
    }

    static ServerCore serverCore(@DelegatesTo(strategy = DELEGATE_FIRST, value = ServerCore) Closure closure = null) {
        serverCore(EMPTY_MAP, closure)
    }

    static ServerMemory serverMemory(Map kwargs,
                                     @DelegatesTo(strategy = DELEGATE_FIRST, value = ServerMemory) Closure closure = null) {
        configure(new ServerMemory(kwargs), closure)
    }

    static ServerMemory serverMemory(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = ServerMemory) Closure closure = null) {
        serverMemory(EMPTY_MAP, closure)
    }

    static ServerPlan_1xCPU_1GB serverPlan_1xCPU_1GB(Map kwargs,
                                                     @DelegatesTo(strategy = DELEGATE_FIRST, value = ServerPlan_1xCPU_1GB) Closure closure = null) {
        configure(new ServerPlan_1xCPU_1GB(kwargs), closure)
    }

    static ServerPlan_1xCPU_1GB serverPlan_1xCPU_1GB(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = ServerPlan_1xCPU_1GB) Closure closure = null) {
        serverPlan_1xCPU_1GB(EMPTY_MAP, closure)
    }

    static ServerPlan_2xCPU_2GB serverPlan_2xCPU_2GB(Map kwargs,
                                                     @DelegatesTo(strategy = DELEGATE_FIRST, value = ServerPlan_2xCPU_2GB) Closure closure = null) {
        configure(new ServerPlan_2xCPU_2GB(kwargs), closure)
    }

    static ServerPlan_2xCPU_2GB serverPlan_2xCPU_2GB(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = ServerPlan_2xCPU_2GB) Closure closure = null) {
        serverPlan_2xCPU_2GB(EMPTY_MAP, closure)
    }

    static ServerSize serverSize(Map kwargs,
                                 @DelegatesTo(strategy = DELEGATE_FIRST, value = ServerSize) Closure closure = null) {
        configure(new ServerSize(kwargs), closure)
    }

    static ServerSize serverSize(@DelegatesTo(strategy = DELEGATE_FIRST, value = ServerSize) Closure closure = null) {
        serverSize(EMPTY_MAP, closure)
    }

    static Storage storage(Map kwargs,
                           @DelegatesTo(strategy = DELEGATE_FIRST, value = Storage) Closure closure = null) {
        configure(new Storage(kwargs), closure)
    }

    static Storage storage(@DelegatesTo(strategy = DELEGATE_FIRST, value = Storage) Closure closure = null) {
        storage(EMPTY_MAP, closure)
    }

    static StorageBackup storageBackup(Map kwargs,
                                       @DelegatesTo(strategy = DELEGATE_FIRST, value = StorageBackup) Closure closure = null) {
        configure(new StorageBackup(kwargs), closure)
    }

    static StorageBackup storageBackup(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = StorageBackup) Closure closure = null) {
        storageBackup(EMPTY_MAP, closure)
    }

    static StorageDevice storageDevice(Map kwargs,
                                       @DelegatesTo(strategy = DELEGATE_FIRST, value = StorageDevice) Closure closure = null) {
        configure(new StorageDevice(kwargs), closure)
    }

    static StorageDevice storageDevice(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = StorageDevice) Closure closure = null) {
        storageDevice(EMPTY_MAP, closure)
    }

    static StorageHdd storageHdd(Map kwargs,
                                 @DelegatesTo(strategy = DELEGATE_FIRST, value = StorageHdd) Closure closure = null) {
        configure(new StorageHdd(kwargs), closure)
    }

    static StorageHdd storageHdd(@DelegatesTo(strategy = DELEGATE_FIRST, value = StorageHdd) Closure closure = null) {
        storageHdd(EMPTY_MAP, closure)
    }

    static StorageMaxiops storageMaxiops(Map kwargs,
                                         @DelegatesTo(strategy = DELEGATE_FIRST, value = StorageMaxiops) Closure closure = null) {
        configure(new StorageMaxiops(kwargs), closure)
    }

    static StorageMaxiops storageMaxiops(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = StorageMaxiops) Closure closure = null) {
        storageMaxiops(EMPTY_MAP, closure)
    }

    static Tag tag(Map kwargs, @DelegatesTo(strategy = DELEGATE_FIRST, value = Tag) Closure closure = null) {
        configure(new Tag(kwargs), closure)
    }

    static Tag tag(@DelegatesTo(strategy = DELEGATE_FIRST, value = Tag) Closure closure = null) {
        tag(EMPTY_MAP, closure)
    }

    static Zone zone(Map kwargs, @DelegatesTo(strategy = DELEGATE_FIRST, value = Zone) Closure closure = null) {
        configure(new Zone(kwargs), closure)
    }

    static Zone zone(@DelegatesTo(strategy = DELEGATE_FIRST, value = Zone) Closure closure = null) {
        zone(EMPTY_MAP, closure)
    }
}

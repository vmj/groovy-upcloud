package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.builder.ResourceBuilder

import static groovy.lang.Closure.*
import static java.util.Collections.EMPTY_MAP

/**
 * Builder for concrete UpCloud resources.
 * <p>
 *     This class is basically just syntactic sugar for the more generic {@link ResourceBuilder}.
 * </p>
 */
class Builder extends ResourceBuilder {
    static Account account(Map kwargs, @DelegatesTo(strategy = DELEGATE_ONLY, value = Account) Closure closure = null) {
        configure(new Account(kwargs), closure)
    }

    static Account account(@DelegatesTo(strategy = DELEGATE_ONLY, value = Account) Closure closure = null) {
        account(EMPTY_MAP, closure)
    }

    static Firewall firewall(Map kwargs, @DelegatesTo(strategy = DELEGATE_ONLY, value = Firewall) Closure closure = null) {
        configure(new Firewall(kwargs), closure)
    }

    static Firewall firewall(@DelegatesTo(strategy = DELEGATE_ONLY, value = Firewall) Closure closure = null) {
        firewall(EMPTY_MAP, closure)
    }

    static IoRequestBackup ioRequestBackup(Map kwargs,
                                           @DelegatesTo(strategy = DELEGATE_ONLY, value = IoRequestBackup) Closure closure = null) {
        configure(new IoRequestBackup(kwargs), closure)
    }

    static IoRequestBackup ioRequestBackup(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = IoRequestBackup) Closure closure = null) {
        ioRequestBackup(EMPTY_MAP, closure)
    }

    static IoRequestHdd ioRequestHdd(Map kwargs,
                                     @DelegatesTo(strategy = DELEGATE_ONLY, value = IoRequestHdd) Closure closure = null) {
        configure(new IoRequestHdd(kwargs), closure)
    }

    static IoRequestHdd ioRequestHdd(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = IoRequestHdd) Closure closure = null) {
        ioRequestHdd(EMPTY_MAP, closure)
    }

    static IoRequestMaxiops ioRequestMaxiops(Map kwargs,
                                             @DelegatesTo(strategy = DELEGATE_ONLY, value = IoRequestMaxiops) Closure closure = null) {
        configure(new IoRequestMaxiops(kwargs), closure)
    }

    static IoRequestMaxiops ioRequestMaxiops(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = IoRequestMaxiops) Closure closure = null) {
        ioRequestMaxiops(EMPTY_MAP, closure)
    }

    static IpAddress ipAddress(Map kwargs,
                               @DelegatesTo(strategy = DELEGATE_ONLY, value = IpAddress) Closure closure = null) {
        configure(new IpAddress(kwargs), closure)
    }

    static IpAddress ipAddress(@DelegatesTo(strategy = DELEGATE_ONLY, value = IpAddress) Closure closure = null) {
        ipAddress(EMPTY_MAP, closure)
    }

    static Ipv4Address ipv4Address(Map kwargs,
                                   @DelegatesTo(strategy = DELEGATE_ONLY, value = Ipv4Address) Closure closure = null) {
        configure(new Ipv4Address(kwargs), closure)
    }

    static Ipv4Address ipv4Address(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = Ipv4Address) Closure closure = null) {
        ipv4Address(EMPTY_MAP, closure)
    }

    static Ipv6Address ipv6Address(Map kwargs,
                                   @DelegatesTo(strategy = DELEGATE_ONLY, value = Ipv6Address) Closure closure = null) {
        configure(new Ipv6Address(kwargs), closure)
    }

    static Ipv6Address ipv6Address(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = Ipv6Address) Closure closure = null) {
        ipv6Address(EMPTY_MAP, closure)
    }

    static Plan plan(Map kwargs, @DelegatesTo(strategy = DELEGATE_ONLY, value = Plan) Closure closure = null) {
        configure(new Plan(kwargs), closure)
    }

    static Plan plan(@DelegatesTo(strategy = DELEGATE_ONLY, value = Plan) Closure closure = null) {
        plan(EMPTY_MAP, closure)
    }

    static PublicIpv4BandwidthIn publicIpv4BandwidthIn(Map kwargs,
                                                       @DelegatesTo(strategy = DELEGATE_ONLY, value = PublicIpv4BandwidthIn) Closure closure = null) {
        configure(new PublicIpv4BandwidthIn(kwargs), closure)
    }

    static PublicIpv4BandwidthIn publicIpv4BandwidthIn(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = PublicIpv4BandwidthIn) Closure closure = null) {
        publicIpv4BandwidthIn(EMPTY_MAP, closure)
    }

    static PublicIpv4BandwidthOut publicIpv4BandwidthOut(Map kwargs,
                                                         @DelegatesTo(strategy = DELEGATE_ONLY, value = PublicIpv4BandwidthOut) Closure closure = null) {
        configure(new PublicIpv4BandwidthOut(kwargs), closure)
    }

    static PublicIpv4BandwidthOut publicIpv4BandwidthOut(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = PublicIpv4BandwidthOut) Closure closure = null) {
        publicIpv4BandwidthOut(EMPTY_MAP, closure)
    }

    static PublicIpv6BandwidthIn publicIpv6BandwidthIn(Map kwargs,
                                                       @DelegatesTo(strategy = DELEGATE_ONLY, value = PublicIpv6BandwidthIn) Closure closure = null) {
        configure(new PublicIpv6BandwidthIn(kwargs), closure)
    }

    static PublicIpv6BandwidthIn publicIpv6BandwidthIn(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = PublicIpv6BandwidthIn) Closure closure = null) {
        publicIpv6BandwidthIn(EMPTY_MAP, closure)
    }

    static PublicIpv6BandwidthOut publicIpv6BandwidthOut(Map kwargs,
                                                         @DelegatesTo(strategy = DELEGATE_ONLY, value = PublicIpv6BandwidthOut) Closure closure = null) {
        configure(new PublicIpv6BandwidthOut(kwargs), closure)
    }

    static PublicIpv6BandwidthOut publicIpv6BandwidthOut(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = PublicIpv6BandwidthOut) Closure closure = null) {
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
    static Server server(Map kwargs, @DelegatesTo(strategy = DELEGATE_ONLY, value = Server) Closure closure = null) {
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
    static Server server(@DelegatesTo(strategy = DELEGATE_ONLY, value = Server) Closure closure = null) {
        server(EMPTY_MAP, closure)
    }

    static ServerCore serverCore(Map kwargs,
                                 @DelegatesTo(strategy = DELEGATE_ONLY, value = ServerCore) Closure closure = null) {
        configure(new ServerCore(kwargs), closure)
    }

    static ServerCore serverCore(@DelegatesTo(strategy = DELEGATE_ONLY, value = ServerCore) Closure closure = null) {
        serverCore(EMPTY_MAP, closure)
    }

    static ServerMemory serverMemory(Map kwargs,
                                     @DelegatesTo(strategy = DELEGATE_ONLY, value = ServerMemory) Closure closure = null) {
        configure(new ServerMemory(kwargs), closure)
    }

    static ServerMemory serverMemory(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = ServerMemory) Closure closure = null) {
        serverMemory(EMPTY_MAP, closure)
    }

    static ServerPlan_1xCPU_1GB serverPlan_1xCPU_1GB(Map kwargs,
                                                     @DelegatesTo(strategy = DELEGATE_ONLY, value = ServerPlan_1xCPU_1GB) Closure closure = null) {
        configure(new ServerPlan_1xCPU_1GB(kwargs), closure)
    }

    static ServerPlan_1xCPU_1GB serverPlan_1xCPU_1GB(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = ServerPlan_1xCPU_1GB) Closure closure = null) {
        serverPlan_1xCPU_1GB(EMPTY_MAP, closure)
    }

    static ServerPlan_2xCPU_2GB serverPlan_2xCPU_2GB(Map kwargs,
                                                     @DelegatesTo(strategy = DELEGATE_ONLY, value = ServerPlan_2xCPU_2GB) Closure closure = null) {
        configure(new ServerPlan_2xCPU_2GB(kwargs), closure)
    }

    static ServerPlan_2xCPU_2GB serverPlan_2xCPU_2GB(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = ServerPlan_2xCPU_2GB) Closure closure = null) {
        serverPlan_2xCPU_2GB(EMPTY_MAP, closure)
    }

    static ServerSize serverSize(Map kwargs,
                                 @DelegatesTo(strategy = DELEGATE_ONLY, value = ServerSize) Closure closure = null) {
        configure(new ServerSize(kwargs), closure)
    }

    static ServerSize serverSize(@DelegatesTo(strategy = DELEGATE_ONLY, value = ServerSize) Closure closure = null) {
        serverSize(EMPTY_MAP, closure)
    }

    static Storage storage(Map kwargs,
                           @DelegatesTo(strategy = DELEGATE_ONLY, value = Storage) Closure closure = null) {
        configure(new Storage(kwargs), closure)
    }

    static Storage storage(@DelegatesTo(strategy = DELEGATE_ONLY, value = Storage) Closure closure = null) {
        storage(EMPTY_MAP, closure)
    }

    static StorageBackup storageBackup(Map kwargs,
                                       @DelegatesTo(strategy = DELEGATE_ONLY, value = StorageBackup) Closure closure = null) {
        configure(new StorageBackup(kwargs), closure)
    }

    static StorageBackup storageBackup(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = StorageBackup) Closure closure = null) {
        storageBackup(EMPTY_MAP, closure)
    }

    static StorageHdd storageHdd(Map kwargs,
                                 @DelegatesTo(strategy = DELEGATE_ONLY, value = StorageHdd) Closure closure = null) {
        configure(new StorageHdd(kwargs), closure)
    }

    static StorageHdd storageHdd(@DelegatesTo(strategy = DELEGATE_ONLY, value = StorageHdd) Closure closure = null) {
        storageHdd(EMPTY_MAP, closure)
    }

    static StorageMaxiops storageMaxiops(Map kwargs,
                                         @DelegatesTo(strategy = DELEGATE_ONLY, value = StorageMaxiops) Closure closure = null) {
        configure(new StorageMaxiops(kwargs), closure)
    }

    static StorageMaxiops storageMaxiops(
            @DelegatesTo(strategy = DELEGATE_ONLY, value = StorageMaxiops) Closure closure = null) {
        storageMaxiops(EMPTY_MAP, closure)
    }

    static Zone zone(Map kwargs, @DelegatesTo(strategy = DELEGATE_ONLY, value = Zone) Closure closure = null) {
        configure(new Zone(kwargs), closure)
    }

    static Zone zone(@DelegatesTo(strategy = DELEGATE_ONLY, value = Zone) Closure closure = null) {
        zone(EMPTY_MAP, closure)
    }
}

package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Description of an UpCloud zone.
 * <p>
 *     An list of these can be fetched from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
 * </p>
 */
@InheritConstructors
class Zone extends Resource {
    /**
     * Unique zone name.
     * <p>
     *     This has values such as {@code fi-hel1}.
     * </p>
     */
    String name

    /**
     * Firewall price information for this zone.
     */
    Firewall firewall

    /**
     * I/O request price information for backups for this zone.
     */
    IoRequestBackup ioRequestBackup

    /**
     * I/O request price information for HDD storage for this zone.
     */
    IoRequestHdd ioRequestHdd

    /**
     * I/O request price information for MAXIOPS storage for this zone.
     */
    IoRequestMaxiops ioRequestMaxiops

    /**
     * IPv4 address price for this zone.
     */
    Ipv4Address ipv4Address

    /**
     * IPv6 address price for this zone.
     */
    Ipv6Address ipv6Address

    /**
     * Inbound IPv4 traffic price information for this zone.
     */
    PublicIpv4BandwidthIn publicIpv4BandwidthIn

    /**
     * Outbound IPv4 traffic price information for this zone.
     */
    PublicIpv4BandwidthOut publicIpv4BandwidthOut

    /**
     * Inbound IPv6 traffic price information for this zone.
     */
    PublicIpv6BandwidthIn publicIpv6BandwidthIn

    /**
     * Outbound IPv6 traffic price information for this zone.
     */
    PublicIpv6BandwidthOut publicIpv6BandwidthOut

    /**
     * Price per CPU core for this zone.
     */
    ServerCore serverCore

    /**
     * Price per MB of RAM for this zone.
     */
    ServerMemory serverMemory

    /**
     * Price per GB of backup storage for this zone.
     */
    StorageBackup storageBackup

    /**
     * Price per GB of HDD storage for this zone.
     */
    StorageHdd storageHdd

    /**
     * Price per GB of MAXIOPS storage for this zone.
     */
    StorageMaxiops storageMaxiops

    /**
     * Fixed server configuration price for 1CPU/1GB plan for this zone.
     */
    ServerPlan_1xCPU_1GB serverPlan_1xCPU_1GB

    /**
     * Fixed server configuration price for 2CPU/2GB plan for this zone.
     */
    ServerPlan_2xCPU_2GB serverPlan_2xCPU_2GB

}

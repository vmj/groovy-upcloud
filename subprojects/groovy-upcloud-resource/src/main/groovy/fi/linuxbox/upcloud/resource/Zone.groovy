/*
 * Groovy UpCloud library - Resource Module
 * Copyright (C) 2018  Mikko Värri
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

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Description of an UpCloud zone.
 * <p>
 *     A list of these can be fetched from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API} or
 *     {@link fi.linuxbox.upcloud.api.UpCloud#zones(def) Zones API}.  Both return a list of zones, but their
 *     content is completely different:
 * </p>
 * <ul>
 *     <li>Zones API returns {@link #id} and {@link #description} properties, and nothing else</li>
 *     <li>Pricing API returns {@link #name} and various pricing properties. The name is same is id</li>
 * </ul>
 */
@CompileStatic
@InheritConstructors
class Zone extends Resource {
    /**
     * Unique zone ID.
     * <p>
     *     This has values such as {@code fi-hel1}.  This is what you get from
     *     {@link fi.linuxbox.upcloud.api.UpCloud#zones(def) Zones API}.  This
     *     same information is available in the {@link #name} property when using
     *     the {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    String id

    /**
     * Zone description.
     * <p>
     *     This is not returned from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    String description

    /**
     * Unique zone name.
     * <p>
     *     This has values such as {@code fi-hel1}.  This is what you get from
     *     {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.  This
     *     same information is available in the {@link #id} property when using
     *     the {@link fi.linuxbox.upcloud.api.UpCloud#zones(def) Zones API}.
     * </p>
     */
    String name

    /**
     * Firewall price information for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    Firewall firewall

    /**
     * I/O request price information for backups for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    IoRequestBackup ioRequestBackup

    /**
     * I/O request price information for HDD storage for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    IoRequestHdd ioRequestHdd

    /**
     * I/O request price information for MAXIOPS storage for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    IoRequestMaxiops ioRequestMaxiops

    /**
     * IPv4 address price for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    Ipv4Address ipv4Address

    /**
     * IPv6 address price for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    Ipv6Address ipv6Address

    /**
     * Inbound IPv4 traffic price information for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    PublicIpv4BandwidthIn publicIpv4BandwidthIn

    /**
     * Outbound IPv4 traffic price information for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    PublicIpv4BandwidthOut publicIpv4BandwidthOut

    /**
     * Inbound IPv6 traffic price information for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    PublicIpv6BandwidthIn publicIpv6BandwidthIn

    /**
     * Outbound IPv6 traffic price information for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    PublicIpv6BandwidthOut publicIpv6BandwidthOut

    /**
     * Price per CPU core for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerCore serverCore

    /**
     * Price per MB of RAM for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerMemory serverMemory

    /**
     * Price per GB of backup storage for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    StorageBackup storageBackup

    /**
     * Price per GB of HDD storage for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    StorageHdd storageHdd

    /**
     * Price per GB of MAXIOPS storage for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    StorageMaxiops storageMaxiops

    /**
     * Fixed server configuration price for 1CPU/1GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_1xCPU_1GB serverPlan_1xCPU_1GB

    /**
     * Fixed server configuration price for 1CPU/2GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_1xCPU_1GB serverPlan_1xCPU_2GB

    /**
     * Fixed server configuration price for 2CPU/2GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_2xCPU_2GB serverPlan_2xCPU_2GB

    /**
     * Fixed server configuration price for 2CPU/4GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_2xCPU_2GB serverPlan_2xCPU_4GB

    /**
     * Fixed server configuration price for 4CPU/8GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_2xCPU_2GB serverPlan_4xCPU_8GB

    /**
     * Fixed server configuration price for 6CPU/16GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_2xCPU_2GB serverPlan_6xCPU_16GB

    /**
     * Fixed server configuration price for 8CPU/32GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_2xCPU_2GB serverPlan_8xCPU_32GB

    /**
     * Fixed server configuration price for 12CPU/48GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_2xCPU_2GB serverPlan_12xCPU_48GB

    /**
     * Fixed server configuration price for 16CPU/64GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_2xCPU_2GB serverPlan_16xCPU_64GB

    /**
     * Fixed server configuration price for 20CPU/96GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_2xCPU_2GB serverPlan_20xCPU_96GB

    /**
     * Fixed server configuration price for 20CPU/128GB plan for this zone.
     * <p>
     *     This is returned only from {@link fi.linuxbox.upcloud.api.UpCloud#prices(def) Pricing API}.
     * </p>
     */
    ServerPlan_2xCPU_2GB serverPlan_20xCPU_128GB

}

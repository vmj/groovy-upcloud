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
 * Resource limits of an trial {@link Account}.
 */
@CompileStatic
@InheritConstructors
class TrialResourceLimits extends Resource {
    /**
     * If {@code 1}, firewall option is disabled.
     */
    Long trialFirewallRestrictions
    /**
     * Trial period length in hours.
     */
    Long trialPeriodLength
    /**
     * Maximum number of CPU cores per server.
     */
    Long trialServerMaxCores
    /**
     * Maximum amount of memory in MiB per server.
     */
    Long trialServerMaxMemory
    /**
     * Maximum number of public IPv4 addresses per server.
     */
    Long trialServerMaxPublicIpv4
    /**
     * Maximum number of public IPv6 addresses per server.
     */
    Long trialServerMaxPublicIpv6
    /**
     * Maximum storage size in GiB.
     */
    Long trialStorageMaxSize
    /**
     * Storage tier type.
     */
    String trialStorageTier
    /**
     * Maximum number of detached floating IP addresses.
     */
    Long trialTotalDetachedFloatingIps
    /**
     * Maximum number of networks.
     */
    Long trialTotalNetworks
    /**
     * Maximum number of public IPv4 addresses.
     */
    Long trialTotalPublicIpv4
    /**
     * Maximum number of public IPv6 addresses.
     */
    Long trialTotalPublicIpv6
    /**
     * Maximum number of CPU cores.
     */
    Long trialTotalServerCores
    /**
     * Maximum amount of memory in GiB.
     */
    Long trialTotalServerMemory
    /**
     * Maximum number of servers.
     */
    Long trialTotalServers
    /**
     * Maximum amount of storage in GiB.
     */
    Long trialTotalStorageSize
    /**
     * Maximum number of storage devices.
     */
    Long trialTotalStorages
    /**
     * Number of detached floating IP addresses.
     */
    Long userDetachedFloatingIps
    /**
     * Number of networks in use.
     */
    Long userNetworks
    /**
     * Number of public IPv4 addresses in use.
     */
    Long userPublicIpv4
    /**
     * Number of public IPv6 addresses in use.
     */
    Long userPublicIpv6
    /**
     * Number of CPU cores in use.
     */
    Long userServerCores
    /**
     * Amount of memory in use MiB.
     */
    Long userServerMemory
}

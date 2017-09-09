package fi.linuxbox.upcloud.resource

import groovy.transform.InheritConstructors

import fi.linuxbox.upcloud.core.*

/**
 * Server details.
 * <p>
 *     A list of these, with the most relevant information, is typically fetched from
 *     {@link fi.linuxbox.upcloud.api.UpCloud#servers(def) Servers API}.  A more detailed
 *     information about a specific server can be fetched from
 *     {@link fi.linuxbox.upcloud.api.Server#load(def) Server API}.
 * </p>
 */
@InheritConstructors
class Server extends Resource {
    /**
     *
     */
    String bootOrder
    /**
     * Number of CPU cores on this server.
     */
    String coreNumber
    /**
     * Whether the firewall is enabled ({@code "on"}) for this server.
     */
    String firewall
    /**
     *
     */
    Long host
    /**
     * Hostname of this server.
     */
    String hostname
    /**
     * IP addresses of this server.
     */
    List<IpAddress> ipAddresses
    /**
     * Amount of credits per hour per CPU required by this server license.
     * <p>
     *     This property is the sum of all the attached storages' license properties.
     * </p>
     */
    Integer license
    /**
     * Amount of main memory on this server.
     */
    String memoryAmount
    /**
     * Type of network interface card on this server.
     */
    String nicModel
    /**
     * Name of the preconfigured server plan, or "custom", for this server.
     */
    String plan
    /**
     * Server state.
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#server-states" target="_top">UpCloud API docs for server states</a>
     */
    String state
    /**
     * Storage devices attached to this server.
     */
    List<StorageDevice> storageDevices
    /**
     * Tags assigned to this server.
     */
    List<String> tags
    /**
     * The hardware clock timezone for this server.
     */
    String timezone
    /**
     * Title of this server.
     */
    String title
    /**
     * Unique identifier of this server.
     */
    String uuid
    /**
     * Type of video card attached to this server.
     */
    String videoModel
    /**
     * Whether VNC is enabled ({@code "on"}) on this server.
     */
    String vnc
    /**
     * Hostname where VNC is available for this server.
     */
    String vncHost
    /**
     * VNC password.
     */
    String vncPassword
    /**
     * TCP port number on {@link #vncHost} where the VNC server is listening for this server.
     */
    String vncPort
    /**
     * Zone ID where this server is located.
     */
    String zone
}

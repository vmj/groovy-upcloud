package fi.linuxbox.upcloud.resource

import groovy.transform.InheritConstructors

import fi.linuxbox.upcloud.core.*

/**
 * Server details.
 * <p>
 *     A list of these is typically fetched from {@link fi.linuxbox.upcloud.api.UpCloud#servers(def) Servers API}.
 * </p>
 */
@InheritConstructors
class Server extends Resource {
    /**
     * Number of CPU cores on this server.
     */
    String coreNumber
    /**
     * Hostname of this server.
     */
    String hostname
    /**
     * License (see pricing).
     */
    Integer license
    /**
     * Amount of main memory on this server.
     */
    String memoryAmount
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
     * Tags assigned to this server.
     */
    List<String> tags
    /**
     * Title of this server.
     */
    String title
    /**
     * Unique identifier of this server.
     */
    String uuid
    /**
     * Zone ID where this server is located.
     */
    String zone
}

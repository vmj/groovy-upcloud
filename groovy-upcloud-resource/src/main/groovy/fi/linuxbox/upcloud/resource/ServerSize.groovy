package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * An available server configuration.
 * <p>
 *     A list of these is typically fetched from {@link fi.linuxbox.upcloud.api.UpCloud#serverSizes(def) Server Sizes API}.
 * </p>
 */
@InheritConstructors
class ServerSize extends Resource {
    /**
     * CPU core count.
     */
    String coreNumber
    /**
     * main memory amount
     */
    String memoryAmount
}

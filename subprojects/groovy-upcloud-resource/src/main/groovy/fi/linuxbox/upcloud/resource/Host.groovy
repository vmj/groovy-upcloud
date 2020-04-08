package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Private cloud host information.
 *
 */
@CompileStatic
@InheritConstructors
class Host extends Resource {
    /**
     * Host identifier.
     */
    Long id
    /**
     * Host description.
     */
    String description
    /**
     * Either {@code yes} or {@code no}.
     */
    String windowsEnabled
    /**
     * Name of the private cloud this host is on.
     */
    String zone
    /**
     * Statistics like CPU idle or memory free.
     */
    List<Stat> stats
}

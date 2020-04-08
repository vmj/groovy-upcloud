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
     * Identifier of a private cloud host.
     */
    Long id
    /**
     * Description of a private cloud host.
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
     * Statistics about a private cloud host.
     * <p>
     *     Note that there may be multiple statistics with the same name; their timestamps should be different, though.
     * </p>
     */
    List<Stat> stats
}

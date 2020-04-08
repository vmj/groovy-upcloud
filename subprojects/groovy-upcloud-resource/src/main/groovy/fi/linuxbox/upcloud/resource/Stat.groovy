package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * A statistic about a {@link Host}.
 */
@CompileStatic
@InheritConstructors
class Stat extends Resource {
    /**
     * Either {@code cpu_idle} or {@code memory_free}.
     */
    String name
    /**
     * Value of this statistic.
     * <p>
     *     For CPU idle statistic, average percentage over the last 15 minutes from the timestamp.
     * </p>
     * <p>
     *     For memory free statistic, gigabytes free at the timestamp.
     * </p>
     */
    BigDecimal value
    /**
     * Timestamp of this statistic.
     */
    String timestamp
}

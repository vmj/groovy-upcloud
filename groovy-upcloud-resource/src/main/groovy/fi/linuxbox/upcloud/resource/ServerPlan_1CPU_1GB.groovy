package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Fixed server configuration price for 1CPU/1GB plan for a {@link Zone}.
 */
@InheritConstructors
class ServerPlan_1xCPU_1GB extends Resource {
    /**
     *
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} ? per hour.
     */
    BigDecimal price
}

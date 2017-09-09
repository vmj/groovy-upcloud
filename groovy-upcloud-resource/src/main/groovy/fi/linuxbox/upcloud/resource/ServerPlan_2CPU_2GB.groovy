package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Fixed server configuration price for 2CPU/2GB plan for a {@link Zone}.
 */
@InheritConstructors
class ServerPlan_2xCPU_2GB extends Resource {
    /**
     *
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} ? per hour.
     */
    BigDecimal price
}

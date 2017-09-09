package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Price per CPU core for a {@link Zone}.
 */
@InheritConstructors
class ServerCore extends Resource {
    /**
     * Amount in CPU cores.
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} CPU cores per hour.
     */
    BigDecimal price
}

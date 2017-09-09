package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Price per MB of RAM for a {@link Zone}.
 */
@InheritConstructors
class ServerMemory extends Resource {
    /**
     * Amount in megabytes of RAM.
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} MB per hour.
     */
    BigDecimal price
}

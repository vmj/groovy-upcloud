package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * I/O request price information for HDD storage for a {@link Zone}.
 */
@InheritConstructors
class IoRequestHdd extends Resource {
    /**
     * Amount in IO operations.
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} IO operations per hour.
     */
    BigDecimal price
}

package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * IPv4 address price for a {@link Zone}.
 */
@InheritConstructors
class Ipv4Address extends Resource {
    /**
     * Amount of addresses.
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} addresses per hour.
     */
    BigDecimal price
}

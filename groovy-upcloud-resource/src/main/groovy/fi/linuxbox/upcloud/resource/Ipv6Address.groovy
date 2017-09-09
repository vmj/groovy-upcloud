package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * IPv6 address price for a {@link Zone}.
 */
@InheritConstructors
class Ipv6Address extends Resource {
    /**
     * Amount of addresses.
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} addresses per hour.
     */
    BigDecimal price
}

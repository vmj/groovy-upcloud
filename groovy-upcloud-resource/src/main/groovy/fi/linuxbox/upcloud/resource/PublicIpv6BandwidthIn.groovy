package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Inbound IPv6 traffic price information for a {@link Zone}.
 */
@InheritConstructors
class PublicIpv6BandwidthIn extends Resource {
    /**
     *
     */
    Integer amount
    /**
     *
     */
    BigDecimal price
}

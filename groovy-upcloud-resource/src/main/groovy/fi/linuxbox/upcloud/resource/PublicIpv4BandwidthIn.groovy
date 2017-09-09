package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Inbound IPv4 traffic price information for a {@link Zone}.
 */
@InheritConstructors
class PublicIpv4BandwidthIn extends Resource {
    /**
     *
     */
    Integer amount
    /**
     *
     */
    BigDecimal price
}

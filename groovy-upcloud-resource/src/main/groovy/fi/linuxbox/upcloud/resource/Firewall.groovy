package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Firewall price information for a {@link Zone}.
 */
@InheritConstructors
class Firewall extends Resource {
    /**
     *
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} firewalls per hour.
     */
    BigDecimal price
}

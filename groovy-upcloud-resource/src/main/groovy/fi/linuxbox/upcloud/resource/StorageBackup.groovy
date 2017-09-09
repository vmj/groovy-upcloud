package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Price per GB of backup storage for a {@link Zone}.
 */
@InheritConstructors
class StorageBackup extends Resource {
    /**
     * Amount in gigabytes.
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} GB per hour.
     */
    BigDecimal price
}

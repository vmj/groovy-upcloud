package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * An attached {@link Storage} on a {@link Server}.
 * <p>
 *     A list of these is available in the {@link Server#storageDevices} property when fetching the detailed server
 *     information from the {@link fi.linuxbox.upcloud.api.Server#load(def)} API.
 * </p>
 */
@InheritConstructors
class StorageDevice extends Resource {
    /**
     * Hardware address where the storage is attached on the server.
     */
    String address
    /**
     * Whether the storage is included in fixed plan price of the server ({@code yes}).
     */
    String partOfPlan
    /**
     * Unique identifier of the {@link Storage}.
     */
    String storage
    /**
     * Storage size in gigabytes.
     */
    Integer storageSize
    /**
     * Storage title.
     */
    String storageTitle
    /**
     * Type of the storage: {@code disk}, ...
     */
    String type
}

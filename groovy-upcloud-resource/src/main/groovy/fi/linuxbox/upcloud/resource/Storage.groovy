package fi.linuxbox.upcloud.resource

import groovy.transform.InheritConstructors

import fi.linuxbox.upcloud.core.*

/**
 * Storage representation.
 * <p>
 *     A list of storages, with the most relevant information, is typically fetched from
 *     {@link fi.linuxbox.upcloud.api.UpCloud#storages(def) Storages API}.
 * </p>
 */
@InheritConstructors
class Storage extends Resource {
    /**
     * Storage access type: {@code public} or {@code private}.
     * <p>
     *     This is available in the storage list response.
     * </p>
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#storage-access-types" target="_top">UpCloud API docs for storage access types</a>
     */
    String access
    /**
     * Amount of credits per hour per CPU required by this storage license.
     * <p>
     *     This is available in the storage list response.
     * </p>
     */
    Integer licence
    /**
     * Size of this storage in gigabytes.
     * <p>
     *     This is available in the storage list response.
     * </p>
     */
    Integer size
    /**
     * Storage state: {@code online}, {@code maintenance}, {@code cloning}, {@code backuping}, or {@code error}.
     * <p>
     *     This is available in the storage list response.
     * </p>
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#storage-states" target="_top">UpCloud API docs for storage states</a>
     */
    String state
    /**
     * Storage tier: {@code hdd}, or {@code maxiops}.
     * <p>
     *     This is available in the storage list response.
     * </p>
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#storage-tiers" target="_top">UpCloud API docs for storage tiers</a>
     */
    String tier
    /**
     * Title of this storage.
     * <p>
     *     This is available in the storage list response.
     * </p>
     */
    String title
    /**
     * Storage type: {@code disk}, {@code cdrom}, {@code template}, or {@code backup}.
     * <p>
     *     This is available in the storage list response.
     * </p>
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#storage-types" target="_top">UpCloud API docs for storage types</a>
     */
    String type
    /**
     * Unique identifier of this storage.
     * <p>
     *     This is available in the storage list response.
     * </p>
     */
    String uuid
    /**
     * Zone ID where this storage is located.
     * <p>
     *     This is available in the storage list response..
     *     See {@link fi.linuxbox.upcloud.api.UpCloud#zones(def)}.
     * </p>
     */
    String zone
}

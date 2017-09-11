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
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to next available slot.
     * </p>
     * <p>
     *     Possible values are:
     * </p>
     * <ul>
     *     <li>{@code ide:0:0}</li>
     *     <li>{@code ide:0:1}</li>
     *     <li>{@code ide:1:0}</li>
     *     <li>{@code ide:1:1}</li>
     *     <li>{@code scsi:0:0}</li>
     *     <li>{@code scsi:0:1}</li>
     *     <li>{@code scsi:0:2}</li>
     *     <li>{@code scsi:0:3}</li>
     *     <li>{@code scsi:0:4}</li>
     *     <li>{@code scsi:0:5}</li>
     *     <li>{@code scsi:0:6}</li>
     *     <li>{@code scsi:0:7}</li>
     *     <li>{@code virtio:0}</li>
     *     <li>{@code virtio:1}</li>
     *     <li>{@code virtio:2}</li>
     *     <li>{@code virtio:3}</li>
     *     <li>{@code virtio:4}</li>
     *     <li>{@code virtio:5}</li>
     *     <li>{@code virtio:6}</li>
     *     <li>{@code virtio:7}</li>
     * </ul>
     */
    String address
    /**
     * Whether the storage is included in fixed plan price of the server ({@code yes}).
     * <p>
     *     This is available on a server details response. This can not be set when creating a server.
     * </p>
     */
    String partOfPlan
    /**
     * Unique identifier of the {@link Storage}.
     * <p>
     *     This is available on a server details response, where this corresponds to the attached storage.
     * </p>
     * <p>
     *     When creating a server, this is required for storage devices with {@link #action} {@code clone} or
     *     {@code attach}, and not applicable for {@link #action} {@code create}.  For cloning, this would correspond
     *     to the template or server disk that is to be cloned.  The new clone UUID will be returned in the response.
     *     For attaching, this would correspond to the CD-ROM UUID.
     * </p>
     */
    String storage
    /**
     * Storage size in gigabytes.
     * <p>
     *     This is available on a server details response. When creating a server from template or by installing it
     *     from a CD-ROM, use {@link #size} property instead.
     * </p>
     */
    Integer storageSize
    /**
     * Storage title.
     * <p>
     *     This is available on a server details response. When creating a server, use {@link #title} property
     *     instead.
     * </p>
     */
    String storageTitle
    /**
     * Type of the storage: {@code disk} or {@code cdrom}.
     * <p>
     *     This is available on a server details response. This should not be used when creating a server from
     *     template or by cloning another server.  When creating a server by installing it from a CD-ROM, this
     *     can be set to {@code cdrom} on the storage device that attaches the CD-ROM.
     * </p>
     */
    String type

    /**
     * The operation when creating a server: {@code clone}, {@code create}, or {@code attach}.
     * <p>
     *     This should be set to {@code clone} when creating a server from template or by cloning another server.
     *     In these cases, the {@link #storage} should be set to the {@link Storage#uuid} of the cloned template
     *     or server disk.  The cloned storage UUID will be returned in the response.
     * </p>
     * <p>
     *     When creating a server by installing it from a CD-ROM, one storage device (the new empty disk) should have
     *     this set to {@code create} (and no {@link #storage}), and another storage device (the CD-ROM) should have
     *     this set to {@code attach} (and {@link #storage} set to the {@link Storage#uuid} of the CD-ROM}).
     * </p>
     */
    String action
    /**
     * Title of the created storage.
     * <p>
     *     This can optionally be set when creating storage(s) for a new server (where {@code action} is either
     *     {@code clone} or {@code create}), instead of {@link #storageTitle}.
     * </p>
     */
    String title
    /**
     * Size of the created storage in gigabytes.
     * <p>
     *     When cloning a template or another server disk, this can optionally be set and defaults to the original
     *     size.  When creating a new storage, this must be set.  The newly created storage size will be returned in
     *     the {@link #storageSize} property in the server details response.
     * </p>
     */
    Integer size // FIXME: String in "create from CD-ROM" use case; does it matter?
    /**
     * On which storage tier the storage should be placed: {@code hdd} or {@code maxiops}.
     * <p>
     *     This can optionally be set when creating storage(s) for a new server (where {@code action} is either
     *     {@code clone} or {@code create}), and defaults to {@code hdd}.
     * </p>
     */
    String tier
}

/*
 * Groovy UpCloud library - Resource Module
 * Copyright (C) 2018  <mikko@varri.fi>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fi.linuxbox.upcloud.resource

import groovy.transform.InheritConstructors

import fi.linuxbox.upcloud.core.*

/**
 * Storage representation.
 * <p>
 *     A list of storages, with the most relevant information, is typically fetched from
 *     {@link fi.linuxbox.upcloud.api.UpCloud#storages(def) Storages API}.  A more detailed
 *     information about a specific storage can be fetched from
 *     {@link fi.linuxbox.upcloud.api.Storage#load(def) Storage details API}.
 * </p>
 * <p>
 *     When creating a storage, one would typically use
 *     {@link fi.linuxbox.upcloud.resource.Builder#storage(groovy.lang.Closure) Builder API} to define an instance of
 *     a storage, and then pass it to
 *     {@link fi.linuxbox.upcloud.api.UpCloud#create(fi.linuxbox.upcloud.core.Resource, def) Resource creation API}
 *     to actually create the storage.
 * </p>
 * <h4>Creating a storage</h4>
 * <p>
 *     To create a storage, the only required properties are {@link #size}, {@link #title}, and {@link #zone}.
 * </p>
 * <pre><code class="groovy">
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def staticFiles = storage {
 *         size = '10'
 *         title = 'frontend server 1 static files'
 *         zone = 'fi-hel1'
 *     }
 * </code></pre>
 * <p>
 *     There are a few optional properties that can be set.
 * <pre><code class="groovy">
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def staticFiles = storage {
 *         // skipping mandatory properties and showing only
 *         // optional properties with their default values:
 *         tier = 'hdd'
 *         backupRule = null
 *     }
 * </code></pre>
 * <p>
 *     There are a few properties that can not be set when creating a storage: {@link #access}, {@link #backups},
 *     {@link #licence}, {@link #servers}, {@link #state}, {@link #type}, and {@link #uuid}.  Most of these can not
 *     be manipulated from client side at all.
 * </p>
 */
@InheritConstructors
class Storage extends Resource {
    /**
     * Storage access type: {@code public} or {@code private}.
     * <p>
     *     This is available in the storage list and details responses.  This can not be specified when creating,
     *     modifying, or cloning a storage, since all user created storages are private.
     * </p>
     * @see <a href="https://www.upcloud.com/api/9-storages/#storage-access-types" target="_top">UpCloud API docs for storage access types</a>
     */
    String access
    /**
     * Storage backup rule.
     * <p>
     *     This is available in the storage details response.  This can optionally be specified when creating or
     *     modifying a storage.  When creating a storage, this defaults to {@code null} (no backup rule).  When
     *     modifying a storage, leaving this unspecified means that any existing backup rule will stay unaffected.
     *     This can not be specified when cloning a storage.
     * </p>
     */
    BackupRule backupRule
    /**
     * Storage UUIDs of the backups for this storage.
     * <p>
     *     This is available in the storage details response.  This can not be specified when creating, modifying, or
     *     cloning a storage.
     * </p>
     */
    List<String> backups
    /**
     * Amount of credits per hour per CPU required by this storage license.
     * <p>
     *     This is available in the storage list and details responses.  This can not be specified when creating,
     *     modifying, or cloning a storage.
     * </p>
     */
    Integer licence
    /**
     * Server UUIDs of servers where this storage is attached.
     * <p>
     *     This is available in the storage details response.  This can not be specified when creating, modifying, or
     *     cloning a storage.
     * </p>
     * <p>
     *     A storage can be attached to a server, or detached from a server, via
     *     {@link fi.linuxbox.upcloud.api.Server#attach(fi.linuxbox.upcloud.core.Resource, def)} or
     *     {@link fi.linuxbox.upcloud.api.Server#detach(fi.linuxbox.upcloud.core.Resource, def)} APIs.
     *     Also, certain kinds of storages can be loaded as CD-ROMs via
     *     {@link fi.linuxbox.upcloud.api.Server#insert(fi.linuxbox.upcloud.core.Resource, def)} API.
     * </p>
     */
    List<String> servers
    /**
     * Size of this storage in gigabytes.
     * <p>
     *     This is available in the storage list and details responses.  This must be specified when creating a
     *     storage.  This can optionally be specified when modifying a storage, but must be larger than the current
     *     size.  Note also, that partition table and filesystem sizes are not updated automatically when resizing
     *     a storage via this property.  This can not be specified when cloning a storage.
     * </p>
     */
    Integer size
    /**
     * Storage state: {@code online}, {@code maintenance}, {@code cloning}, {@code backuping}, or {@code error}.
     * <p>
     *     This is available in the storage list and details responses.  This can not be specified when creating,
     *     modifying, or cloning a storage.
     * </p>
     * @see <a href="https://www.upcloud.com/api/9-storages/#storage-states" target="_top">UpCloud API docs for storage states</a>
     */
    String state
    /**
     * Storage tier: {@code hdd}, or {@code maxiops}.
     * <p>
     *     This is available in the storage list and details responses.  This can optionally be specified when
     *     creating or cloning a storage, where this defaults to {@code hdd}.  This can not be modified.
     * </p>
     * @see <a href="https://www.upcloud.com/api/9-storages/#storage-tiers" target="_top">UpCloud API docs for storage tiers</a>
     */
    String tier
    /**
     * Title of this storage.
     * <p>
     *     This is available in the storage list and details responses.  This must be specified when creating or
     *     cloning a storage, and can optionally be specified when modifying a storage.
     * </p>
     */
    String title
    /**
     * Storage type: {@code disk}, {@code cdrom}, {@code template}, or {@code backup}.
     * <p>
     *     This is available in the storage list and details responses.  This can not be specified when creating,
     *     modifying, or cloning a storage.
     * </p>
     * <p>
     *     When creating or cloning a storage, its type becomes {@code disk}.  Templates can be created via
     *     {@link fi.linuxbox.upcloud.api.Storage#templatize(fi.linuxbox.upcloud.core.Resource, def)} API, and backups
     *     can be created via
     *     {@link fi.linuxbox.upcloud.api.Storage#backup(fi.linuxbox.upcloud.core.Resource, def)} API.
     * </p>
     * @see <a href="https://www.upcloud.com/api/9-storages/#storage-types" target="_top">UpCloud API docs for storage types</a>
     */
    String type
    /**
     * Unique identifier of this storage.
     * <p>
     *     This is available in the storage list and details responses.  This can not be specified when creating,
     *     modifying, or cloning a storage.
     * </p>
     */
    String uuid
    /**
     * Zone ID where this storage is located.
     * <p>
     *     This is available in the storage list and details responses.  This must be specified when creating or
     *     cloning a storage.  This can <strong>not</strong> be specified when modifying a storage.
     *     See {@link fi.linuxbox.upcloud.api.UpCloud#zones(def)}.
     * </p>
     * <p>
     *     Transferring a storage between zones is possible via
     *     {@link fi.linuxbox.upcloud.api.Storage#clone(fi.linuxbox.upcloud.core.Resource, def)} API.
     * </p>
     */
    String zone
}

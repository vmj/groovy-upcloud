/*
 * Groovy UpCloud library - API Module
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
package fi.linuxbox.upcloud.api

import fi.linuxbox.upcloud.core.*

/**
 * Storage related APIs.
 *
 * <p>
 *     This class provides the following APIs:
 * </p>
 * <ul>
 *     <li>loading, modifying, and deleting storages</li>
 *     <li>cloning and templatizing storages</li>
 *     <li>cancelling the cloning operation</li>
 *     <li>creating and restoring backups</li>
 *     <li>adding and removing storages from favorites</li>
 * </ul>
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null HTTP property, which can be read-only</li>
 *     <li>non-null uuid property, which can be read-only</li>
 * </ul>
 */
trait StorageApi {
    abstract HTTPFacade<?> getHTTP()
    abstract String getUuid()

    /**
     * Fetch detailed information about a specific {@link fi.linuxbox.upcloud.resource.Storage}.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Storage}
     *     in the {@code storage} property.
     * </p>
     * <pre><code class="groovy">
     *     storageApi.load { resp, err ->
     *         assert resp?.storage instanceof Storage
     *     }
     * </code></pre>
     * <p>
     *     While this operation returns details of a single storage, a less details list of all storages can be
     *     requested with the {@link UpCloudApi#storages(def)} API.
     * </p>
     *
     * @param args Request callbacks for the {@code GET /storage/&#36;&#123;storage.uuid&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#get-storage-details" target="_top">UpCloud API docs for GET /storage/&#36;{storage.uuid}</a>
     */
    def load(...args) {
        HTTP.GET(storagePath(), *args)
    }

    /**
     * Modifies the configuration of an existing storage.
     * <p>
     *     A {@code 202 Accepted} response will include an instance of {@link fi.linuxbox.upcloud.resource.Storage}
     *     in the {@code storage} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.*
     *
     *     def staticFiles = storage {
     *         size = 20
     *         title = 'A larger storage'
     *     }
     *
     *     storageApi.update staticFiles { resp, err ->
     *         assert resp?.storage instanceof Storage
     *     }
     * </code></pre>
     * @param resource Updated storage resource.
     * @param args Request callbacks for the {@code PUT /storage/&#36;&#123;storage.uuid&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#modify-storage" target="_top">UpCloud API docs for PUT /storage/&#36;{storage.uuid}</a>
     */
    def update(Resource resource, ...args) {
        HTTP.PUT(storagePath(), resource.wrapper(), *args)
    }

    /**
     * Delete an existing storage resource.
     * <p>
     *     The storage can not be attached to any server.
     * </p>
     * <p>
     *     Any backups, clones, or templates created from this storage are not deleted.  Restoring a backup will
     *     recreate this storage.
     * </p>
     * <p>
     *     A {@code 204 No Content} response signifies success.
     * </p>
     * @param args Request callbacks for the {@code DELETE /storage/&#36;&#123;storage.uuid&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#delete-storage" target="_top">UpCloud API docs for DELETE /storage/&#36;{storage.uuid}</a>
     */
    def delete(...args) {
        HTTP.DELETE(storagePath(), *args)
    }

    /**
     * Creates an exact copy of an existing storage resource.
     * <p>
     *     A {@code 201 Created} response will include an instance of {@link fi.linuxbox.upcloud.resource.Storage}
     *     in the {@code storage} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.*
     *
     *     def cloneOfOsDisk = storage {
     *         zone = 'fi-hel1'
     *         tier = 'maxiops'
     *         title = 'Clone of operating system disk'
     *     }
     *
     *     storageApi.clone cloneOfOsDisk { resp, err ->
     *         assert resp?.storage instanceof Storage
     *     }
     * </code></pre>
     * @param resource Specification of the clone
     * @param args Request callbacks for the {@code POST /storage/&#36;&#123;storage.uuid&#125;/clone} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#clone-storage" target="_top">UpCloud API docs for POST /storage/&#36;{storage.uuid}/clone</a>
     */
    def clone(Resource resource, ...args) {
        HTTP.POST(cmdPath('clone'), resource.wrapper(), *args)
    }

    /**
     * Cancels an incomplete cloning operation.
     * <p>
     *     Cancelling the cloning will stop the cloning operation, remove the incomplete new storage and return the
     *     source storage back to online state.
     * </p>
     * <p>
     *     A {@code 204 No Content} response signifies success.
     * </p>
     *
     * @param args Request callbacks for the {@code POST /storage/&#36;&#123;storage.uuid&#125;/cancel} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#cancel-storage-operation" target="_top">UpCloud API docs for POSt /storage/&#36;{storage.uuid}/cancel</a>
     */
    def cancel(...args) {
        HTTP.POST(cmdPath('cancel'), null, *args)
    }

    /**
     * Creates an exact copy of an existing storage resource.
     * <p>
     *     Only storage resources on the {@code maxiops} {@link fi.linuxbox.upcloud.resource.Storage#tier} can be
     *     templatized.
     * </p>
     * <p>
     *     The differences between {@link #clone(fi.linuxbox.upcloud.core.Resource, def) cloning} and templatizing a
     *     resource are that any storage tier can be cloned and cloning can happen across zones, but only templates
     *     can be used when creating a server OS disk from a template.
     * </p>
     * <p>
     *     A {@code 201 Created} response will include an instance of {@link fi.linuxbox.upcloud.resource.Storage}
     *     in the {@code storage} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.*
     *
     *     def myTemplate = storage {
     *         title = 'My server template'
     *     }
     *
     *     storageApi.templatize myTemplate { resp, err ->
     *         assert resp?.storage instanceof Storage
     *     }
     * </code></pre>
     * @param resource Specification of the template
     * @param args Request callbacks for the {@code POST /storage/&#36;&#123;storage.uuid&#125;/templatize} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#templatize-storage" target="_top">UpCloud API docs for POST /storage/&#36;{storage.uuid}/templatize</a>
     */
    def templatize(Resource resource, ...args) {
        HTTP.POST(cmdPath('templatize'), resource.wrapper(), *args)
    }

    /**
     * Creates a backup of an existing storage resource.
     * <p>
     *     A {@code 201 Created} response will include an instance of {@link fi.linuxbox.upcloud.resource.Storage}
     *     in the {@code storage} property.
     * </p>
     * <pre><code class="groovy">
     *     import static fi.linuxbox.upcloud.resource.Builder.*
     *
     *     def myBackup = storage {
     *         title = 'Manually created backup'
     *     }
     *
     *     storageApi.backup myBackup { resp, err ->
     *         assert resp?.storage instanceof Storage
     *     }
     * </code></pre>
     * @param resource Specification of the backup
     * @param args Request callbacks for the {@code POST /storage/&#36;&#123;storage.uuid&#125;/backup} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#create-backup" target="_top">UpCloud API docs for POST /storage/&#36;{storage.uuid}/backup</a>
     */
    def backup(Resource resource, ...args) {
        HTTP.POST(cmdPath('backup'), resource.wrapper(), *args)
    }

    /**
     * Restores the origin storage from this backup.
     * <p>
     *     If the origin storage is attached to a server, the server must first be stopped or the storage has to be
     *     detached.
     * </p>
     * <p>
     *     A {@code 204 No Content} response signifies success.
     * </p>
     * @param args Request callbacks for the {@code POST /storage/&#36;&#123;storage.uuid&#125;/restore} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#restore-backup" target="_top">UpCloud API docs for POST /storage/&#36;{storage.uuid}/restore</a>
     */
    def restore(...args) {
        HTTP.POST(cmdPath('restore'), null, *args)
    }

    /**
     * Adds the storage to the favorites.
     * <p>
     *     This operation succeeds even if the storage already is a favorite.
     * </p>
     * <p>
     *     Favorite storages can be listed with {@link UpCloudApi#storages(def) Storages API},
     *     by specifying a keyword argument {@code type: "favorite"}.
     * </p>
     * <p>
     *     A {@code 204 No Content} response signifies success.
     * </p>
     * @param args Request callbacks for the {@code POST /storage/&#36;&#123;storage.uuid&#125;/favorite} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#add-storage-to-favorites" target="_top">UpCloud API docs for POST /storage/&#36;{storage.uuid}/favorite</a>
     */
    def favor(...args) {
        HTTP.POST(cmdPath('favorite'), null, *args)
    }

    /**
     * Removes the storage from the favorites.
     * <p>
     *     This operation succeeds even if the storage is not a favorite.
     * </p>
     * <p>
     *     Favorite storages can be listed with {@link UpCloudApi#storages(def) Storages API},
     *     by specifying a keyword argument {@code type: "favorite"}.
     * </p>
     * <p>
     *     A {@code 204 No Content} response signifies success.
     * </p>
     * @param args Request callbacks for the {@code DELETE /storage/&#36;&#123;storage.uuid&#125;/favorite} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/9-storages/#remove-storage-from-favorites" target="_top">UpCloud API docs for DELETE /storage/&#36;{storage.uuid}/favorite</a>
     */
    def unfavor(...args) {
        HTTP.DELETE(cmdPath('favorite'), *args)
    }


    private String storagePath()       { "storage/$uuid" }
    private String cmdPath(String cmd) { "${storagePath()}/$cmd" }
}

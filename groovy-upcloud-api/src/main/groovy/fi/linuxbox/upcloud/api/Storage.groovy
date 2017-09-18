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
 *     <li>creating and restoring backups</li>
 *     <li>adding and removing storages from favorites</li>
 * </ul>
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null SESSION property, which can be read-only</li>
 *     <li>non-null uuid property, which can be read-only</li>
 * </ul>
 */
trait Storage {

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
     *     requested with the {@link UpCloud#storages(def)} API.
     * </p>
     *
     * @param args Request callbacks for the {@code GET /storage/&#36;&#123;storage.uuid&#125;} call.
     * @return Whatever is returned by the {@link Session} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/1.2.4/9-storages/#get-storage-details" target="_top">UpCloud API docs for GET /storage/&#36;{storage.uuid}</a>
     */
    def load(...args) {
        this.SESSION.GET(storagePath(), *args)
    }

    def update(Resource resource, ...args) {
        this.SESSION.PUT(storagePath(), resource.wrapper(), *args)
    }

    def delete(...args) {
        this.SESSION.DELETE(storagePath(), *args)
    }

    def clone(Resource resource, ...args) {
        this.SESSION.POST(cmdPath('clone'), resource.wrapper(), *args)
    }

    def templatize(Resource resource, ...args) {
        this.SESSION.POST(cmdPath('templatize'), resource.wrapper(), *args)
    }

    def backup(Resource resource, ...args) {
        this.SESSION.POST(cmdPath('backup'), resource.wrapper(), *args)
    }

    def restore(...args) {
        this.SESSION.POST(cmdPath('restore'), null, *args)
    }

    def favor(...args) {
        this.SESSION.POST(cmdPath('favorite'), null, *args)
    }

    def unfavor(...args) {
        this.SESSION.DELETE(cmdPath('favorite'), *args)
    }


    private String storagePath()       { "storage/$uuid" }
    private String cmdPath(String cmd) { "${storagePath()}/$cmd" }
}

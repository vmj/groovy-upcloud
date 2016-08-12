package fi.linuxbox.upcloud.api

import groovy.transform.*

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
 *     <li>non-null API property, which can be read-only</li>
 *     <li>non-null uuid property, which can be read-only</li>
 *     <li>wrapper method (only needed in clone, backup, and templatize)</li>
 * </ul>
 */
@SelfType(Resource) // must have uuid property
trait Storage {

    def load(...args) {
        this.API.GET(storagePath(), *args)
    }

    def update(Resource resource, ...args) {
        this.API.PUT(storagePath(), resource.wrapper(), *args)
    }

    def delete(...args) {
        this.API.DELETE(storagePath(), *args)
    }

    def clone(...args) {
        this.API.POST(cmdPath('clone'), wrapper(), *args) // TODO: clone, templatize, backup: maybe take the storage body as arg
    }

    def templatize(...args) {
        this.API.POST(cmdPath('templatize'), wrapper(), *args)
    }

    def backup(...args) {
        this.API.POST(cmdPath('backup'), wrapper(), *args)
    }

    def restore(...args) {
        this.API.POST(cmdPath('restore'), null, *args)
    }

    def favor(...args) {
        this.API.POST(cmdPath('favorite'), null, *args)
    }

    def unfavor(...args) {
        this.API.DELETE(cmdPath('favorite'), *args)
    }


    private String storagePath()       { "storage/$uuid" }
    private String cmdPath(String cmd) { "${storagePath()}/$cmd" }
}

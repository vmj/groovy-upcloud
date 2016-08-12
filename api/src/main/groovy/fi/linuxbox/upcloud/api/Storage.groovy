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
 *     <li>CRUD operations for storages</li>
 *     <li>cloning, and templatizing storages</li>
 *     <li>creating and restoring backups</li>
 *     <li>adding and removing storages from favorites</li>
 * </ul>
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null API property, which can be read-only</li>
 *     <li>non-null uuid property, which can be read-only (creation does not need it, though)</li>
 *     <li>wrapper method (only needed in create and update, and clone, backup, and templatize)</li>
 * </ul>
 */
@SelfType(Resource) // must have uuid property
trait Storage {

    def create(...args) {
        this.API.POST(storagesPath(), wrapper(), *args)
    }

    def load(...args) {
        this.API.GET(storagePath(), *args)
    }

    def update(...args) {
        this.API.PUT(storagePath(), wrapper(), *args) // FIXME: update issue: uuid needed for URL, but should not be PUT in repr
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


    private String storagesPath()      { 'storage/' }
    private String storagePath()       { "${storagesPath()}$uuid" }
    private String cmdPath(String cmd) { "${storagePath()}/$cmd" }
}

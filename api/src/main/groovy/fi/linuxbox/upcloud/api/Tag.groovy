package fi.linuxbox.upcloud.api

import groovy.transform.SelfType

import fi.linuxbox.upcloud.core.Resource

/**
 * Tag related APIs.
 * <p>
 *     This class provides following APIs:
 * </p>
 * <lu>
 *     <li>modifying and deleting tags</li>
 * </ul>
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null API property, which can be read-only</li>
 *     <li>non-null name property, which can be read-only</li>
 *     <li>wrapper method (only needed in update)</li>
 * </ul>
 */
@SelfType(Resource) // must have name property
trait Tag {

    def update(...args) {
        this.API.PUT(tagPath(), wrapper(), *args) // FIXME: update issue: old tag name required for URL, but new for PUT repr
    }

    def delete(...args) {
        this.API.DELETE(tagPath(), *args)
    }

    private String tagPath() { "tag/$name" }
}

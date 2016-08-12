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
 * </ul>
 */
@SelfType(Resource) // must have name property
trait Tag {

    def update(Resource resource, ...args) {
        this.API.PUT(tagPath(), resource.wrapper(), *args)
    }

    def delete(...args) {
        this.API.DELETE(tagPath(), *args)
    }

    private String tagPath() { "tag/$name" }
}

package fi.linuxbox.upcloud.api

import groovy.transform.SelfType

import fi.linuxbox.upcloud.core.Resource

/**
 * Tag related APIs.
 * <p>
 *     This class provides following APIs:
 * </p>
 * <lu>
 *     <li>creating, modifying, and deleting tags</li>
 * </ul>
 */
@SelfType(Resource) // must have name property
trait Tag {

    def create(...args) {
        this.API.POST(tagsPath(), wrapper(), *args)
    }

    def update(...args) {
        this.API.PUT(tagPath(), wrapper(), *args) // FIXME: update issue: old tag name required for URL, but new for PUT repr
    }

    def delete(...args) {
        this.API.DELETE(tagPath(), *args)
    }

    private String tagsPath() { 'tag/' }
    private String tagPath() { "${tagsPath()}$name" }
}
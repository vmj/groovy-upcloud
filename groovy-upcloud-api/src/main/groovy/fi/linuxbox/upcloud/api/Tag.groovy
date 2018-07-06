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
 * Tag related APIs.
 * <p>
 *     This class provides following APIs:
 * </p>
 * <ul>
 *     <li>modifying and deleting tags</li>
 * </ul>
 * <p>
 *     This trait can be implemented by any class that has
 * </p>
 * <ul>
 *     <li>non-null HTTP property, which can be read-only</li>
 *     <li>non-null name property, which can be read-only</li>
 * </ul>
 */
trait Tag {
    abstract AbstractSession<?> getHTTP()
    abstract String getName()

    /**
     * Changes properties of an existing tag.
     * <p>
     *     A {@code 200 OK} response will include an instance of {@link fi.linuxbox.upcloud.resource.Tag} in the
     *     {@code tag} property.
     * </p>
     * @param resource Description of the updated tag
     * @param args Request callbacks for the {@code PUT /tag/&#36;&#123;tag.name&#125;} call.
     * @return Whatever is returned by the {@link AbstractSession} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/12-tags/#modify-existing-tag" target="_top">UpCloud API docs for PUT /tag/&#36;{tag.name}</a>
     */
    def update(Resource resource, ...args) {
        HTTP.PUT(tagPath(), resource.wrapper(), *args)
    }

    /**
     * Delete an existing tag.
     * <p>
     *     A {@code 204 No Content} response signifies success.
     * </p>
     * <p>
     *     Deleting a tag will automatically remove that tag from any servers.
     * </p>
     * @param args Request callbacks for the {@code DELETE /tag/&#36;&#123;tag.name&#125;} call.
     * @return Whatever is returned by the {@link AbstractSession} for starting an asynchronous request.
     * @see <a href="https://www.upcloud.com/api/12-tags/#delete-tag" target="_top">UpCloud API docs for DELETE /tag/&#36;{tag.name}</a>
     */
    def delete(...args) {
        HTTP.DELETE(tagPath(), *args)
    }

    private String tagPath() { "tag/$name" }
}

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

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * UpCloud API error.
 * <p>
 *     Whenever there's a problem with the API, it will respond with HTTP status in the range {@code 400}-{@code 599},
 *     and an instance of this class in the {@code error} property of the passed in {@link Resource}.
 *     This is different from network errors, which do not have HTTP status codes: they come back to the application
 *     as {@link Throwable}s.
 * </p>
 * <pre><code class="groovy">
 *     import fi.linuxbox.upcloud.core.Resource
 *     import fi.linuxbox.upcloud.resource.Error
 *
 *     upcloud.account { Resource resp, Throwable err ->
 *         if (err) {
 *             // The err.message is something like:
 *             //   - "failed to start http exchange (GET /1.2/account)"
 *             //   - "failed to finish http exchange (GET /1.2/account)"
 *             //   - "cancelled http exchange (GET /1.2/account)"
 *             log.fatal("Network error", err)
 *         } else if (resp.error) {
 *             // "HTTP error: 401 Unauthorized (GET /1.2/account)"
 *             log.error "HTTP error: ${resp.META}"
 *             // "Details: Error(errorCode: xxx, errorMessage: yyy)"
 *             log.error "Details: ${resp.error}"
 *         } else {
 *             // "Got: Resource(account: Account(...))"
 *             log.info "Got: ${resp}"
 *         }
 *     })
 * </code></pre>
 */
@InheritConstructors
class Error extends Resource {
    /**
     * Human-readable error description.
     * <p>
     *     This can include dynamic content, such as resource identifier, in order to help track down the source
     *     of the error.
     * </p>
     */
    String errorMessage
    /**
     * A more specific error code.
     */
    String errorCode
}

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
 *     This is different from {@link fi.linuxbox.upcloud.http.spi.ERROR network errors}, which do not have HTTP status
 *     codes.
 * </p>
 * <pre><code class="groovy">
 *     import fi.linuxbox.upcloud.core.Resource
 *     import fi.linuxbox.upcloud.http.spi.ERROR
 *     import fi.linuxbox.upcloud.resource.Error
 *
 *     upcloud.account(
 *         error: { Resource resp ->
 *             log.error "HTTP error: ${resp.META}"
 *             log.error "Details: ${resp.error.errorCode}: ${resp.error.errorMessage}"
 *         },
 *         { Resource resp, ERROR err ->
 *             if (err) {
 *                 log.fatal "Network error: ${err.message}"
 *                 Throwable cause = err.cause
 *                 while (cause != null) {
 *                     log.fatal "Caused by: ${cause.class.simpleName}: ${cause.message}"
 *                     if (cause == cause.cause)
 *                         break
 *                     cause = cause.cause
 *                 }
 *             } else {
 *
 *             }
 *         })
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

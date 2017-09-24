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

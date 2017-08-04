package fi.linuxbox.upcloud.core.callback

import fi.linuxbox.upcloud.core.Resource
import fi.linuxbox.upcloud.http.spi.ERROR
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.function.BiConsumer

import static java.util.Collections.unmodifiableMap

/**
 * A helper class for unmodifiable set of callbacks, and a way to call one
 * of them based on HTTP response status code.
 */
@CompileStatic
class RequestCallback extends Callbacks implements BiConsumer<Resource, ERROR> {
    private final Logger log = LoggerFactory.getLogger(RequestCallback)

    /**
     * An unmodifiable copy of the session callbacks.
     * <p>
     *     This is taken at the time of request initialization, and any
     *     modifications to the session callbacks during the HTTP exchange
     *     have no affect on this copy.
     * </p>
     */
    private final Map<String, Closure<Void>> sessionCallbacks

    /**
     * An unmodifiable copy of the request callbacks.
     * <p>
     *     This is taken at the time of request initialization, and any
     *     modifications to the original map given to the API
     *     have no affect on this copy.
     * </p>
     */
    private final Map<String, Closure<Void>> requestCallbacks

    /**
     * Default request callback.
     * <p>
     *     This should be capable of handling network errors, i.e. the cases
     *     when we do not get an HTTP status code or category.
     * </p>
     */
    private final Closure<Void> defaultRequestCallback

    /**
     * Constructor.
     * <p>
     *     Makes unmodifiable copies of the given maps.
     * </p>
     *
     * @param sessionCallbacks
     * @param requestCallbacks
     * @param defaultRequestCallback
     */
    RequestCallback(final SessionCallbacks sessionCallbacks,
                    final Map<?, Closure<Void>> requestCallbacks,
                    final Closure<Void> defaultRequestCallback) {
        this.sessionCallbacks = sessionCallbacks.asUnmodifiableMap()
        this.requestCallbacks = unmodifiableMap(internalize(requestCallbacks))
        this.defaultRequestCallback = defaultRequestCallback
    }

    /**
     * Chooses the callback and calls it.
     *
     * @param resource Resource to pass to the callback (or null)
     * @param error Error to pass to the callback (or null)
     */
    @Override
    void accept(final Resource resource, final ERROR error) {
        final Integer status = resource?.META?.status
        final Closure<Void> callback = chooseCallback(status)
        callApp(callback, resource, error)
    }

    /**
     * Calls back to the app.
     * <p>
     *     Tries to accommodate to the number of arguments expected by the app, and shields the HTTP implementation
     *     from having to deal with exceptions.
     * </p>
     *
     * @param callback Selected callback.
     * @param resource HTTP response or <code>null</code>
     * @param err ERROR or <code>null</code>
     */
    private void callApp(final Closure<Void> callback, final Resource resource, final ERROR err) {
        try {
            if (callback.maximumNumberOfParameters == 2)
                callback(resource, err)
            else
                callback(resource)
        } catch (final MissingMethodException e) {
            // this is error because we couldn't reach the app
            log.error("application callback has wrong signature", e)
        } catch (final Exception e) {
            // this is "only" warning because we reached the app but it failed
            log.warn("application callback threw an exception", e)
        }
    }

    /**
     * Return a callback for the given status.
     *
     * @param statusCode The response status, an exact number like 200.
     * @param defaultRequestCallback The default request callback.
     * @param requestCallbacks The request callbacks.
     * @return A callback corresponding to the response status.
     */
    private Closure<Void> chooseCallback(final Integer statusCode) {
        if (!statusCode)
            return defaultRequestCallback

        // Try exact match to the status code first
        Closure<Void> callback = selectCallback(statusCode.toString())
        if (callback)
            return callback

        // Try one of the broad categories
        callback = HTTP_STATUS_CATEGORIES.findResult { final IntRange range, final String category ->
            selectCallback(statusCode, range, category)
        }
        if (callback)
            return callback

        // Fall back to default request callback
        return defaultRequestCallback
    }

    /**
     * Return a callback for the given category.
     *
     * <p>
     * If the given {@param statusCode} does not match the given {@param range},
     * <code>null</code> is returned.  Otherwise, the given {@param category}
     * is used to search for a callback.  If the category is not found from the
     * request callbacks, then the session callbacks is checked.
     * </p>
     *
     * @param statusCode The HTTP response status code.
     * @param statusRange A status category range, like (200..299).
     * @param statusCategory A status category name, like "success".
     * @return A callback corresponding to the response status, or null.
     */
    private Closure<Void> selectCallback(final Integer statusCode,
                                         final IntRange statusRange,
                                         final String statusCategory) {
        statusCode in statusRange ? selectCallback(statusCategory) : null
    }

    /**
     * Return a callback for the given status or category.
     *
     * <p>
     * A callback from additional request callbacks is returned if found.
     * Otherwise, a callback from session callbacks is returned if it exists.
     * </p>
     *
     * @param selector The HTTP response status code or category.
     * @return A callback corresponding to the selector, or null.
     */
    private Closure<Void> selectCallback(final String selector) {
        requestCallbacks[selector] ?: sessionCallbacks[selector]
    }
}

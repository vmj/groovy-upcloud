/*
 * Groovy UpCloud library - Core Module
 * Copyright (C) 2018  Mikko Värri
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
package fi.linuxbox.upcloud.core.callback

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.function.BiConsumer

import static fi.linuxbox.upcloud.core.callback.CallbackChecks.NETWORK_ERROR
import static fi.linuxbox.upcloud.core.callback.CallbackChecks.internalize
import static fi.linuxbox.upcloud.core.callback.CallbackChecks.internalizeCallback
import static fi.linuxbox.upcloud.core.callback.CallbackChecks.HTTP_STATUS_CATEGORIES
import static fi.linuxbox.upcloud.core.callback.CallbackChecks.NETWORK_ERROR
import static java.util.Collections.unmodifiableMap

/**
 * A helper class for unmodifiable set of callbacks, and a way to call one
 * of them based on HTTP response status code.
 */
@CompileStatic
@Slf4j
class RequestCallback implements BiConsumer<Resource, Throwable> {
    /**
     * An unmodifiable copy of the callbacks.
     * <p>
     *     This is taken at the time of request initialization, and any
     *     modifications to the session or request callbacks during the HTTP
     *     exchange have no affect on this copy.
     * </p>
     * <p>
     *     The session and request callbacks are merged into this map.
     *     Request callbacks override session callbacks.
     * </p>
     */
    private final Map<String, Closure<Void>> callbacks

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

        this.callbacks = unmodifiableMap(
                sessionCallbacks.asUnmodifiableMap() + internalize(requestCallbacks))
        this.defaultRequestCallback = internalizeCallback(null, defaultRequestCallback)

        if (defaultRequestCallback.maximumNumberOfParameters != 2 && callbacks[NETWORK_ERROR] == null) {
            throw new IllegalArgumentException('Network error handler missing')
        }
    }

    /**
     * Chooses the callback and calls it.
     *
     * @param resource Resource to pass to the callback (or null)
     * @param error Error to pass to the callback (or null)
     */
    @Override
    void accept(final Resource resource, final Throwable error) {
        final Closure<Void> callback = chooseCallback(resource?.META?.status, error)
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
     * @param err Exception or <code>null</code>
     */
    private void callApp(final Closure<Void> callback, final Resource resource, final Throwable err) {
        try {
            if (callback.maximumNumberOfParameters == 2)
                callback(resource, err)
            else
                callback(resource ?: err)
        } catch (final MissingMethodException e) {
            // this is error because we couldn't reach the app
            log.error("application callback has wrong signature", e)
        } catch (final InterruptedException e) {
            log.debug("application callback interrupted; shutting down")
            Thread.currentThread().interrupt()
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
    private Closure<Void> chooseCallback(final Integer statusCode, final Throwable error) {
        if (error)
            return callbacks[NETWORK_ERROR] ?: defaultRequestCallback

        // Try exact match to the status code first
        Closure<Void> callback = callbacks[statusCode.toString()]
        if (callback)
            return callback

        // Try one of the broad categories
        callback = HTTP_STATUS_CATEGORIES.findResult { final IntRange range, final String category ->
            statusCode in range ? callbacks[category] : null
        }
        if (callback)
            return callback

        // Fall back to default request callback
        return defaultRequestCallback
    }
}

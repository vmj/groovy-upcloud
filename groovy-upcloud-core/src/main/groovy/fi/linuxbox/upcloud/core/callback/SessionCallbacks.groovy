/*
 * Groovy UpCloud library - Core Module
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
package fi.linuxbox.upcloud.core.callback

import groovy.transform.CompileStatic

import static fi.linuxbox.upcloud.core.callback.CallbackChecks.internalize

/**
 * A helper class for a mutable map of callbacks.
 */
@CompileStatic
class SessionCallbacks {
    /**
     * Session callbacks.
     *
     * <p>
     * Application can store common callbacks in here.  The key is the HTTP response status code as a String, e.g. "500",
     * or a category like "server_error".
     * </p>
     */
    private final Map<String, Closure<Void>> sessionCallbacks = [: ]

    /**
     * Set or clear session callbacks.
     *
     * <p>
     *     The set of session callbacks is empty by default.  Application can store common callbacks in this map.  The
     *     keys in the given map are HTTP response status codes like 500 or 404, or HTTP status categories: "info",
     *     "success", "redirect", "client_error", "server_error", "error".  If the key is not recognized as either a
     *     status code or category, an {@ IllegalAgumentException} is thrown.
     * </p>
     * <p>
     *     Note that the old callbacks are not removed, unless their corresponding key is set to <code>null</code> in
     *     {@param cbs}.
     * </p>
     *
     * @param cbs Session callbacks to set or clear.  Note that any previously set callbacks are not cleared unless
     *            they are explicitly set to <code>null</code> in this argument.
     */
    void callback(Map<?, Closure<Void>> cbs) {
        final Map<String, Closure<Void>> callbacks = internalize(cbs)

        callbacks.each { String status, Closure<Void> cb ->
            if (cb)
                sessionCallbacks[status] = cb
            else
                sessionCallbacks.remove(status)
        }
    }

    /**
     * Returns an unmodifiable copy of the current map of callbacks.
     * <p>
     *     Even if this instance is modified later, the copy is not affected.
     * </p>
     *
     * @return An unmodifiable map of callbacks.
     */
    Map<String, Closure<Void>> asUnmodifiableMap() {
        Collections.unmodifiableMap(sessionCallbacks)
    }
}

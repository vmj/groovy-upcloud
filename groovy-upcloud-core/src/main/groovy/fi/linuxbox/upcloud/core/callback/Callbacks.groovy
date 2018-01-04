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

/**
 * A helper base class for managing callbacks.
 */
@CompileStatic
abstract class Callbacks {
    /**
     * HTTP response status categories.
     */
    protected static final List<Tuple2<IntRange, String>> HTTP_STATUS_CATEGORIES = [
            new Tuple2((100..199), 'info'),
            new Tuple2((200..299), 'success'),
            new Tuple2((300..399), 'redirect'),
            new Tuple2((400..499), 'client_error'),
            new Tuple2((500..599), 'server_error'),
            new Tuple2((400..599), 'error')
    ]
    /**
     * HTTP response status category names.
     */
    private static final List<String> HTTP_STATUS_CATEGORY_NAMES = HTTP_STATUS_CATEGORIES.second
    /**
     * HTTP response status code range.
     */
    private static final IntRange HTTP_STATUS_CODE_RANGE = (HTTP_STATUS_CATEGORIES[0].first.from..HTTP_STATUS_CATEGORIES[-1].first.to)

    /**
     * Cleans up and checks the map for internal use.
     * <p>
     *     Converts the keys to internal representation.
     * </p>
     *
     * @param cbs Application provided callbacks.  Note that map values may also be null.
     * @return Internalized and sanitized map of callbacks.
     * @throws IllegalArgumentException if the cleanup fails.
     */
    protected Map<String, Closure<Void>> internalize(final Map<?, Closure<Void>> cbs) {
        if (!cbs) return [:]
        cbs.inject(new LinkedHashMap<String, Closure<Void>>()) { Map callbacks, Map.Entry<?, Closure<Void>> cb ->
            final String status = internalizeStatus(cb.key)
            callbacks[status] = cb.value
            callbacks
        }
    }

    /**
     * Try to interpret the status as either a status code or a status category.
     *
     * @param status Object to interpret
     * @return Internal representation of the callback name (key to the map of callbacks)
     * @throws IllegalArgumentException if the interpretation fails.
     */
    private String internalizeStatus(final Object status) {
        if (status == null)
            throw new IllegalArgumentException("HTTP status must be non-null")

        // specifically thinking of GStrings with this .toString(), but why not for something else, too.
        final String statusString = status.toString()

        final BigInteger statusCode = internalizeStatusCode(statusString)
        if (statusCode) {
            if (statusCode in HTTP_STATUS_CODE_RANGE)
                return statusCode.toString()
            throw new IllegalArgumentException(
                    "HTTP status code must be in range (${HTTP_STATUS_CODE_RANGE.inspect()}): $status")
        }

        if (statusString in HTTP_STATUS_CATEGORY_NAMES)
            return statusString
        throw new IllegalArgumentException(
                "HTTP status category must be one of ${HTTP_STATUS_CATEGORY_NAMES}: $status")
    }

    /**
     * Try to interpret the status as a status code.
     * <p>
     *     This method does not do any range checks.
     * </p>
     * @param status Object to interpret.
     * @return The integral number, or <code>null</code> it the object doesn't look like an integral number.
     */
    private BigInteger internalizeStatusCode(final String status) {
        try {
            // ".valueOf()" and "as BigInteger" both parse the beginning of the string and accept anything at the end.
            // new BigInteger(string) however rejects any junk at the end.
            new BigInteger(status)
        } catch (final NumberFormatException ignored) {
            return null
        }
    }
}

/*
 * Groovy UpCloud library - Core Module
 * Copyright (C) 2017  <mikko@varri.fi>
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
package fi.linuxbox.upcloud.core

import groovy.transform.CompileStatic

import static java.util.Collections.emptyMap
import static java.util.Collections.unmodifiableMap

/**
 *
 * @param <T>
 */
@CompileStatic
abstract class AbstractSession<T> {
    private final Map<?, Closure<Void>> EMPTY_CBS = unmodifiableMap(emptyMap())

    /**
     * Perform a HTTP GET method.
     *
     * @param cbs Additional request callbacks.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass.  See {@link #request(Map,String,String,Resource,Closure)}.
     */
    T GET(Map<?, Closure<Void>> cbs, String path, Closure<Void> cb) {
        request(cbs, "GET", path, null, cb)
    }

    /**
     * Perform a HTTP DELETE method.
     *
     * @param cbs Additional request callbacks.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass.  See {@link #request(Map,String,String,Resource,Closure)}.
     */
    T DELETE(Map<?, Closure<Void>> cbs, String path, Closure<Void> cb) {
        request(cbs, "DELETE", path, null, cb)
    }

    /**
     * Perform a HTTP PUT method.
     *
     * @param cbs Additional request callbacks.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass.  See {@link #request(Map,String,String,Resource,Closure)}.
     */
    T PUT(Map<?, Closure<Void>> cbs, String path, Resource resource, Closure<Void> cb) {
        request(cbs, "PUT", path, resource, cb)
    }

    /**
     * Perform a HTTP POST method.
     *
     * @param cbs Additional request callbacks.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass.  See {@link #request(Map,String,String,Resource,Closure)}.
     */
    T POST(Map<?, Closure<Void>> cbs, String path, Resource resource, Closure<Void> cb) {
        request(cbs, "POST", path, resource, cb)
    }

    /**
     * Perform a HTTP request.
     *
     * @param cbs Additional request callbacks.
     * @param method HTTP method.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or null.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass.
     */
    abstract protected T request(final Map<?, Closure<Void>> cbs,
                                 final String method,
                                 final String path,
                                 final Resource resource,
                                 final Closure<Void> cb)

    /**
     * Calls {@link #GET(Map,String,Closure)} with an empty map.
     */
    T GET(String path, Closure<Void> cb) {
        GET(EMPTY_CBS, path, cb)
    }

    /**
     * Calls {@link #GET(Map,String,Closure)} with given arguments in correct order.
     */
    T GET(String path, Map<?, Closure<Void>> cbs, Closure<Void> cb) {
        GET(cbs, path, cb)
    }

    /**
     * Calls {@link #GET(Map,String,Closure)} with given arguments in correct order.
     */
    T GET(String path, Closure<Void> cb, Map<?, Closure<Void>> cbs) {
        GET(cbs, path, cb)
    }

    T DELETE(String path, Closure<Void> cb) {
        DELETE(EMPTY_CBS, path, cb)
    }

    T DELETE(String path, Map<?, Closure<Void>> cbs, Closure<Void> cb) {
        DELETE(cbs, path, cb)
    }

    T DELETE(String path, Closure<Void> cb, Map<?, Closure<Void>> cbs) {
        DELETE(cbs, path, cb)
    }

    T PUT(String path, Resource resource, Closure<Void> cb) {
        PUT(EMPTY_CBS, path, resource, cb)
    }

    T PUT(String path, Map<?, Closure<Void>> cbs, Resource resource, Closure<Void> cb) {
        PUT(cbs, path, resource, cb)
    }

    T PUT(String path, Resource resource, Map<?, Closure<Void>> cbs, Closure<Void> cb) {
        PUT(cbs, path, resource, cb)
    }

    T PUT(String path, Resource resource, Closure<Void> cb, Map<?, Closure<Void>> cbs) {
        PUT(cbs, path, resource, cb)
    }

    T POST(String path, Resource resource, Closure<Void> cb) {
        POST(EMPTY_CBS, path, resource, cb)
    }

    T POST(String path, Map<?, Closure<Void>> cbs, Resource resource, Closure<Void> cb) {
        POST(cbs, path, resource, cb)
    }

    T POST(String path, Resource resource, Map<?, Closure<Void>> cbs, Closure<Void> cb) {
        POST(cbs, path, resource, cb)
    }

    T POST(String path, Resource resource, Closure<Void> cb, Map<?, Closure<Void>> cbs) {
        POST(cbs, path, resource, cb)
    }
}

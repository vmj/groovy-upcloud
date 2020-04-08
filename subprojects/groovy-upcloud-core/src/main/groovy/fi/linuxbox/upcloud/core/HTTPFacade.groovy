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
package fi.linuxbox.upcloud.core

import groovy.transform.CompileStatic

import static java.util.Collections.emptyMap

/**
 *
 * @param <T> Type parameter T is the return type of the
 * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}
 */
@CompileStatic
abstract class HTTPFacade<T> {
    private static final Map<?, Closure<Void>> EMPTY_CBS = emptyMap().asUnmodifiable()


    /**
     * Performs a HTTP GET method.
     * <p>
     *     This method calls the
     *     {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}
     *     with {@code GET} method and {@code null} resource.
     * </p>
     * <p>
     *     This overload is used when the application code calls this directly.  For example:
     * </p>
     * <pre><code lang="groovy">
     *     session.GET '/lb', error: { log.fatal("${it.META}") }, { res, err ->
     *         res.loadBalancers.each { lb ->
     *             log.info("Load balancer: ${lb.name}")
     *         }
     *     }
     * </code></pre>
     * <p>
     *     In the example above, there is a REST API endpoint that this library does not know about: {@code /lb}.
     *     Application can bypass the outdated library API and make REST API calls directly.
     * </p>
     *
     * @param cbs Additional request callbacks.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T GET(Map<?, Closure<Void>> cbs, String path, Closure<Void> cb = null) {
        request(cbs, "GET", path, null, cb)
    }

    /**
     * Performs a HTTP DELETE method.
     * <p>
     *     This method calls the
     *     {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}
     *     with {@code DELETE} method and {@code null} resource.
     * </p>
     * <p>
     *     This overload is used when the application code calls this directly.  For example:
     * </p>
     * <pre><code lang="groovy">
     *     Resource lb = // obtain a LoadBalancer...
     *     session.DELETE "/lb/${lb.uuid}", error: { log.fatal("${it.META}") }, { res, err ->
     *         if (err)
     *           log.warn("Network error: ${err.message}")
     *         else
     *           log.info("Load balancer deleted")
     *     }
     * </code></pre>
     * <p>
     *     In the example above, there is a REST API endpoint that this library does not know about: {@code /lb}.
     *     Application can bypass the outdated library API and make REST API calls directly.
     * </p>
     *
     * @param cbs Additional request callbacks.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T DELETE(Map<?, Closure<Void>> cbs, String path, Closure<Void> cb = null) {
        request(cbs, "DELETE", path, null, cb)
    }

    /**
     * Performs a HTTP PUT method.
     * <p>
     *     This method calls the
     *     {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}
     *     with {@code PUT} method.
     * </p>
     * <p>
     *     This overload is used when the application code calls this directly.  For example:
     * </p>
     * <pre><code lang="groovy">
     *     import static fi.linuxbox.upcloud.builder.ResourceBuilder.configure
     *
     *     Resource lb = // obtain a LoadBalancer...
     *
     *     lb = configure lb {
     *         method = 'round-robin'
     *     }
     *
     *     session.PUT "/lb/${lb.uuid}", lb, error: { log.fatal("${it.META}") }, { res, err ->
     *         if (err)
     *           log.warn("Network error: ${err.message}")
     *         else
     *           log.info("Load balancer updated")
     *     }
     * </code></pre>
     * <p>
     *     In the example above, there is a REST API endpoint that this library does not know about: {@code /lb}.
     *     Application can bypass the outdated library API and PUT the custom resource to the REST API directly.
     * </p>
     *
     * @param cbs Additional request callbacks.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T PUT(Map<?, Closure<Void>> cbs, String path, Resource resource, Closure<Void> cb = null) {
        request(cbs, "PUT", path, resource, cb)
    }

    /**
     * Performs a HTTP POST method.
     * <p>
     *     This method calls the
     *     {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}
     *     with {@code POST} method.
     * </p>
     * <p>
     *     This overload is used when the application code calls this directly.  For example:
     * </p>
     * <pre><code lang="groovy">
     *     import static fi.linuxbox.upcloud.builder.ResourceBuilder.build
     *
     *     def lb = build 'LoadBalancer' {
     *         name = 'web'
     *         method = 'random'
     *         backends = [
     *             "192.168.1.120:80",
     *             "192.168.1.121:80"
     *         ]
     *     }
     *
     *     session.POST '/lb', lb, error: { log.warn("${it.META}") }, { res, err ->
     *         if (err)
     *           log.warn("Network error: ${err.message}")
     *         else
     *           log.info("Load balancer created")
     *     }
     * </code></pre>
     * <p>
     *     In the example above, there is a REST API endpoint that this library does not know about: {@code /lb}.
     *     Application can bypass the outdated library API and POST the custom resource to the REST API directly.
     * </p>
     *
     * @param cbs Additional request callbacks.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T POST(Map<?, Closure<Void>> cbs, String path, Resource resource, Closure<Void> cb = null) {
        request(cbs, "POST", path, resource, cb)
    }

    /**
     * Performs a HTTP PATCH method.
     * <p>
     *     This method calls the
     *     {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}
     *     with {@code PATCH} method.
     * </p>
     * <p>
     *     This overload is used when the application code calls this directly.  For example:
     * </p>
     * <pre><code lang="groovy">
     *     import static fi.linuxbox.upcloud.builder.ResourceBuilder.configure
     *
     *     Resource lb = // obtain a LoadBalancer...
     *
     *     lb = configure lb {
     *         method = 'round-robin'
     *     }
     *
     *     session.PATCH "/lb/${lb.uuid}", lb, error: { log.fatal("${it.META}") }, { res, err ->
     *         if (err)
     *           log.warn("Network error: ${err.message}")
     *         else
     *           log.info("Load balancer updated")
     *     }
     * </code></pre>
     * <p>
     *     In the example above, there is a REST API endpoint that this library does not know about: {@code /lb}.
     *     Application can bypass the outdated library API and PATCH the custom resource to the REST API directly.
     * </p>
     *
     * @param cbs Additional request callbacks.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T PATCH(Map<?, Closure<Void>> cbs, String path, Resource resource, Closure<Void> cb = null) {
        request(cbs, "PATCH", path, resource, cb)
    }

    /**
     * Perform a HTTP request.
     *
     * @param cbs Additional request callbacks.
     * @param method HTTP method.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass.
     */
    abstract T request(final Map<?, Closure<Void>> cbs,
                       final String method,
                       final String path,
                       final Resource resource,
                       final Closure<Void> cb)

    /**
     * Calls {@code GET} with an empty map as additional request callbacks.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does not pass any
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T GET(String path, Closure<Void> cb = null) {
        GET(EMPTY_CBS, path, cb)
    }

    /**
     * Calls {@code GET} with given arguments in correct order.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does provide some
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cbs Additional request callbacks.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T GET(String path, Map<?, Closure<Void>> cbs, Closure<Void> cb = null) {
        GET(cbs, path, cb)
    }

    /**
     * Calls {@code DELETE} with an empty map as additional request callbacks.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does not pass any
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T DELETE(String path, Closure<Void> cb = null) {
        DELETE(EMPTY_CBS, path, cb)
    }

    /**
     * Calls {@code DELETE} with given arguments in correct order.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does provide some
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param cbs Additional request callbacks.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T DELETE(String path, Map<?, Closure<Void>> cbs, Closure<Void> cb = null) {
        DELETE(cbs, path, cb)
    }

    /**
     * Calls {@code PUT} with an empty map as additional request callbacks.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does not pass any
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T PUT(String path, Resource resource, Closure<Void> cb = null) {
        PUT(EMPTY_CBS, path, resource, cb)
    }

    /**
     * Calls {@code PUT} with given arguments in correct order.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does provide some
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cbs Additional request callbacks.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T PUT(String path, Resource resource, Map<?, Closure<Void>> cbs, Closure<Void> cb = null) {
        PUT(cbs, path, resource, cb)
    }

    /**
     * Calls {@code POST} with an empty map as additional request callbacks.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does not pass any
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T POST(String path, Resource resource, Closure<Void> cb = null) {
        POST(EMPTY_CBS, path, resource, cb)
    }

    /**
     * Calls {@code POST} with given arguments in correct order.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does provide some
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cbs Additional request callbacks.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T POST(String path, Resource resource, Map<?, Closure<Void>> cbs, Closure<Void> cb = null) {
        POST(cbs, path, resource, cb)
    }

    /**
     * Calls {@code PATCH} with an empty map as additional request callbacks.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does not pass any
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T PATCH(String path, Resource resource, Closure<Void> cb = null) {
        PATCH(EMPTY_CBS, path, resource, cb)
    }

    /**
     * Calls {@code PATCH} with given arguments in correct order.
     * <p>
     *     This overload is typically used by the higher level API traits when the application does provide some
     *     additional request callbacks.
     * </p>
     *
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or {@code null}.
     * @param cbs Additional request callbacks.
     * @param cb Default request callback.
     * @return Whatever is returned by a concrete subclass from the
     * {@link #request(Map&lt;?, Closure&lt;Void&gt;&gt;, java.lang.String, java.lang.String, fi.linuxbox.upcloud.core.Resource, Closure&lt;Void&gt;) request method}.
     */
    T PATCH(String path, Resource resource, Map<?, Closure<Void>> cbs, Closure<Void> cb = null) {
        PATCH(cbs, path, resource, cb)
    }
}

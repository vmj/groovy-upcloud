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
package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.core.callback.RequestCallback
import fi.linuxbox.upcloud.core.callback.SessionCallbacks
import fi.linuxbox.upcloud.core.http.simple.SimpleHeaders
import fi.linuxbox.upcloud.http.spi.*
import fi.linuxbox.upcloud.json.spi.JSON
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.function.BiConsumer

import static fi.linuxbox.upcloud.core.UpCloudContract.*

/**
 * The API for all the things managed in UpCloud.
 *
 * <p>
 *     Together with the {@link Resource} class, this is the most core part of the Groovy UpCloud library.  Although seldom
 *     used directly, all the UpCloud API calls go through this class.
 * </p>
 * <p>
 *     Basically, this class provides a method for each of the HTTP "verbs" that the server side knows about:
 * </p>
 * <ul>
 *     <li>{@code GET(...)} for fetching information about something.</li>
 *     <li>{@code DELETE(...)} for deleting something.</li>
 *     <li>{@code POST(...)} for creating new things.</li>
 *     <li>{@code PUT(...)} for updating those things.</li>
 * </ul>
 * <p>
 *     The arguments above are shown as {@code ...} because the interfaces are somewhat flexible.  This is Groovy
 *     after all.  We'll unravel them bit by bit below.
 * </p>
 *
 * <h4>Default request callback</h4>
 * <p>
 *     There's one argument that is most prominent and must always be provided by the application: the default request
 *     callback.  When sending a request to the server, it is sent asynchronously, and a callback is invoked when the
 *     response is received from the server.
 * </p>
 * <p>
 *     As said, the HTTP methods are typically called indirectly.  For example, to create a server, one could do as
 *     follows:
 * </p>
 * <pre><code class="groovy">
 *     def server = createMyServerResource()
 *     upcloud.create server, { response ->
 *         // Do something with the response object.
 *     }
 * </code></pre>
 * <p>
 *     Above {@code upcloud.create(...)} will call {@code session.POST(..., Closure&lt;Void&gt; cb)}.
 * </p>
 * <p>
 *     The default request callback is a {@link groovy.lang.Closure Closure} that doesn't return anything.  All of
 *     those HTTP methods mentioned above take the default request callback as their last argument.
 * </p>
 * <p>
 *     The response object above is an instance of {@link Resource} class.  You can read about it later, but for example,
 *     to inspect the actual HTTP response status code, one could use the
 *     {@link fi.linuxbox.upcloud.http.spi.META#getStatus() response.META.status} property.
 *     But that is not typically necessary because there's a better way.
 * </p>
 *
 * <h4>Additional request callbacks</h4>
 * <p>
 *     As the name "default request callback" might imply, there are other callbacks that can be attached to the HTTP
 *     exchange: additional request callbacks.  This is a {@link java.util.Map Map} that maps HTTP status codes to
 *     callbacks.
 * </p>
 * <p>
 *     For example, to handle the case where the username or password is mistyped, or they get revoked, one might write
 *     the server creation code like this:
 * </p>
 * <pre><code class="groovy">
 *     def server = createMyServerResource()
 *     upcloud.create(server,
 *         401: { response ->
 *             // The username/password pair were bogus.  Deal with it here.
 *         },
 *         { response ->
 *             // Handle all the other cases here.
 *         })
 * </code></pre>
 * <p>
 *     Above code snippet is taking advantage of the way Groovy deals with keyword arguments: it collects them to one
 *     {@link java.util.Map Map} and puts that {@code map} at the beginning of the argument list before calling the method.
 *     In other words, the default request callback doesn't actually have to be the last argument, it can be followed
 *     by keyword arguments.
 * </p>
 * <p>
 *     Additional request callbacks are similar to default request callback: they are
 *     {@link groovy.lang.Closure Closures} with {@code void} return type.  They get called with one argument, the
 *     {@link Resource} instance representing the HTTP response.  All of those HTTP methods mentioned above ({@code GET},
 *     {@code POST}, and so on) take the {@link java.util.Map Map} of additional request callbacks as their first argument.
 * </p>
 * <p>
 *     Dealing with common HTTP error responses like above can get boring after awhile.  Worry not, there's another way.
 * </p>
 *
 * <h4>Sessions callbacks</h4>
 * <p>
 *     One can attach session callbacks to the {@code Session} instance.  These are just like additional request
 *     callbacks, except that these are considered for all future HTTP requests made through this instance of the
 *     {@code Session}.
 * </p>
 * <p>
 *     For example, the above 401 response can be handled once and for all like this:
 * </p>
 * <pre><code class="groovy">
 *     session.callback 401: { log.fatal("configuration error: the username/password is no good") }
 * </code></pre>
 * <p>
 *     From that point on, all the requests made through this {@code Session} instance would have that callback in
 *     their set of callbacks.
 * </p>
 *
 * <h4>HTTP response status categories</h4>
 * <p>
 *     As you may know, all the HTTP response status codes are three digit numbers and can be divided into five
 *     categories.  The categories by range of status codes are listed below, with category names as known by this
 *     library:
 * </p>
 * <ul>
 *     <li>100-199: {@code info}</li>
 *     <li>200-299: {@code success}</li>
 *     <li>300-399: {@code redirect}</li>
 *     <li>400-499: {@code client_error}</li>
 *     <li>500-599: {@code server_error}</li>
 * </ul>
 * <p>
 *     This library also knows about {@code error} category, which is just more generic and covers both
 *     {@code client_error} and {@code server_error} categories, i.e. status codes 400-599.
 * </p>
 * <p>
 *     These categories are useful when one wants to deal with a range of responses in the same way.  For example, as
 *     far as I know, at the time of this writing, the UpCloud API never responds with {@code info} or
 *     {@code redirect} type of status codes.  So, I might recommend adding this to the beginning of scripts:
 * </p>
 * <pre><code class="groovy">
 *     session.callback info: { log.fatal "oh my, assumptions are all broken" },
 *                  redirect: { log.fatal "dear dear, but I don't want to go elsewhere" }
 * </code></pre>
 * <p>
 *     Of course, in reality, one would log some details from the passed in {@link Resource} instance.
 * </p>
 *
 * <h4>Callback resolution order</h4>
 * <p>
 *     Since each HTTP exchange may have multiple sources of callbacks, this library chooses the callback in the
 *     following way:
 * </p>
 * <ol>
 *     <li>An exact match to the status code is tried first</li>
 *     <li>The status code category is tried, if exact match is not found</li>
 *     <li>If the response is in the more generic {@code error} category, it is tried last</li>
 *     <li>If nothing else is found, the default request callback is invoked</li>
 * </ol>
 * <p>
 *     For example, if the server responds with a {@code 200 OK}, the resolution path looks like this:
 * </p>
 * <ol>
 *     <li>If there's an additional request callback for {@code 200}, that is invoked</li>
 *     <li>If there's a session callback for {@code 200} attached to the {@code Session} instance, that is invoked</li>
 *     <li>If there's an additional request callback for {@code success}, that is invoked</li>
 *     <li>If there's a session callback for {@code success} attached to the {@code Session} instance, that is invoked</li>
 *     <li>Failing all above, the default request callback is invoked</li>
 * </ol>
 * <p>
 *     On the other hand, for an HTTP response {@code 400 Bad Request}, the resolution path is a bit longer due to the more
 *     generic {@code error} category:
 * </p>
 * <ol>
 *     <li>request callback for {@code 400}</li>
 *     <li>session callback for {@code 400}</li>
 *     <li>request callback for {@code client_error}</li>
 *     <li>session callback for {@code client_error}</li>
 *     <li>request callback for {@code error}</li>
 *     <li>session callback for {@code error}</li>
 *     <li>default request callback</li>
 * </ol>
 * <p>
 *     In all examples above, remember that only one callback is invoked for one HTTP exchange: the first one that
 *     matches.
 * </p>
 *
 * <h4>Network error handling</h4>
 * <p>
 *     The additional request callbacks and the session callbacks attached to the {@code Session} instance, are all by
 *     definition tied to the HTTP response status code or the status category.  However, networks are unreliable and
 *     the communication with the server may not always work.  The server might be unreachable, or there might be an
 *     I/O error talking to the server, or the request might be cancelled.
 * </p>
 * <p>
 *     The default request callback can take a second optional argument: an instance of {@link Throwable} class.
 *     This is {@code null} whenever a response from the server is available, even if the response represents an error,
 *     and non-{@code null} when the communication fails:
 * </p>
 * <pre><code class="groovy">
 *     def server = createMyServerResource()
 *     upcloud.create server, { response, error ->
 *          if (error) {
 *             // Handle the network error here.
 *             assert error instanceof Throwable
 *             assert response == null
 *         } else {
 *             // Handle all the other cases here.
 *             assert response instanceof Resource
 *             assert error == null
 *         })
 * </code></pre>
 * <p>
 *     Alternatively, one can use {@code network_error} category for a callback.  This callback is different from
 *     additional request callbacks in that it takes an instance of {@link Throwable} instead of a {@link Resource}
 *     instance as their only parameter. Otherwise it is the same: {@link groovy.lang.Closure Closures} with
 *     {@code void} return type.
 * </p>
 * <pre><code class="groovy">
 *     def server = createMyServerResource()
 *     upcloud.create(server,
 *         network_error: { error ->
 *             // Handle the network error here.
 *             assert error instanceof Throwable
 *         },
 *         { response ->
 *             // Handle all the other cases here.
 *         })
 * </code></pre>
 * <p>
 *     Naturally, the {@code network_error} callback can be attached to the {@code Session} instance, too.
 *     Also, similar to additional request callbacks, it takes precedence over default request callback (in case the
 *     default request callback would take two arguments).
 * </p>
 */
@CompileStatic
@Slf4j
abstract class Session<T> extends HTTPFacade<T> {
    /**
     * HTTP request headers.
     *
     * <p>
     *     The Authorization header is filled in the constructor.  That also means that this is not shared between
     *     instances.
     * </p>
     * <p>
     *     The Host header ensures that it is set correctly.  At least the Apache HttpAsyncClient would add it by
     *     default, but it would add the ":443" port there, too.  The UpCloud server doesn't like that.
     * </p>
     */
    private final SimpleHeaders requestHeaders

    /**
     * Session callbacks.
     *
     * <p>
     * Application can store common callbacks in here.
     * </p>
     */
    private final SessionCallbacks sessionCallbacks = new SessionCallbacks()
    /**
     * HTTP implementation.
     */
    private final HTTP http
    /**
     * JSON implementation.
     */
    private final JSON json

    /**
     * Injectable constructor.
     *
     * @param http HTTP implementation.
     * @param json JSON implementation.
     * @param username UpCloud API username.  This is not the one you use to login to the control panel.
     * @param password UpCloud API password.  This is not the one you use to login to the control panel.
     */
    Session(HTTP http, JSON json, String username, String password) {
        this.http = http
        this.json = json
        this.requestHeaders = requestHeaders(username, password, http.userAgent)
    }

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
     * <p>
     *     Note also that the given map is copied while checking the keys,
     *     so any later modifications to the map itself are not picked up by
     *     this class.
     * </p>
     *
     * @param cbs Session callbacks to set or clear.  Note that any previously set callbacks are not cleared unless
     *            they are explicitly set to <code>null</code> in this argument.
     */
    void callback(Map<?, Closure<Void>> cbs) {
        sessionCallbacks.callback(cbs)
    }

    /**
     * Perform a HTTP request.
     *
     * @param cbs Additional request callbacks.
     * @param method HTTP method.
     * @param path Resource path relative to the API context path, i.e. without leading slash.
     * @param resource Resource to send or null.
     * @param cb Default request callback.
     * @return Whatever is returned by the HTTP implementation for starting an asynchronous request.
     */
    @Override
    T request(final Map<?, Closure<Void>> cbs,
                 final String method,
                 final String path,
                 final Resource resource,
                 final Closure<Void> cb) {
        final RequestCallback requestCallback =
                new RequestCallback(sessionCallbacks, cbs, cb)

        final T promise = unresolvedPromise()
        final BiConsumer<Resource, Throwable> resolver = promiseResolver(promise)

        http.execute new Request(
                host: HOST,
                method: method,
                resource: API_VERSION + path,
                headers: requestHeaders),
                resource ? json.encode(resource as Map) : null,
                { final META meta, final InputStream body, final Throwable err ->
                    // Contract is that either meta is non-null, or err
                    // is non-null.  Never both nulls and never both non-nulls.
                    final Resource resp = err ? null : decode(meta, body)
                    body?.close()
                    requestCallback.accept(resp, err)
                    resolver?.accept(resp, err)
                }

        promise
    }

    abstract protected T unresolvedPromise()
    abstract protected BiConsumer<Resource, Throwable> promiseResolver(final T unresolvedPromise)

    /**
     * Allow session to act as HTTP provider.
     *
     * @return this
     */
    HTTPFacade<T> getHTTP() {
        this
    }

    /**
     * Decodes the entity {@param body}, if it is valid UTF-8 JSON.
     * <p>
     *     Even in case it is not, an empty <code>Resource</code> is
     *     returned containing the headers.
     * </p>
     *
     * @param meta HTTP headers from which to check whether the entity body is supposed to be UTF-8 JSON.
     * @param body HTTP entity body to parse.
     * @return Resource, never null.
     */
    private Resource decode(final META meta, final InputStream body) {
        final Map<String, Object> repr = decode(meta.headers, body)
        return new Resource(repr: repr, HTTP: this, META: meta)
    }

    /**
     * Decodes the entity {@param body}, but only if it is claimed to be UTF-8 JSON by the {@param headers}.
     * <p>
     *     The server may respond with HTML or XML or something else, especially if we screw up with the request
     *     headers.
     * </p>
     * <p>
     *     Also, since the parsing happens in a callback, this tries to be extra careful not to throw any exceptions
     *     to the HTTP implementation.
     * </p>
     * <p>
     *     If the content isn't valid UTF-8 JSON, then an empty map is returned.
     * </p>
     * @param meta HTTP headers from which to check whether the entity body is supposed to be UTF-8 JSON.
     * @param body HTTP entity body to parse.
     * @return Resource representation, or empty Map.
     */
    private Map<String, Object> decode(final Headers headers, final InputStream body) {
        if (headers && body) {
            def isUTF8Json = headers.getAt('Content-Type').find { final HeaderElement headerElement ->
                headerElement.name == 'application/json' && headerElement.parameters.find { final Parameter param ->
                    param.name == 'charset' && param.value == 'UTF-8'
                }
            }

            if (isUTF8Json) {
                try {
                    return json.decode(body)
                } catch (final Exception e) {
                    log.warn("Failed to parse UTF-8 JSON", e)
                }
            }
        }
        return [:]
    }
}

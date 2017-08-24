package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.core.callback.RequestCallback
import fi.linuxbox.upcloud.core.callback.SessionCallbacks
import fi.linuxbox.upcloud.http.spi.ERROR
import fi.linuxbox.upcloud.http.spi.Request
import fi.linuxbox.upcloud.http.spi.HTTP
import fi.linuxbox.upcloud.http.spi.Headers
import fi.linuxbox.upcloud.http.spi.META
import fi.linuxbox.upcloud.core.http.simple.SimpleHeaders
import fi.linuxbox.upcloud.json.spi.JSON
import groovy.transform.*

import javax.inject.*
import org.slf4j.*

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
 *     <li><code>GET(...)</code> for fetching information about something.</li>
 *     <li><code>DELETE(...)</code> for deleting something.</li>
 *     <li><code>POST(...)</code> for creating new things.</li>
 *     <li><code>PUT(...)</code> for updating those things.</li>
 * </ul>
 * <p>
 *     The arguments above are shown as <code>...</code> because the interfaces are somewhat flexible.  This is Groovy
 *     after all.  We'll unravel them bit by bit below.
 * </p>
 * <h1>Default request callback</h1>
 * <p>
 *     There's one argument that is most prominent and must always be provided by the application: the default request
 *     callback.  When sending a request to the server, it is sent asynchronously, and a callback is invoked when the
 *     response is received from the server.
 * </p>
 * <p>
 *     As said, the HTTP methods are typically called indirectly.  For example, to create a server, one could do as
 *     follows:
 * </p>
 * <pre>
 *     def server = createMyServerResource()
 *     server.create({ response ->
 *         // Do something with the response object.
 *     })
 * </pre>
 * <p>
 *     Above <code>server.create(...)</code> will call <code>session.POST(..., Closure&lt;Void&gt; cb)</code>.
 * </p>
 * <p>
 *     The default request callback is a {@link groovy.lang.Closure Closure} that doesn't return anything.  All of
 *     those HTTP methods mentioned above take the default request callback as their last argument.
 * </p>
 * <p>
 *     The response object above is an instance of {@link Resource} class.  You can read about it later, but for example,
 *     to inspect the actual HTTP response status code, one could use the <code>response.META.status</code> property.
 *     But that is not typically necessary because there's a better way.
 * </p>
 * <h1>Additional request callbacks</h1>
 * <p>
 *     As the name "default request callback" might imply, there are other callbacks that can be attached to the HTTP
 *     exchange: additional request callbacks.  This is a {@link java.util.Map Map} that maps HTTP status codes to
 *     callbacks.
 * </p>
 * <p>
 *     For example, to handle the case where the user name or password is mistyped, or they get revoked, one might write
 *     the server creation code like this:
 * </p>
 * <pre>
 *     def server = createMyServerResource()
 *     server.create(
 *         401: { response ->
 *             // The username/password pair were bogus.  Deal with it here.
 *         },
 *         { response ->
 *             // Handle all the other cases here.
 *         })
 * </pre>
 * <p>
 *     Above code snippet is taking advantage of the way Groovy deals with keyword arguments: it collects them to one
 *     <code>Map</code> and puts that <code>map</code> at the beginning of the argument list before calling the method.
 *     In other words, the default request callback doesn't actually have to be the last argument, it can be followed
 *     by keyword arguments.
 * </p>
 * <p>
 *     Additional request callbacks are similar to default request callback: they are
 *     {@link groovy.lang.Closure Closures} with <code>void</code> return type.  They get called with one argument, the
 *     {@link Resource} instance representing the HTTP response.  All of those HTTP methods mentioned above (<code>GET</code>,
 *     <code>POST</code>, and so on) take the <code>Map</code> of additional request callbacks as their first argument.
 * </p>
 * <p>
 *     Dealing with common HTTP error responses like above can get boring after awhile.  Worry not, there's another way.
 * </p>
 * <h1>Sessions callbacks</h1>
 * <p>
 *     One can attach session callbacks to the <code>Session</code> instance.  These are just like additional request
 *     callbacks, except that these are considered for all future HTTP requests made through this instance of the
 *     <code>Session</code>.
 * </p>
 * <p>
 *     For example, the above 401 response can be handled once and for all like this:
 * </p>
 * <pre>
 *     session.callback 401: { log.fatal("configuration error: the username/password is no good") }
 * </pre>
 * <p>
 *     From that point on, all the requests made through this <code>Session</code> instance would have that callback in
 *     their set of callbacks.
 * </p>
 * <h1>HTTP response status categories</h1>
 * <p>
 *     As you may know, all the HTTP response status codes are three digit numbers and can be divided into five
 *     categories.  The categories by range of status codes are listed below, with category names as known by this
 *     library:
 * </p>
 * <ul>
 *     <li>100-199: <code>info</code></li>
 *     <li>200-299: <code>success</code></li>
 *     <li>300-399: <code>redirect</code></li>
 *     <li>400-499: <code>client_error</code></li>
 *     <li>500-599: <code>server_error</code></li>
 * </ul>
 * <p>
 *     This library also knows about <code>error</code> category, which is just more generic and covers both
 *     <code>client_error</code> and <code>server_error</code> categories, i.e. status codes 400-599.
 * </p>
 * <p>
 *     These categories are useful when one wants to deal with a range of responses in the same way.  For example, as
 *     far as I know, at the time of this writing, the UpCloud API never responds with <code>info</code> or
 *     <code>redirect</code> type of status codes.  So, I might recommend adding this to the beginning of scripts:
 * </p>
 * <pre>
 *     session.callback info: { log.fatal("oh my, assumptions are all broken" },
 *                  redirect: { log.fatal("dear dear, but I don't want to go elsewhere") }
 * </pre>
 * <p>
 *     Of course, in reality, one would log some details from the passed in {@link Resource} instance.
 * </p>
 * <h1>Callback resolution order</h1>
 * <p>
 *     Since each HTTP exchange may have multiple sources of callbacks, this library chooses the callback in the
 *     following way:
 * </p>
 * <ol>
 *     <li>An exact match to the status code is tried first</li>
 *     <li>The status code category is tried, if exact match is not found</li>
 *     <li>If the response is in the more generic <code>error</code> category, it is tried last</li>
 *     <li>If nothing else is found, the default request callback is invoked</li>
 * </ol>
 * <p>
 *     For example, if the server responds with a "200 OK", the resolution path looks like this:
 * </p>
 * <ol>
 *     <li>If there's an additional request callback for "200", that is invoked</li>
 *     <li>If there's a session callback for "200" attached to the <code>Session</code> instance, that is invoked</li>
 *     <li>If there's an additional request callback for "info", that is invoked</li>
 *     <li>If there's a session callback for "info" attached to the <code>Session</code> instance, that is invoked</li>
 *     <li>Failing all above, the default request callback is invoked</li>
 * </ol>
 * <p>
 *     On the other hand, for an HTTP response "400 Bad Request", the resolution path is a bit longer due to the more
 *     generic <code>error</code> category:
 * </p>
 * <ol>
 *     <li>request callback for "400"</li>
 *     <li>session callback for "400"</li>
 *     <li>request callback for "client_error"</li>
 *     <li>session callback for "client_error"</li>
 *     <li>request callback for "error"</li>
 *     <li>session callback for "error"</li>
 *     <li>default request callback</li>
 * </ol>
 * <p>
 *     In all examples above, remember that only one callback is invoked for one HTTP exchange: the first one that
 *     matches.
 * </p>
 * <h1>Network error handling</h1>
 * <p>
 *     The additional request callbacks and the session callbacks attached to the <code>Session</code> instance, are all by
 *     definition tied to the HTTP response status code or the status category.  However, networks are unreliable and
 *     the communication with the server may not always work.  This is where the special nature, and the importance, of
 *     the default request callback comes apparent.
 * </p>
 * <p>
 *     The default request callback takes a second optional argument: an instance of {@link ERROR} class.  This is
 *     <code>null</code> whenever the response from the server is available, even if the response represents an error,
 *     and <code>non-null</code> if the server is, for example, unreachable or there's a I/O error talking to the
 *     server.  Read more about that in the {@link ERROR} class documentation.
 * </p>
 */
class Session {
    private final Logger log = LoggerFactory.getLogger(Session)

    /**
     * UpCloud API host.
     */
    private static final String HOST = 'https://api.upcloud.com'
    /**
     * UpCloud API context path.
     */
    private static final String API_VERSION = '/1.2/'

    /**
     * HTTP method descriptions.
     *
     * <p>
     * These are used to create the HTTP methods dynamically.  The values are the range of how many method arguments
     * they take, and the boolean describes whether the method takes an entity body or not.
     * </p>
     */
    private final static Map<String, Tuple2<IntRange, Boolean>> HTTP_METHODS = [
            GET   : new Tuple2((2..3), true),
            DELETE: new Tuple2((2..3), true),
            PUT   : new Tuple2((3..4), false),
            POST  : new Tuple2((3..4), false)
    ]
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
    private final Map<String, String> requestHeaders = [
            'Accept'       : 'application/json; charset=UTF-8',
            'Authorization': 'Basic ',
            'Content-Type' : 'application/json',
            'Host'         : 'api.upcloud.com',
            'User-Agent'   : 'Groovy UpCloud/0.0.4 ',
    ]
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
    @Inject
    Session(HTTP http, JSON json, @Named("username") String username, @Named("password") String password) {
        this.http = http
        this.json = json
        requestHeaders['Authorization'] += "$username:$password".bytes.encodeBase64().toString()
        requestHeaders['User-Agent'] += http.userAgent
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
    @PackageScope // for testing
    request(
            final Map<?, Closure<Void>> cbs = [ : ],
            final String method, final String path, final Resource resource,
            final Closure<Void> cb) {
        final RequestCallback requestCallback =
                new RequestCallback(sessionCallbacks, cbs, cb)

        http.execute new Request(
                host: HOST,
                method: method,
                resource: API_VERSION + path,
                headers: new SimpleHeaders(requestHeaders),
                body: resource ? json.encode(resource as Map) : null,
                cb: { final META meta,
                      final InputStream body, final ERROR err ->
                    // Contract is that either resource is non-null, or err
                    // is non-null.  Never both nulls and never both non-nulls.
                    final Resource m = err ? null : decode(meta, body)
                    requestCallback.accept(m, err)
                })
    }

    /**
     * MOP method for HTTP request methods.
     *
     * <p>
     *     This implementation adds the following methods:
     * </p>
     * <ul>
     *     <li>GET(Map cbs = [:], String path, Closure<Void> cb)</li>
     *     <li>DELETE(Map cbs = [:], String path, Closure<Void> cb)</li>
     *     <li>PUT(Map cbs = [:], String path, Resource resource, Closure<Void> cb)</li>
     *     <li>POST(Map cbs = [:], String path, Resource resource, Closure<Void> cb)</li>
     * </ul>
     * <p>
     *     To be exact, the Map argument can exists anywhere, and it is moved to the beginning of the argument list.
     *     This is to allow the Groovy style of keyword arguments:
     * </p>
     * <pre>
     *     session.GET('some-resource',
     *                 error: {
     *                     // handle error
     *                 },
     *                 {
     *                     // handle all the other cases
     *                 })
     * </pre>
     * <p>
     *     This implementation (mainly the move operation of the map) also enables the following higher level APIs:
     * </p>
     * <pre>
     *     class Server extends Resource {
     *         def create(...args) {
     *             this.SESSION.POST('server', this.wrapper(), *args)
     *         }
     *     }
     * </pre>
     * <p>
     *     Which can still be called like this:
     * </p>
     * <pre>
     *     server.create \
     *         400: {
     *             // handle bad request
     *         },
     *         {
     *             // handle all the other cases
     *         }
     * </pre>
     * <p>
     *     Or like this:
     * </p>
     * <pre>
     *     server.create(
     *         { response ->
     *         },
     *         400: {
     *         },
     *         500: {
     *         })
     * </pre>
     *
     * @param name The HTTP request method (verb).
     * @param args The rest of the arguments.
     * @return Whatever the HTTP implementation returns as a result of starting an asynchronous operation.
     */
    def methodMissing(final String name, final args) {
        def method = HTTP_METHODS[name]
        if (method && args.length in method.first) {
            def ARGV = [ *args ]

            // The first argument (Map of request callbacks) may be missing
            int i = ARGV.findIndexOf { it instanceof Map<?, Closure<Void>> }
            if (i == -1) {
                ARGV.add(0, [ : ])
            } else if (i != 0) {
                ARGV.add(0, ARGV.removeAt(i))
            }

            // Force the second argument to be the HTTP method
            ARGV.add(1, name)

            // The third argument (second or first from caller perspective) must be a string
            //if (!(ARGV[2] instanceof String))
            //    throw new MissingMethodException(name, this.class, args)

            // If this is a GET or DELETE, force the fourth argument be null resource
            if (method.second)
                ARGV.add(3, null)
            //else if (!(ARGV[3] instanceof Resource))
            //    throw new MissingMethodException(name, this.class, args)

            // The final argument is the mandatory default request callback
            //if (!(ARGV[4] instanceof Closure<Void>))
            //    throw new MissingMethodException(name, this.class, args)

            // Make the call!
            return request(*ARGV)
        }
        throw new MissingMethodException(name, this.class, args)
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
        final Map<String, Object> repr = decode(meta?.headers, body)
        return new Resource(repr: repr, SESSION: this, META: meta)
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
            def isUTF8Json = headers.getAt('Content-Type').find {
                it.name == 'application/json' && it.parameters.find { it.name == 'charset' && it.value == 'UTF-8' }
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

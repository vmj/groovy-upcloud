package fi.linuxbox.upcloud.core

import groovy.transform.*
import javax.inject.*
import org.slf4j.*

import fi.linuxbox.upcloud.core.http.*
import fi.linuxbox.upcloud.core.http.simple.*
import fi.linuxbox.upcloud.core.json.*
/**
 * The API for all the things managed in UpCloud.
 *
 * <p>
 *     Together with the {@link MODEL} class, this is the most core part of the Groovy UpCloud library.  Although seldom
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
 *     As said, the API methods are typically called indirectly.  For example, to create a server, one could do as
 *     follows:
 * </p>
 * <pre>
 *     def server = createMyServerModel()
 *     server.create({ response ->
 *         // Do something with the response object.
 *     })
 * </pre>
 * <p>
 *     Above <code>server.create(...)</code> will call <code>API.POST(..., Closure&lt;Void&gt; cb)</code>.
 * </p>
 * <p>
 *     The default request callback is a {@link groovy.lang.Closure Closure} that doesn't return anything.  All of
 *     those API methods mentioned above take the default request callback as their last argument.
 * </p>
 * <p>
 *     The response object above is an instance of {@link MODEL} class.  You can read about it later, but for example,
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
 *     def server = createMyServerModel()
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
 *     {@link MODEL} instance representing the HTTP response.  All of those API methods mentioned above (<code>GET</code>,
 *     <code>POST</code>, and so on) take the <code>Map</code> of additional request callbacks as their first argument.
 * </p>
 * <p>
 *     Dealing with common HTTP error responses like above can get boring after awhile.  Worry not, there's another way.
 * </p>
 * <h1>Default callbacks</h1>
 * <p>
 *     One can attach default callbacks to the <code>API</code> instance.  These are just like additional request
 *     callbacks, except that these are considered for all future HTTP requests made through this instance of the
 *     <code>API</code>.
 * </p>
 * <p>
 *     For example, the above 401 response can be handled once and for all like this:
 * </p>
 * <pre>
 *     api.callback 401: { log.fatal("configuration error: the username/password is no good") }
 * </pre>
 * <p>
 *     From that point on, all the requests made through this <code>API</code> instance would have that callback in
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
 *     api.callback info: { log.fatal("oh my, assumptions are all broken" },
 *                  redirect: { log.fatal("dear dear, but I don't want to go elsewhere") }
 * </pre>
 * <p>
 *     Of course, in reality, one would log some details from the passed in {@link MODEL} instance.
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
 *     <li>If there's a default callback for "200" attached to the <code>API</code> instance, that is invoked</li>
 *     <li>If there's an additional request callback for "info", that is invoked</li>
 *     <li>If there's a default callback for "info" attached to the <code>API</code> instance, that is invoked</li>
 *     <li>Failing all above, the default request callback is invoked</li>
 * </ol>
 * <p>
 *     On the other hand, for an HTTP response "400 Bad Request", the resolution path is a bit longer due to the more
 *     generic <code>error</code> category:
 * </p>
 * <ol>
 *     <li>request callback for "400"</li>
 *     <li>default callback for "400"</li>
 *     <li>request callback for "client_error"</li>
 *     <li>default callback for "client_error"</li>
 *     <li>request callback for "error"</li>
 *     <li>default callback for "error"</li>
 *     <li>default request callback</li>
 * </ol>
 * <p>
 *     In all examples above, remember that only one callback is invoked for one HTTP exchange: the first one that
 *     matches.
 * </p>
 * <h1>Network error handling</h1>
 * <p>
 *     The additional request callbacks and the default callbacks attached to the <code>API</code> instance, are all by
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
class API {
    private final Logger log = LoggerFactory.getLogger(API)

    /**
     * UpCloud API host.
     */
    private static final String HOST = 'https://api.upcloud.com'
    /**
     * UpCloud API context path.
     */
    private static final String API_VERSION = '/1.2/'
    /**
     * HTTP response status categories.
     */
    private static final List<Tuple2<Range<Integer>, String>> HTTP_STATUS_CATEGORIES = [
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
    private static final List<String> HTTP_STATUS_CATEGORY_NAMES = [
            'info', 'success', 'redirect', 'client_error', 'server_error', 'error'
    ]
    /**
     * HTTP response status code minimum.
     */
    private static final Integer HTTP_STATUS_CODE_MIN = 100
    /**
     * HTTP response status code maximum.
     */
    private static final Integer HTTP_STATUS_CODE_MAX = 599


    /**
     * HTTP method descriptions.
     *
     * <p>
     * These are used to create the HTTP methods dynamically.  The values are the range of how many method arguments
     * they take, and the boolean describes whether the method takes an entity body or not.
     * </p>
     */
    private final static Map<String, Tuple2<Range<Integer>, Boolean>> HTTP_METHODS = [
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
            'User-Agent'   : 'Groovy UpCloud/0.1 ',
    ]
    /**
     * Default callbacks.
     *
     * <p>
     * Application can store common callbacks in here.  The key is the HTTP response status code as a String, e.g. "500",
     * or a category like "server_error".
     * </p>
     */
    private final Map<String, Closure<Void>> defaultCallbacks = [ : ]
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
    API(HTTP http, JSON json, @Named("username") String username, @Named("password") String password) {
        this.http = http
        this.json = json
        requestHeaders['Authorization'] += Base64.encoder.encodeToString("$username:$password".bytes)
        requestHeaders['User-Agent'] += http.userAgent
    }

    /**
     * Set or clear default callbacks.
     *
     * <p>
     *     The set of default callbacks is empty by default.  Application can store common callbacks in this map.  The
     *     keys in the given map are HTTP response status codes like 500 or 404, or HTTP status categories: "info",
     *     "success", "redirect", "client_error", "server_error", "error".  If the key is not recognized as either a
     *     status code or category, an {@ IllegalAgumentException} is thrown.
     * </p>
     * <p>
     *     Note that the old callbacks are not removed, unless their corresponding key is set to <code>null</code> in
     *     {@param cbs}.
     * </p>
     *
     * @param cbs Default callbacks to set or clear.  Note that any previously set callbacks are not cleared unless
     *            they are explicitly set to <code>null</code> in this argument.
     */
    void callback(Map<?, Closure<Void>> cbs) {
        final Map<String, Closure<Void>> defaultCallbacks = internalize(cbs)

        defaultCallbacks.each { String status, Closure<Void> cb ->
            if (cb)
                this.defaultCallbacks[status] = cb
            else
                this.defaultCallbacks.remove(status)
        }
    }

    /**
     * Perform a HTTP request.
     *
     * @param cbs Additional request callbacks.
     * @param method HTTP method.
     * @param resource Resource path relative to the API context path, i.e. without leading slash.
     * @param model MODEL to send or null.
     * @param cb Default request callback.
     * @return Whatever is returned by the HTTP implementation for starting an asynchronous request.
     */
    @PackageScope // for testing
    def request(
            final Map<?, Closure<Void>> cbs = [ : ],
            final String method, final String resource, final MODEL model, final Closure<Void> cb) {
        final Map<String, Closure<Void>> requestCallbacks = internalize(cbs)

        http.execute(new Exchange(
                host: HOST,
                method: method,
                resource: API_VERSION + resource,
                headers: new SimpleHeaders(requestHeaders),
                body: model ? json.encode(model as Map) : null,
                cb: { final META meta, final InputStream body, final ERROR err ->
                    if (!meta) {
                        callApp(cb, null, err) // err ought to be non-null
                        return
                    }

                    Map<String, Object> repr = decode(meta.headers, body)
                    MODEL m = new MODEL(repr: repr, API: this, META: meta)
                    final Closure<Void> callback = chooseCallback(meta.status, cb, requestCallbacks)
                    callApp(callback, m, null)
                    return
                }))
    }

    /**
     * MOP method for HTTP request methods.
     *
     * <p>
     *     This implementation adds the following methods:
     * </p>
     * <ul>
     *     <li>GET(Map cbs = [:], String resource, Closure<Void> cb)</li>
     *     <li>DELETE(Map cbs = [:], String resource, Closure<Void> cb)</li>
     *     <li>PUT(Map cbs = [:], String resource, MODEL model, Closure<Void> cb)</li>
     *     <li>POST(Map cbs = [:], String resource, MODEL model, Closure<Void> cb)</li>
     * </ul>
     * <p>
     *     To be exact, the Map argument can exists anywhere, and it is moved to the beginning of the argument list.
     *     This is to allow the Groovy style of keyword arguments:
     * </p>
     * <pre>
     *     api.GET('some-resource',
     *             error: {
     *                 // handle error
     *             },
     *             {
     *                 // handle all the other cases
     *             })
     * </pre>
     * <p>
     *     This implementation (mainly the move operation of the map) also enables the following higher level APIs:
     * </p>
     * <pre>
     *     class Server extends MODEL {
     *         def create(...args) {
     *             this.API.POST('server', this.wrapper(), *args)
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
    def methodMissing(final String name, final def args) {
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

            // If this is a GET or DELETE, force the fourth argument be null model
            if (method.second)
                ARGV.add(3, null)
            //else if (!(ARGV[3] instanceof MODEL))
            //    throw new MissingMethodException(name, this.class, args)

            // The final argument is the mandatory default callback
            //if (!(ARGV[4] instanceof Closure<Void>))
            //    throw new MissingMethodException(name, this.class, args)

            // Make the call!
            return request(*ARGV)
        }
        throw new MissingMethodException(name, this.class, args)
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
     * @return MODEL representation, or empty Map.
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

    /**
     * Calls back to the app.
     * <p>
     *     Tries to accommodate to the number of arguments expected by the app, and shields the HTTP implementation
     *     from having to deal with exceptions.
     * </p>
     *
     * @param callback Selected callback.
     * @param model HTTP response or <code>null</code>
     * @param err ERROR or <code>null</code>
     */
    private void callApp(final Closure<Void> callback, final MODEL model, final ERROR err) {
        try {
            if (callback.maximumNumberOfParameters == 2)
                callback(model, err)
            else
                callback(model)
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
     * @param defaultRequestCallback The default callback.
     * @param requestCallbacks The request callbacks.
     * @return A callback corresponding to the response status.
     */
    private Closure<Void> chooseCallback(final int statusCode, final Closure<Void> defaultRequestCallback, final Map<String, Closure<Void>> requestCallbacks) {
        if (!statusCode)
            return defaultRequestCallback

        // Try exact match to the status code first
        Closure<Void> callback = selectCallback(statusCode.toString(), requestCallbacks)
        if (callback)
            return callback

        // Try one of the broad categories
        callback = HTTP_STATUS_CATEGORIES.findResult { final Range<Integer> range, final String category ->
            selectCallback(statusCode, range, category, requestCallbacks)
        }
        if (callback)
            return callback

        // Fall back to default callback;
        return defaultRequestCallback;
    }

    /**
     * Return a callback for the given category.
     *
     * <p>
     * If the given {@param statusCode} does not match the given {@param range}, <code>null</code> is returned.
     * Otherwise, the given {@param category} is used to search for a callback.  If the category is not found from the
     * request callbacks, then the default callbacks is checked.
     * </p>
     *
     * @param statusCode The response status, an exact number like 200.
     * @param range A status category range, like (200..299).
     * @param category A status category name, like "success".
     * @param requestCallbacks The request callbacks.
     * @return A callback corresponding to the response status, or null.
     */
    private Closure<Void> selectCallback(
            final int statusCode, final Range<Integer> range, final String category,
            final Map<String, Closure<Void>> requestCallbacks) {
        statusCode in range ? selectCallback(category, requestCallbacks) : null
    }

    /**
     * Return a callback for the given status or category.
     *
     * <p>
     * A callback from request callbacks is returned if found.  Otherwise, a callback from default callbacks is returned
     * if it exists.
     * </p>
     *
     * @param selector The response status, either an exact number like "200" or a category like "success".
     * @param requestCallbacks The request callbacks.
     * @return A callback corresponding to the selector, or null.
     */
    private Closure<Void> selectCallback(final String selector, final Map<String, Closure<Void>> requestCallbacks) {
        requestCallbacks[selector] ?: defaultCallbacks[selector]
    }

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
    private Map<String, Closure<Void>> internalize(final Map<?, Closure<Void>> cbs) {
        if (!cbs) return [:]
        cbs.inject(new LinkedHashMap<String, Closure<Void>>()) { Map callbacks, Map.Entry<?, Closure<Void>> cb ->
            callbacks[internalizeStatus(cb.key)] = cb.value
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
            if (statusCode >= HTTP_STATUS_CODE_MIN && statusCode <= HTTP_STATUS_CODE_MAX)
                return statusCode.toString()
            throw new IllegalArgumentException(
                    "HTTP status code must be in range (${HTTP_STATUS_CODE_MIN}..${HTTP_STATUS_CODE_MAX}): $status")
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

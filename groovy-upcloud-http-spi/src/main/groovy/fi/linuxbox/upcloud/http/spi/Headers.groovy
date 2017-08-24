package fi.linuxbox.upcloud.http.spi

/**
 * A collection of HTTP message headers.
 *
 * <p>
 * Don't fall for the simplification that a header is just a name value pair.  It is, but each HTTP message can have
 * multiple headers with the same name.  And the names are case-insensitive.  Furthermore, each header value is
 * actually a comma separated list.  People often refer to these value <i>elements</i> as "the header value".  To top
 * it off, each of those elements can have its own set of parameters.
 * </p>
 */
interface Headers {
    /**
     * An iterator over all HTTP headers in this collection.
     *
     * <p>
     * Since a single HTTP message may contain multiple headers with the same name, this iterator will give them to you
     * separately.
     * </p>
     *
     * <p>
     * This method can be used when the meaning of the headers doesn't really matter.  E.g. when copying the headers
     * from one place to another.
     * </p>
     *
     * @return The iterator over all HTTP headers in this collection.
     */
    Iterator<Header> all()

    /**
     * An iterator over all HTTP header value elements of all headers with the given name in this collection.
     *
     * <p>
     * This method is used when meaning of the headers drives the decision making in the program.
     * </p>
     *
     * <p>
     * For example, given the following headers:
     * </p>
     *
     * <pre>
     *     Set-Cookie: cookie1=a; path=/; domain=localhost
     *     Set-Cookie: cookie2=b; path="/", cookie3=c; domain="localhost"
     * </pre>
     *
     * <p>
     * This iterator will allow you to collect all the three cookies as if they were in three separate headers:
     * </p>
     *
     * <pre>
     *     headers['Set-Cookie'].each { cookie ->
     *         println "Cookie called ${cookie.name} has a value of ${cookie.value}"
     *         cookie.parameters.each { param ->
     *             println " - ${param.name} = ${param.value}
     *         }
     *     }
     * </pre>
     *
     * <p>
     * Note, however, that not all elements have a value.  E.g. in header 'Accept: application/json', the sole element
     * has name 'application/json' and a null value.
     * </p>
     *
     * @param name Case-insensitive header name.
     * @return Iterator over value elements of headers with the given name.
     */
    Iterator<HeaderElement> getAt(String name)
}

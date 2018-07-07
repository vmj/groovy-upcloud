/*
 * Groovy UpCloud library - HTTP SPI Module
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
package fi.linuxbox.upcloud.http.spi

import groovy.transform.KnownImmutable

/**
 * An iterable over a collection of HTTP message headers.
 *
 * <p>
 *     Don't fall for the simplification that a header is just a name value pair.  It is, but each HTTP message can
 *     have multiple headers with the same name.  And the names are case-insensitive.  Furthermore, each header value
 *     is actually a comma separated list.  People often refer to these value <i>elements</i> as "the header value".
 *     To top it off, each of those elements can have its own set of parameters.
 * </p>
 * <p>
 *     When this iterable is used via {@link #iterator} method or anything built on it, like the
 *     <a href="http://docs.groovy-lang.org/latest/html/groovy-jdk/java/lang/Iterable.html">Groovy extensions</a>,
 *     the headers with the same (case-insensitive) name will be available separately.  This is useful when the
 *     meaning of the headers doesn't really matter.  E.g. when copying the headers from one place to another.
 * </p>
 */
@KnownImmutable
interface Headers extends Iterable<Header> {
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
     * <pre><code class="http">
     *     Set-Cookie: cookie1=a; path=/; domain=localhost
     *     Set-Cookie: cookie2=b; path="/", cookie3=c; domain="localhost"
     * </code></pre>
     *
     * <p>
     * This iterator will allow you to collect all the three cookies as if they were in three separate headers:
     * </p>
     *
     * <pre><code class="groovy">
     *     headers['Set-Cookie'].each { cookie ->
     *         println "Cookie called ${cookie.name} has a value of ${cookie.value}"
     *         cookie.each { param ->
     *             println " - ${param.name} = ${param.value}
     *         }
     *     }
     * </code></pre>
     * <p>
     *     Above would print:
     * </p>
     * <pre>
     *     Cookie c1 has a value of a
     *      - path = /
     *      - domain = localhost
     *     Cookie c2 has a value of b
     *      - path = /
     *     Cookie c3 has a value of c
     *      - domain = localhost
     * </pre>
     * <p>
     * Note, however, that not all elements have a value.  E.g. in header 'Accept: application/json', the sole element
     * has name 'application/json' and a null value.
     * </p>
     *
     * @param name Case-insensitive header name.
     * @return Iterator over value elements of headers with the given name.
     */
    Iterator<HeaderElement> getAt(final String name)
}

package fi.linuxbox.upcloud.http.spi;

import java.util.Iterator;

/**
 * An HTTP header.
 *
 * <p>
 * An HTTP header is a name value pair, where the value can have multiple elements embedded in it.
 * </p>
 */
public interface Header extends Iterable<HeaderElement> {

    /**
     * Name of this HTTP header.
     *
     * <p>
     * E.g. 'Set-Cookie' in a header 'Set-Cookie: c2=b; path="/", c3=c; domain="localhost"'.
     * </p>
     *
     * @return The name of this HTTP header.
     */
    String getName();

    /**
     * Value of this HTTP header.
     *
     * <p>
     * E.g. given a header 'Set-Cookie: c2=b; path="/", c3=c; domain="localhost"', this will return
     * 'c2=b; path="/", c3=c; domain="localhost"', i.e. two elements in the string.
     * </p>
     *
     * <p>
     * If you need to access each value element of this header separately, use the {#iterator()} method.
     * </p>
     *
     * @return The value of this HTTP header.
     */
    String getValue();

    /**
     * Iterator over separate elements of this HTTP header value.
     *
     * <p>
     * This iterator will allow you to walk through the comma separated parts of the value of this HTTP header.
     * </p>
     *
     * <p>
     * E.g. given a header 'Set-Cookie: c2=b; path="/", c3=c; domain="localhost"', this iterator will first
     * given you the header element corresponding to 'c2=b; path="/"', and then the header element corresponding to
     * 'c3=c; domain="localhost"'.
     *
     * @return Iterator over elements of the value of this HTTP header.
     */
    default Iterator<HeaderElement> getElements() { return iterator(); }
}

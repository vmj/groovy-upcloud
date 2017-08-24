package fi.linuxbox.upcloud.http.spi;

import java.util.Iterator;

/**
 * An HTTP header value element.
 *
 * <p>
 * An HTTP header value element can have a semicolon separated list of parameters.
 * </p>
 */
public interface HeaderElement extends Iterable<Parameter> {
    /**
     * Name of this HTTP header value element.
     *
     * <p>
     * E.g. 'application/json' in header 'Accept: application/json; charset=UTF-8'.
     * </p>
     *
     * @return Name of this element.
     */
    String getName();

    /**
     * Value of this HTTP header value element.
     *
     * <p>
     * E.g. '123' in header 'Set-Cookie: sessionId=123', but <code>null</code> in header 'Accept: test/plain'.
     * </p>
     *
     * @return Value of this element.
     */
    String getValue();

    /**
     * Iterator over the parameters of this HTTP header value element.
     *
     * @return Iterator over parameters of this element.
     */
    default Iterator<Parameter> getParameters() { return iterator(); }
}

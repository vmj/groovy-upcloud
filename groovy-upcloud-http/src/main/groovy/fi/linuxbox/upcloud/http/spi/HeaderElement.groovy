package fi.linuxbox.upcloud.http.spi

/**
 * An HTTP header value element.
 *
 * <p>
 * An HTTP header value element can have a semicolon separated list of parameters.
 * </p>
 */
interface HeaderElement {
    /**
     * Name of this HTTP header value element.
     *
     * <p>
     * E.g. 'application/json' in header 'Accept: application/json; charset=UTF-8'.
     * </p>
     *
     * @return Name of this element.
     */
    String getName()

    /**
     * Value of this HTTP header value element.
     *
     * <p>
     * E.g. '123' in header 'Set-Cookie: sessionId=123', but <code>null</code> in header 'Accept: test/plain'.
     * </p>
     *
     * @return Value of this element.
     */
    String getValue()

    /**
     * Iterator over the parameters of this HTTP header value element.
     *
     * @return Iterator over parameters of this element.
     */
    Iterator<Parameter> getParameters()
}

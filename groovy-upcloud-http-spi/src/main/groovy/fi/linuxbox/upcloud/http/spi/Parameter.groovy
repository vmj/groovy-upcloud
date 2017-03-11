package fi.linuxbox.upcloud.http.spi

/**
 * An HTTP header parameter.
 *
 * <p>
 * HTTP header parameters are a list of name value pairs, and part of the header value.
 * </p>
 */
class Parameter extends Tuple2<String, String> {
    /**
     * The sole constructor.
     *
     * @param name Name of the parameter.  E.g. "charset" in a header "Accept: application/json; charset=UTF-8"
     * @param value value of the parameter.  E.g. "UTF-8" in a header "Accept: application/json; charset=UTF-8"
     */
    Parameter(final String name, final String value) {
        super(name, value)
    }

    /**
     * Name of this HTTP header parameter.
     *
     * <p>
     * E.g. "charset" in a header "Accept: application/json; charset=UTF-8".
     * </p>
     *
     * @return The name of this HTTP header parameter.
     */
    String getName() { first }

    /**
     * Value of this HTTP header parameter.
     *
     * <p>
     * E.g. "UTF-8" in a header "Accept: application/json; charset=UTF-8".
     * </p>
     *
     * @return The value of this HTTP header parameter.
     */
    String getValue() { second }
}

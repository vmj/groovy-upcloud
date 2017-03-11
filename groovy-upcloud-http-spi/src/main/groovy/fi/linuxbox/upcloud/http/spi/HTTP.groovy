package fi.linuxbox.upcloud.http.spi
/**
 * An interface which is implemented by the HTTP implementation.
 */
interface HTTP {
    /**
     * Returns the User-Agent string for this implementation.
     *
     * @return User-Agent string for this implementation.
     */
    String getUserAgent()

    /**
     * Execute the HTTP exchange asynchronously.
     *
     * <p>
     * The request is described by the exchange, and the response must be sent to the callback in the exchange.
     * </p>
     *
     * @param exchange HTTP exchange to execute.
     */
    void execute(final Exchange exchange)
}

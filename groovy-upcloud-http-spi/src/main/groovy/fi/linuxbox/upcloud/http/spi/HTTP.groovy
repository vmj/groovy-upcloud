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
     * @param request HTTP request to execute.
     */
    void execute(final Request request)
}

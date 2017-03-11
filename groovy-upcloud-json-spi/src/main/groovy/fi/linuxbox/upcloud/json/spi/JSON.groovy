package fi.linuxbox.upcloud.json.spi

/**
 * An interface which is implemented by the JSON implementation.
 */
interface JSON {
    /**
     * Parse the representation from the input stream.
     *
     * @param data The UTF-8 JSON data.
     * @return Java representation of the JSON.
     */
    Map<String, Object> decode(InputStream data)

    /**
     * Write the representation to a stream.
     *
     * @param repr Java representation of the JSON to be written.
     * @return The UTF-8 JSON data as an input stream.
     */
    InputStream encode(Map<String, Object> repr)
}

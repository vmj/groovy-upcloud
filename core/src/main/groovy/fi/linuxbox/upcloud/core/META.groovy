package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.core.http.Headers

/**
 * Meta information about a model.
 *
 * <p>
 * This is basically the head of the HTTP message: the status code, the reason phrase, and the headers.  In the
 * application level callbacks, a reference to an instance of this class can be found in every model.  For example,
 * given the following response from the server:
 * </p>
 *
 * <pre>
 *     HTTP1.1 200 OK
 *     Content-Type: application/json; charset=UTF-8
 *
 *     {
 *         "error": {
 *             "error_code": "BAD_REQUEST",
 *             "error_message": "The value of foo cannot be bar"
 *         }
 *     }
 * </pre>
 *
 * The application callback will be called with an instance of MODEL as the first parameter:
 *
 * <pre>
 *     { model ->
 *       assert model instanceof fi.linuxbox.upcloud.core.MODEL
 *       assert model.META.status == "200"
 *       assert model.META.message == "OK"
 *       assert model.error instanceof fi.linuxbox.upcloud.model.Error
 *       assert model.error.errorCode == "BAD_REQUEST"
 *       assert model.error.errorMessage == "The value of foo cannot be bar"
 *     }
 * </pre>
 */
class META {
    /**
     * The HTTP status code.
     *
     * <p>
     * This is never null.
     * </p>
     */
    final String status
    /**
     * The HTTP reason phrase.
     *
     * <p>
     * This may be null.
     * </p>
     */
    final String message
    /**
     * The HTTP headers.
     */
    final Headers headers

    /**
     * Designated constructor.
     *
     * @param status The HTTP status code.  This must not be null.
     * @param message The HTTP status phrase.  May be null.
     * @param headers The HTTP headers.
     */
    META(int status, String message, Headers headers) {
        this.status = status.toString()
        this.message = message
        this.headers = headers
    }

    /**
     * Constructor that sets the message to null.
     *
     * @param status The HTTP status code.  This must not be null.
     * @param headers The HTTP headers.
     */
    META(int status, Headers headers) {
        this(status, null, headers)
    }
}

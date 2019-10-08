/*
 * Groovy UpCloud library - HTTP SPI Module
 * Copyright (C) 2018  Mikko Värri
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

import groovy.transform.CompileStatic
import groovy.transform.Immutable

/**
 * Meta information about a resource.
 *
 * <p>
 * This is basically the head of the HTTP message: the status code, the reason phrase, and the headers.  In the
 * application level callbacks, a reference to an instance of this class can be found in every resource.  For example,
 * given the following odd response from the server:
 * </p>
 *
 * <pre><code class="http">
 *     HTTP1.1 200 OK
 *     Content-Type: application/json; charset=UTF-8
 *
 *     {
 *         "error": {
 *             "error_code": "BAD_REQUEST",
 *             "error_message": "The value of foo cannot be bar"
 *         }
 *     }
 * </code></pre>
 *
 * The application callback will be called with an instance of {@link fi.linuxbox.upcloud.core.Resource} as the first parameter:
 *
 * <pre><code class="groovy">
 *     { response ->
 *       assert response instanceof fi.linuxbox.upcloud.core.Resource
 *       assert response.META.status == 200
 *       assert response.META.message == "OK"
 *       assert response.error instanceof fi.linuxbox.upcloud.resource.Error
 *       assert response.error.errorCode == "BAD_REQUEST"
 *       assert response.error.errorMessage == "The value of foo cannot be bar"
 *     }
 * </code></pre>
 */
@CompileStatic
@Immutable
class META {
    Integer status
    String message
    Headers headers
    Request request

    /**
     * Returns a string representation of this instance.
     *
     * @return A string representation.
     */
    @Override
    String toString() {
        "$status $message ($request)"
    }
}

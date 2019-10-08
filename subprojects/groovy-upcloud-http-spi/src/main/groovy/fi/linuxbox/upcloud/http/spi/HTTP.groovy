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

/**
 * An interface which is implemented by the HTTP implementation.
 */
@CompileStatic
interface HTTP extends Closeable {
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
     * @param body Request entity, or <code>null</code>.
     * @param cb Completion callback to call when the HTTP exchange is finished.
     */
    void execute(final Request request, final byte[] body, final CompletionCallback cb)
}

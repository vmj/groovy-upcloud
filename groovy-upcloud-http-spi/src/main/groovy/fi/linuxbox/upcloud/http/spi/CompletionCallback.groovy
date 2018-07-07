/*
 * Groovy UpCloud library - HTTP SPI Module
 * Copyright (C) 2018  <mikko@varri.fi>
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
 * HTTP completion callback.
 */
@CompileStatic
interface CompletionCallback {
    /**
     * Method to invoke when the HTTP exchange is finished.
     * <p>
     *     In case of failed network or cancelled request, the <code>META</code> must be <code>null</code> and the
     *     <code>error</code> should be non-<code>null</code>.  In case the server responds with an HTTP error,
     *     <code>META</code> must be non-<code>null</code> and <code>error</code> must be <code>null</code>.  The
     *     second argument, the <code>InputStream</code>, can be <code>null</code> if the HTTP response
     *     didn't have an entity body.
     * </p>
     * <p>
     *     The Session will close the input stream when done with it.
     * </p>
     *
     * @param meta information about the response, or <code>null</code>
     * @param entity response entity or <code>null</code>
     * @param error information about failure to talk to the server, or <code>null</code>
     * @throws InterruptedException in case the caller is shutting down
     */
    void completed(final META meta, final InputStream entity, final Throwable error) throws InterruptedException
}

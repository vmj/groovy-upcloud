/*
 * Groovy UpCloud library - Script Module
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
package fi.linuxbox.upcloud.script


import fi.linuxbox.upcloud.core.Resource
import fi.linuxbox.upcloud.core.Session
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j

import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

/**
 * A session implementation that returns {@link CompletableFuture} from the {@link Session#request} method.
 */
@CompileStatic
@InheritConstructors
@Slf4j
class CompletableFutureSession extends Session<CompletableFuture<Resource>> {
    @Override
    protected CompletableFuture<Resource> unresolvedPromise() {
        new CompletableFuture<Resource>()
    }

    @Override
    protected BiConsumer<Resource, Throwable> promiseResolver(final CompletableFuture<Resource> unresolvedPromise) {
        { final Resource resource, final Throwable err ->
            if (err)
                unresolvedPromise.completeExceptionally(err)
            else
                unresolvedPromise.complete(resource)
            null
        } as BiConsumer<Resource, Throwable>
    }
}

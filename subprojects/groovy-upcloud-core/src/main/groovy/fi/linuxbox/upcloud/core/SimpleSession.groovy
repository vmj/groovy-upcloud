/*
 * Groovy UpCloud library - Core Module
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
package fi.linuxbox.upcloud.core

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j

import java.util.function.BiConsumer

/**
 * A session implementation that does not return anything.
 * <p>
 *     This is enough for applications that do not have any dependencies between calls the the API.
 *     For example, when making a single call to get account information.
 * </p>
 */
@CompileStatic
@InheritConstructors
@Slf4j
class SimpleSession extends Session<Void> {

    @Override
    protected Void unresolvedPromise() {
        null
    }

    @Override
    protected BiConsumer<Resource, Throwable> promiseResolver(final Void unresolvedPromise) {
        null
    }
}

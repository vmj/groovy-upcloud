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
package fi.linuxbox.upcloud.core.http.simple

import fi.linuxbox.upcloud.http.spi.Header
import groovy.transform.CompileStatic


/**
 * Iterator over simple headers.
 */
@CompileStatic
class SimpleHeaderIterator implements Iterator<Header> {
    private final Iterator<Map.Entry<String, String>> it

    SimpleHeaderIterator(final Iterator<Map.Entry<String, String>> it) {
        this.it = it
    }

    @Override
    boolean hasNext() {
        it.hasNext()
    }

    @Override
    Header next() {
        new SimpleHeader(it.next())
    }
}

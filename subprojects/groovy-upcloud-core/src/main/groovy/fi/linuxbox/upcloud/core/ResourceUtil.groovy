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

import java.util.regex.Pattern

import static fi.linuxbox.upcloud.core.NamingContract.javaClassToJavaProperty

@CompileStatic
class ResourceUtil {
    private static final Pattern META_PROPERTIES = ~/^[A-Z]+$/

    /**
     * Returns the given resource wrapped in another resource.
     *
     * <p>
     * Many of the UpCloud APIs require a wrapping JSON object to be sent into the resources,
     * and this method is used in those places.
     * </p>
     *
     * @return A wrapped resource whose sole property is the given resource.
     */
    static Resource wrapped(final Resource resource) {
        new Resource(
                HTTP: resource.HTTP,
                META: resource.META,
                repr: [(javaClassToJavaProperty(resource.class)): resource]
        )
    }

    /**
     * Returns properties of the given resource.
     *
     * <p>
     * This method skips GroovyObject properties (every Groovy object has a 'class' property),
     * meta resource properties (every Resource has 'META' and 'HTTP' properties),
     * and properties whose value is <code>null</code>.
     * </p>
     *
     * @return Properties of the given resource.
     */
    static Map<String, Object> resourceProperties(final Resource resource) {
        resource.properties.findAll {
            !(it.key == 'class' || it.value == null || it.key =~ META_PROPERTIES)
        }
    }
}

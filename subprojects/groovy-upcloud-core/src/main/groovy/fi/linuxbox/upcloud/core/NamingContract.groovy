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

/**
 * Name juggling service.
 */
class NamingContract {

    /**
     * Converts Class name to a Java style property name.
     *
     * @param clazz Simple class name, i.e. name without the package.
     * @return Java style property name.
     */
    static String javaClassToJavaProperty(final Class clazz) {
        clazz.simpleName.replaceAll(/([A-Z])([A-Z]+)/, {
            it[1] + it[2].toLowerCase() // RESOURCE -> Resource
        }) replaceFirst(/^([A-Z])/, {
            it[0].toLowerCase() // Server -> server
        })
    }

    /**
     * Converts a JSON property name to a Java style property name.
     *
     * <p>
     * For example, 'storage_device' becomes 'storageDevice'.
     * </p>
     *
     * @param type_name JSON style property name.
     * @return Java style property name.
     */
    static String jsonPropertyToJavaProperty(final String type_name) {
        type_name.replaceAll(/_([a-z])/, {
            it[1].toUpperCase() // type_name -> typeName
        }) replaceAll(/-/, '_') // typeName-1CPU-1GB -> typeName_1CPU_1GB
    }

    /**
     * Converts a JSON property name to a class name.
     *
     * <p>
     * For example, 'storage_device' becomes 'StorageDevice'.
     * </p>
     *
     * @param type_name JSON style property name.
     * @return Simple class name, i.e. name without the package.
     */
    static String jsonPropertyToJavaClass(final String type_name) {
        type_name.replaceAll(/(?:^|_)([a-z])/, {
            it[1].toUpperCase() // type_name -> TypeName
        }) replaceAll(/-/, '_') // TypeName-1CPU-1GB -> TypeName_1CPU_1GB
    }

    /**
     * Converts a class name or a Java property name to a JSON property name.
     *
     * <p>
     * For example, 'StorageDevice' becomes 'storage_device'.
     * </p>
     *
     * @param className Simple name of a class, i.e. name without the package.
     * @return JSON style property name.
     */
    static String javaClassOrPropertyToJsonProperty(final String className) {
        className.replaceAll(/([A-Z])([A-Z]+)/, {
            it[1] + it[2].toLowerCase() // RESOURCE -> Resource
        }) replaceFirst(/^([A-Z])/, {
            it[0].toLowerCase() // Server -> server
        }) replaceAll(/([A-Z])/, {
            '_' + it[0].toLowerCase() // storageDevice -> storage_device
        })
    }

    /**
     * Converts a class name to a URL path segment.
     *
     * <p>
     * For example, 'IpAddress' becomes 'ip_address'.
     * </p>
     *
     * @param className Simple name of a class, i.e. name without the package.
     * @return URL path style segment.
     */
    static String javaClassToPathSegment(final String className) {
        className.replaceAll(/([A-Z])([A-Z]+)/, { it[1] + it[2].toLowerCase() }) // RESOURCE -> Resource
                .replaceFirst(/^([A-Z])/, { it[0].toLowerCase() }) // Server -> server
                .replaceAll(/([A-Z])/, { '_' + it[0].toLowerCase() }) // storageDevice -> storage_device
    }
}

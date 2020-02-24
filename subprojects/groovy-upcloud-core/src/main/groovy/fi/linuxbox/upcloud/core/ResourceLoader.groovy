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
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
class ResourceLoader {
    private static final GroovyClassLoader gcl = new GroovyClassLoader(Resource.classLoader)
    private static final String resourcePackageName = "fi.linuxbox.upcloud.resource"

    /**
     * Returns a Class corresponding to the named resource.
     *
     * <p>
     * For example, <code>loadResourceClass('Server')</code> would return the Class for
     * <code>fi.linuxbox.upcloud.resource.Server</code> class.
     * </p>
     *
     * @param resourceClassName Simple name of the resource class, i.e. name without the package.
     * @return Class for the named resource.
     */
    static Class loadResourceClass(final String resourceClassName) {
        Class clazz
        try {
            clazz = gcl.loadClass("${resourcePackageName}.$resourceClassName")
            log.trace("Loaded resource $resourceClassName")
        } catch (final ClassNotFoundException ignored) {
            log.trace("Generating resource $resourceClassName")
            clazz = gcl.parseClass("""
                                   package ${resourcePackageName}

                                   import ${Resource.class.name}

                                   @groovy.transform.CompileStatic
                                   class $resourceClassName extends ${Resource.class.simpleName} {
                                       $resourceClassName(final Map kwargs = [:]) {
                                           super(kwargs)
                                       }
                                   }
                                   """)
        }
        clazz
    }

    static Object instantiateResourceClass(final Class resourceClass, final Map kwargs = [:]) {
        try {
            resourceClass.getDeclaredConstructor(Map)
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("Resource class ${resourceClass.simpleName} must declare contructor that takes a map", e)
        }
        resourceClass.metaClass.invokeConstructor(kwargs)
    }

    static Object instantiateResourceClass(final String resourceClassName, final Map kwargs = [:]) {
        instantiateResourceClass(loadResourceClass(resourceClassName), kwargs)
    }
}

package fi.linuxbox.upcloud.core

import groovy.util.logging.Slf4j

@Slf4j
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
            log.debug("Loaded resource $resourceClassName")
        } catch (final ClassNotFoundException ignored) {
            log.info("Generating resource $resourceClassName")
            clazz = gcl.parseClass("""
                                   package ${resourcePackageName}

                                   import ${Resource.class.name}

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
        resourceClass.metaClass.invokeConstructor(kwargs)
    }

    static Object instantiateResourceClass(final String resourceClassName, final Map kwargs = [:]) {
        instantiateResourceClass(loadResourceClass(resourceClassName), kwargs)
    }
}

package fi.linuxbox.upcloud.core

import groovy.transform.PackageScope
import org.slf4j.*

/**
 * Model for a resource in UpCloud, e.g. server, storage device, or an IP address.
 *
 * <p>
 * Together with the {@link API} class, this class is the most core of the Groovy UpCloud library.  You probably don't
 * use this directly, but you could.  This class allows for dynamic creation of UpCloud resource representations and
 * their properties.
 * </p>
 *
 * <p>
 * For example, at the time of this writing, the UpCloud account API returns <code>credits</code> and
 * <code>username</code> properties, but no <code>email</code> property.  Not saying that it should, but it could be
 * useful.  Now, if a future revision of the API adds that property, it would be immediately available in the
 * <code>Account</code> resource of this library, even without an update.
 * </p>
 * <pre>
 *     HTTP/1.1 200 OK
 *     Content-Type: application/json; charset=UTF-8
 *
 *     {
 *         "account": {
 *             "credits": 1000,
 *             "username": "foo_bar",
 *             "email": "juliet.capolet@protagonist.org"
 *         }
 *     }
 * </pre>
 * <pre>
 *     upcloud.account({ response ->
 *         assert response.account.email == "juliet.capolet@protagonist.org"
 *     })
 * </pre>
 *
 * <p>
 * Similarly, if a future revision of the UpCloud API would add a completely new property to, say, a server, represented
 * as a new kind of object, then it would be visible in the responses generated by this library.  For example:
 * </p>
 * <pre>
 *     HTTP/1.1 200 OK
 *     Content-Type: application/json; charset=UTF-8
 *
 *     {
 *         "server": {
 *             ...
 *             "docker_images": {
 *                 "docker_image": [
 *                     {
 *                         "image": "ubuntu:latest",
 *                         "state": "running"
 *                     },
 *                     {
 *                         "image": "nginx:latest",
 *                         "state": "running"
 *                     }
 *                 ]
 *             },
 *             ...
 *         }
 *     }
 * </pre>
 *
 * <p>
 * Above, the <code>docker_images</code> property is an imaginary one, but because the server returned it, it would be
 * available in the application callback as follows:
 * <p>
 *
 * <pre>
 *     server.load({ response ->
 *       assert response.server instanceof fi.linuxbox.upcloud.resource.Server
 *       // Above is old news.
 *       // Now for the fun part:
 *       assert response.server.dockerImages instanceof List
 *       assert response.server.dockerImages.every { it instanceof fi.linuxbox.upcloud.resource.DockerImage }
 *       assert response.server.dockerImages[0].state == "running"
 *     })
 * </pre>
 *
 * <p>
 * In reality, you wouldn't be able to write that code exactly as shown, because the <code>DockerImage</code> class is
 * not available at compile time, i.e. your compiler would complain about the reference.  But at runtime the class is
 * as real as any other resource class.
 * </p>
 *
 * <p>
 *     THE FOLLOWING IS JUST UNFINISHED NOTES
 * </p>
 *
 * <p>
 * Representation is always a Map with String keys.  The value can be
 * </p>
 *
 * <ul>
 *   <li>A list wrapper: a map with one key whose value is a list</li>
 *   <li>A resource: a map that is not a list wrapper</li>
 *   <li>A simple value: anything except a map</li>
 * </ul>
 *
 * <p>
 * JSON data types:
 * </p>
 *
 * <ul>
 *   <li>object: either a list wrapper or a resource</li>
 *   <li>array, number, string, boolean, null: a simple value</li>
 * </ul>
 */
class Resource {
    private final Logger log = LoggerFactory.getLogger(Resource)

    final API API
    final META META

    /**
     * Designated, and only, constructor.
     *
     * @param kwargs.API The API instance.  This is used by the resource specific API wrappers, not directly by this class.
     * @param kwargs.META The META instance.  This is received from the HTTP implementation.
     * @param kwargs.repr The Map<String, Object> intermediary representation from the JSON implementations.
     */
    Resource(final Map kwargs = [:]) {
        // the second arg (register=false) makes this metaClass instance scoped
        metaClass = new ExpandoMetaClass(this.class, false, true)
        metaClass.initialize()

        API = kwargs.API
        META = kwargs.META

        final Map<String, ?> map = kwargs.remove('repr') as Map<String, ?>
        map?.each { final String key, final Object value ->
            final String propertyName = propertyName(key)
            switch (value) {
                case ListWrapper:
                    //  value ----v
                    // 'objects': [ 'object': [ [:], ... ]
                    // 'objects': [ 'object': [ string, ... ]
                    final String type_name = value.keySet()[0] // the only key
                    final String className = className(type_name)
                    // e.g.
                    //    [
                    //       prices: [
                    //                  zone: [
                    //                           [:],
                    //                           ...
                    //                        ]
                    //               ]
                    //    ]
                    // becomes
                    //    this.prices = [ Zone(), ... ]
                    final List list = (List) value[type_name]
                    this.metaClass."$propertyName" = list.collect { element ->
                        if (element instanceof Map<String, ?>) {
                            element = ResourceLoader.instantiateResourceClass(className, [*:kwargs, repr: element])
                        }
                        element
                    }
                    break
                case Map/*<String, ?>*/: // groovy cannot handle the generics syntax here
                    // 'object': [key1: *, key2: *, ...]
                    final String className = className(key)
                    this.metaClass."$propertyName" = ResourceLoader.instantiateResourceClass(className, [*:kwargs, repr: value])
                    break
                default:
                    // simple attribute
                    this.metaClass."$propertyName" = value
                    break
            }
        }
    }

    /**
     * Returns properties of this resource.
     *
     * <p>
     * This method skips GroovyObject properties (every Groovy object has a 'class' property), meta resource properties
     * (every Resource has 'META' and 'API' properties), and properties whose value is <code>null</code>.
     * </p>
     *
     * @return Properties of this resource.
     */
    private Map<String, Object> resourceProperties() {
        def reserved = ~/^[A-Z]+$/
        this.properties.findAll {!(it.key == 'class' || it.value == null || it.key =~ reserved) }
    }

    /**
     * Converts this resource to a Map<String, Object> representation for JSON generation.
     *
     * <p>
     * This is used as <code>resource as Map</code> and meant to be invoked only from the API class, just before the resource
     * is put into the queue of to-be-sent requests.
     * </p>
     *
     * <p>
     * Instead of using direct object-to-JSON mapping, this intermediary representation is used because
     * </p>
     * <ul>
     *     <li>
     *         This allows for a quick snapshot of the resource.  Generating the JSON could happen in a background thread.
     *     </li>
     *     <li>
     *         This allows the JSON generator to be stupid.  It doesn't have to deal with conversion between Java and
     *         JSON style of property names.  Also, it doesn't have to know how to filter the properties, or how to
     *         introspect for them in the first place.
     *     </li>
     *     <li>
     *         This allows the JSON to be changed to Protobuf or XML more easily.  Or the JSON implementation to change
     *         mode easily.  This is because the requirements for those modules is very low.
     *     </li>
     * </ul>
     *
     * @param clazz Map.  Nothing else supported.
     * @return Representation of this resource.
     */
    Object asType(Class clazz) {
        resourceProperties().collectEntries { final String key, final Object value ->
            final String property_name = type_name(key)
            [(property_name): value.with {
                switch (it) {
                    case { it instanceof List && !it.isEmpty() }: // non-empty List
                        String type_name_str
                        Closure mapper
                        // Assume that all elements are of the same type
                        final Object firstElement = it[0]
                        switch (firstElement) {
                            case Resource:
                                // this.prices = [ Zone(), ... ]
                                // map[prices] = [ zone: [ [:], ... ] ]
                                // this.timezones = [ "asd", "dsa", ... ]
                                // map[timezones] = [ timezone: [ "asd", "dsa", ... ] ]
                                type_name_str = type_name(firstElement.class.simpleName)
                                mapper = { it as Map }
                                break
                            case String:
                                // HACK: only places I know of where we need this:
                                //       * server creation: [ssh_keys: [ssh_key: [key1, key2]]]
                                //       * tag creation: [servers: [server: [uuid1, uuid2]]]
                                //       * tag update: [servers: [server: [uuid1, uuid2]]]
                                //       so naive unpluralization of the property_name (ssh_keys or servers)
                                //       will do for those.
                                type_name_str = property_name[0..-2]
                                mapper = { it }
                                break
                            default:
                                throw new UnsupportedOperationException("Only lists of Resources or Strings is currently supported!!!")
                        }
                        // the parentheses around the key force the GString to evaluate to String
                        return [(type_name_str): it.collect(mapper)]
                    case Resource:
                        return it as Map
                    default:
                        return it
                }
            }]
        }
    }

    /**
     * Returns this resource wrapped in another resource.
     *
     * <p>
     * Many of the UpCloud APIs require a wrapping JSON object to be sent into the resources, and this method is used
     * in those places.
     * </p>
     *
     * @return A wrapped resource whose sole property is this resource.
     */
    def wrapper() {
        final String propertyName = propertyName(this.class)
        new Resource(API: API, META: META)."$propertyName"(this)
    }

    /**
     * Returns a projection of this resource.
     *
     * <p>
     * Projection is a copy of this resource with some of the properties removed.
     * </p>
     *
     * <p>
     * Some of the UpCloud APIs specifically disallow some of the resource properties from being sent in the requests,
     * and this method is used in those API calls.
     * </p>
     *
     * @param properties A list of property names to exclude.
     * @return A copy of this resource with specified properties removed.
     */
    def proj(final List<String> properties) {
        resourceProperties().grep { it.key in properties }
                .inject ((Resource)this.metaClass.invokeConstructor(API: API, META: META)) {
            final Resource resource, final Map.Entry<String, Object> property -> resource."${property.key}"(property.value)
        }
    }

    /**
     * MOP method that allows resource properties to be set in a fluent fashion.
     *
     * <p>
     * Allows for code like <code>server.name('my server').description('My server')</code> to set two properties for
     * the server resource.
     * </p>
     *
     * @param name Name of the property.
     * @param args Value of the property (must be a single element argument array).
     * @return The resource for chaining.
     */
    def methodMissing(final String name, final def args) {
        if (args?.length == 1)
            this."$name" = args[0]
        this
    }

    /**
     * MOP method that allows resource properties to be missing.
     *
     * <p>
     * Allows for code like <code>if (server.name) ...</code> without actually defining Java Bean property for the name.
     * </p>
     *
     * @param name Name of the property.
     * @return Value of the property or <code>null</code>.
     */
    def propertyMissing(final String name) {
        // Help the trait proxies see all the properties (we used to return null here, but the proxies didn't work).
        this.properties[name]
    }

    /**
     * MOP method that allows resource properties to be set.
     *
     * <p>
     * Allows for code like <code>server.name = "my server"</code> without actually defining Java Bean property for the
     * name.
     * </p>
     *
     * @param name Name of the property.
     * @param arg Value of the property.
     * @return Value of the property.
     */
    def propertyMissing(final String name, final def arg) {
        this.metaClass."$name" = arg
    }

    /**
     * Converts Class name to a Java style property name.
     *
     * @param clazz Simple class name, i.e. name without the package.
     * @return Java style property name.
     */
    @PackageScope
    static String propertyName(final Class clazz) {
        clazz.simpleName.replaceAll(/([A-Z])([A-Z]+)/, { it[1] + it[2].toLowerCase() }) // RESOURCE -> Resource
                .replaceFirst(/^([A-Z])/, { it[0].toLowerCase() }) // Server -> server
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
    @PackageScope
    static String propertyName(final String type_name) {
        type_name.replaceAll(/_([a-z])/, { it[1].toUpperCase() }) // type_name -> typeName
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
    @PackageScope
    static String className(final String type_name) {
        type_name.replaceAll(/(?:^|_)([a-z])/, { it[1].toUpperCase() }) // type_name -> TypeName
    }

    /**
     * Converts a class name to a JSON property name.
     *
     * <p>
     * For example, 'StorageDevice' becomes 'storage_device'.
     * </p>
     *
     * @param className Simple name of a class, i.e. name without the package.
     * @return JSON style property name.
     */
    @PackageScope
    static String type_name(final String className) {
        className.replaceAll(/([A-Z])([A-Z]+)/, { it[1] + it[2].toLowerCase() }) // RESOURCE -> Resource
                .replaceFirst(/^([A-Z])/, { it[0].toLowerCase() }) // Server -> server
                .replaceAll(/([A-Z])/, { '_' + it[0].toLowerCase() }) // storageDevice -> storage_device
    }

    /**
     * Returns <code>true</code> if the <code>value</code> is a list wrapper.
     *
     * <p>
     * A list wrapper is a Map that has only one key and the value corresponding to that key is a list.  This is a
     * convention in the UpCloud API.
     * </p>
     *
     * @param value A map which to check.
     * @return <code>true</code> if <code>value</code> is a list wrapper, <code>false</code> otherwise.
     */
    private static boolean isListWrapper(final Map<String, ?> value) {
        final Set<String> keys = value.keySet()
        keys.size() == 1 && value[keys[0]] instanceof List
    }

    private static class ListWrapper {
        static Boolean isCase(final Object value) {
            value instanceof Map<String, ?> && value.size() == 1 && value.values()[0] instanceof List
        }
    }
}

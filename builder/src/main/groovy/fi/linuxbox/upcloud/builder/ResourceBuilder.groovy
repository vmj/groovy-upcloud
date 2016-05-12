package fi.linuxbox.upcloud.builder

import org.slf4j.*

import fi.linuxbox.upcloud.core.*

/**
 *
 */
class ResourceBuilder {
    private final Logger log = LoggerFactory.getLogger(ResourceBuilder)

    /**
     * Build a new server resource.
     * <p>
     *     ...
     * </p>
     * <code>
     *     def resource = server {
     *         hostname = 'server1.example.com'
     *         coreNumber = '1'
     *         memoryAmount = '2048'
     *     }
     * </code>
     *
     * @param closure A closure that will configure the new server.
     * @return The new server.
     */
    static def server(Closure closure = null) {
        build 'server', closure
    }

    def methodMissing(final String name, final def args) {
        switch (args.length) {
            case 0:
                return build(name)
            case 1:
                if (args[0] instanceof Closure)
                    return build(name, args[0])
        }
        throw new MissingMethodException(name, this.class, args)
    }

    /**
     * (Re)configure a resource.
     * <p>
     *     ...
     * </p>
     * <code>
     *     def resource = configure server, {
     *         hostname = 'server1.example.com'
     *         coreNumber = '1'
     *         memoryAmount = '2048'
     *     }
     * </code>
     *
     * @param resource The existing resource to (re)configure.
     * @param closure A closure that will (re)configure the resource.
     * @return The same resource, (re)configured.
     */
    static Resource configure(Resource resource, Closure closure = null) {
        if (closure) {
            closure.delegate = resource
            closure.call()
        }
        resource
    }

    /**
     * Build a new named resource.
     * <p>
     *     ...
     * </p>
     * <code>
     *     def resource = build 'server', {
     *         hostname = 'server1.example.com'
     *         coreNumber = '1'
     *         memoryAmount = '2048'
     *     }
     * </code>
     *
     * @param type Type of the resource to create (simple class name with first letter in lowercase)
     * @param closure A closure that will configure the new resource
     * @return The new resource.
     */
    static def build(String type, Closure closure = null) {
        Resource resource = new Resource([ repr: [ (type): [ :]]])."$type"
        configure resource, closure
    }
}

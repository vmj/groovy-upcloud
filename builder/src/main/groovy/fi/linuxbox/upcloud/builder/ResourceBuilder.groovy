package fi.linuxbox.upcloud.builder

import org.slf4j.*

import fi.linuxbox.upcloud.core.*

import static groovy.lang.Closure.DELEGATE_ONLY

/**
 *
 */
class ResourceBuilder {
    private final Logger log = LoggerFactory.getLogger(ResourceBuilder)

    /**
     * MOP method to allow custom Resource creation.
     * <p>
     *     This allows for following code:
     * </p>
     * <code>
     *     ResourceBuilder builder = new ResourceBuilder()
     *
     *     def dockerImage = builder.dockerImage {
     *         image = 'nginx:latest'
     *         state = 'running'
     *     }
     *     assert dockerImage.class.simpleName == 'DockerImage'
     *
     *     def rancherAgent = builder.RangerAgent()
     *     assert rancherAgent.class.simpleName == 'RancherAgent'
     * </code>
     * <p>
     *     This also supports passing the keyword arguments:
     * </p>
     * <code>
     *     ResourceBuilder builder = new ResourceBuilder()
     *
     *     def dockerImage = builder.dockerImage API: myApi, {
     *         ...
     *     }
     *
     *     def rancherAgent = builder.RangerAgent API: myApi
     * </code>
     * <p>
     *     If possible, use the static build method instead.
     * </p>
     *
     * @param name Simple class name of the resource, i.e. without the package.
     * @param args Optional keyword arguments and the optional configuration block.
     * @return The new resource.
     */
    def methodMissing(final String name, final def args) {
        switch (args.length) {
            case 0:
                return build(name)
            case 1:
                if (args[0] instanceof Closure)
                    return build(name, args[0])
                else if (args[0] instanceof Map)
                    return build(args[0], name)
            case 2:
                return build(args[0], name, args[1])
        }
        throw new MissingMethodException(name, this.class, args)
    }

    /**
     * (Re)configure a resource.
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
    static <R extends Resource> R configure(@DelegatesTo.Target R resource, @DelegatesTo(strategy = DELEGATE_ONLY) Closure<Void> closure = null) {
        if (closure) {
            Closure<Void> c = closure.rehydrate(resource, this, this)
            c.resolveStrategy = DELEGATE_ONLY
            c.call()
        }
        resource
    }

    /**
     * Build a new named resource.
     * <code>
     *     def resource = build 'Server', {
     *         hostname = 'server1.example.com'
     *         coreNumber = '1'
     *         memoryAmount = '2048'
     *     }
     * </code>
     * <p>
     *     This also supports passing the keyword arguments:
     * </p>
     * <code>
     *     def resource = build 'Server', API: myApi, {
     *         ...
     *     }
     * </code>
     *
     * @param type Type of the resource to create (simple class name, possibly with first letter in lowercase)
     * @param closure Optional keyword arguments and closure that will configure the new resource
     * @return The new resource.
     */
    static Resource build(Map kwargs, String type, Closure closure = null) {
        // Support both "IpAddress" and "ipAddress"
        type = type.replaceFirst('^([A-Z])') { it[0].toLowerCase() }

        Resource resource = new Resource((kwargs ?: [:]) + [ repr: [ (type): [ :]]])."$type"
        configure resource, closure
    }

    /**
     * Calls build with empty keyword arguments.
     *
     * @param type Type of the resource to create (simple class name, possibly with first letter in lowercase)
     * @param closure Optional closure that will configure the new resource
     * @return The new resource.
     */
    static Resource build(String type, Closure closure = null) {
        build([:], type, closure)
    }
}

package fi.linuxbox.upcloud.builder

import fi.linuxbox.upcloud.core.Resource
import fi.linuxbox.upcloud.core.ResourceLoader
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static groovy.lang.Closure.DELEGATE_FIRST

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
     *     def dockerImage = builder.dockerImage SESSION: mySession, {
     *         ...
     *     }
     *
     *     def rancherAgent = builder.RangerAgent SESSION: mySession
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
                switch (args[0]) {
                    case Closure:
                        return build(name, args[0])
                    case Map:
                        return build(args[0], name)
                }
                break
            case 2:
                return build(args[0], name, args[1])
        }
        throw new MissingMethodException(name, this.class, args)
    }

    /**
     * (Re)configure a resource.
     * <pre>
     *     def resource = configure server, {
     *         hostname = 'server1.example.com'
     *         coreNumber = '1'
     *         memoryAmount = '2048'
     *     }
     * </pre>
     *
     * @param resource The existing resource to (re)configure.
     * @param closure A closure that will (re)configure the resource.
     * @return The same resource, (re)configured.
     */
    static <R extends Resource> R configure(@DelegatesTo.Target("resource")
                                            R resource,
                                            @DelegatesTo(value = DelegatesTo.Target,
                                                         target = "resource",
                                                         strategy = DELEGATE_FIRST)
                                            @ClosureParams(FirstParam)
                                            Closure<Void> closure = null) {
        if (closure) {
            final Closure<Void> c = closure.clone()
            c.resolveStrategy = DELEGATE_FIRST
            c.delegate = resource
            c.call(resource)
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
     *     def resource = build 'Server', SESSION: mySession, {
     *         ...
     *     }
     * </code>
     *
     * @param resourceClassName Type of the resource to create (simple class name, possibly with first letter in lowercase)
     * @param closure Optional keyword arguments and closure that will configure the new resource
     * @return The new resource.
     */
    static Resource build(Map kwargs, String resourceClassName, Closure closure = null) {
        // Support both "IpAddress" and "ipAddress"
        resourceClassName = resourceClassName.replaceFirst('^([a-z])') { it[0].toUpperCase() }

        Resource resource = ResourceLoader.instantiateResourceClass(resourceClassName, kwargs)
        configure resource, closure
    }

    /**
     * Calls build with empty keyword arguments.
     *
     * @param resourceClassName Type of the resource to create (simple class name, possibly with first letter in lowercase)
     * @param closure Optional closure that will configure the new resource
     * @return The new resource.
     */
    static Resource build(String resourceClassName, Closure closure = null) {
        build([:], resourceClassName, closure)
    }
}

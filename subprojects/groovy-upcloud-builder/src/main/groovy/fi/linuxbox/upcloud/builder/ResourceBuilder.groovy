/*
 * Groovy UpCloud library - Builder Module
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
package fi.linuxbox.upcloud.builder

import fi.linuxbox.upcloud.core.Resource
import fi.linuxbox.upcloud.core.ResourceLoader
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

import static groovy.lang.Closure.DELEGATE_FIRST

/**
 *
 */
class ResourceBuilder {
    /**
     * MOP method to allow custom Resource creation.
     * <p>
     *     This allows for following code:
     * </p>
     * <pre><code class="groovy">
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
     * </code></pre>
     * <p>
     *     This also supports passing the keyword arguments:
     * </p>
     * <pre><code class="groovy">
     *     ResourceBuilder builder = new ResourceBuilder()
     *
     *     def dockerImage = builder.dockerImage HTTP: mySession, {
     *         ...
     *     }
     *
     *     def rancherAgent = builder.RangerAgent HTTP: mySession
     * </code></pre>
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
     * <pre><code class="groovy">
     *     def resource = configure server, {
     *         hostname = 'server1.example.com'
     *         coreNumber = '1'
     *         memoryAmount = '2048'
     *     }
     * </code></pre>
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
     * <pre><code class="groovy">
     *     def resource = build 'Server', {
     *         hostname = 'server1.example.com'
     *         coreNumber = '1'
     *         memoryAmount = '2048'
     *     }
     * </code></pre>
     * <p>
     *     This also supports passing the keyword arguments:
     * </p>
     * <pre><code class="groovy">
     *     def resource = build 'Server', HTTP: mySession, {
     *         ...
     *     }
     * </code></pre>
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

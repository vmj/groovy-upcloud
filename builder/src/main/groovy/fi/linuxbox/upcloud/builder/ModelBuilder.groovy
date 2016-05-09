package fi.linuxbox.upcloud.builder

import org.slf4j.*

import fi.linuxbox.upcloud.core.*

/**
 *
 */
class ModelBuilder {
    private final Logger log = LoggerFactory.getLogger(ModelBuilder)

    /**
     * Build a new server model.
     * <p>
     *     ...
     * </p>
     * <code>
     *     def model = server {
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
     * (Re)configure a model.
     * <p>
     *     ...
     * </p>
     * <code>
     *     def model = configure server, {
     *         hostname = 'server1.example.com'
     *         coreNumber = '1'
     *         memoryAmount = '2048'
     *     }
     * </code>
     *
     * @param model The existing model to (re)configure.
     * @param closure A closure that will (re)configure the model.
     * @return The same model, (re)configured.
     */
    static MODEL configure(MODEL model, Closure closure = null) {
        if (closure) {
            closure.delegate = model
            closure.call()
        }
        model
    }

    /**
     * Build a new named model.
     * <p>
     *     ...
     * </p>
     * <code>
     *     def model = build 'server', {
     *         hostname = 'server1.example.com'
     *         coreNumber = '1'
     *         memoryAmount = '2048'
     *     }
     * </code>
     *
     * @param type Type of the model to create (simple class name with first letter in lowercase)
     * @param closure A closure that will configure the new model
     * @return The new model.
     */
    static def build(String type, Closure closure = null) {
        MODEL model = new MODEL([repr: [(type): [:]]])."$type"
        configure model, closure
    }
}

package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.builder.*

import static groovy.lang.Closure.*

/**
 *
 */
class Builder extends ResourceBuilder {
    static Server server(@DelegatesTo(strategy = DELEGATE_ONLY, value = Server) Closure closure) {
        configure(new Server(), closure)
    }
}

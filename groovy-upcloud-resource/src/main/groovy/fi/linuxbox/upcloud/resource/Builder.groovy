package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.builder.*

import static groovy.lang.Closure.*
import static java.util.Collections.EMPTY_MAP

/**
 * Builder for concrete UpCloud resources.
 * <p>
 *     This class is basically just syntactic sugar for the more generic {@link ResourceBuilder}.
 * </p>
 */
class Builder extends ResourceBuilder {
    static Account account(Map kwargs, @DelegatesTo(strategy = DELEGATE_ONLY, value = Account) Closure closure = null) {
        configure(new Account(kwargs), closure)
    }

    static Account account(@DelegatesTo(strategy = DELEGATE_ONLY, value = Account) Closure closure = null) {
        account(EMPTY_MAP, closure)
    }

    static Server server(Map kwargs, @DelegatesTo(strategy = DELEGATE_ONLY, value = Server) Closure closure = null) {
        configure(new Server(kwargs), closure)
    }

    static Server server(@DelegatesTo(strategy = DELEGATE_ONLY, value = Server) Closure closure = null) {
        server(EMPTY_MAP, closure)
    }
}

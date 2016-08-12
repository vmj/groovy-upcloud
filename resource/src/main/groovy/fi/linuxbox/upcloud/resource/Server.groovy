package fi.linuxbox.upcloud.resource

import groovy.transform.InheritConstructors

import fi.linuxbox.upcloud.core.*

/**
 *
 */
@InheritConstructors
class Server extends Resource {
    String hostname
    String coreNumber
    String memoryAmount
}

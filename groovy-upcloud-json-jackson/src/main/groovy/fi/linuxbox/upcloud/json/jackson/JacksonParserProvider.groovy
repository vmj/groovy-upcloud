package fi.linuxbox.upcloud.json.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic

import javax.inject.Provider

/**
 *
 */
@CompileStatic
class JacksonParserProvider implements Provider<ObjectMapper> {
    @Override
    ObjectMapper get() {
        new ObjectMapper()
    }
}

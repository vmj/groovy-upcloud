package fi.linuxbox.upcloud.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import javax.inject.Provider
import org.slf4j.*

/**
 *
 */
class JacksonParserProvider implements Provider<ObjectMapper> {
    private final Logger log = LoggerFactory.getLogger(JacksonParserProvider)

    @Override
    ObjectMapper get() {
        new ObjectMapper()
    }
}

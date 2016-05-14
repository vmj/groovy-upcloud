package fi.linuxbox.upcloud.json.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import fi.linuxbox.upcloud.core.json.JSON
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject

/**
 *
 */
class JacksonJSON implements JSON {
    private final Logger log = LoggerFactory.getLogger(JacksonJSON)

    private final ObjectMapper mapper

    @Inject
    JacksonJSON(final ObjectMapper mapper) {
        this.mapper = mapper
    }

    @Override
    Map<String, Object> decode(final InputStream data) {
        mapper.readValue(data, Map)
    }

    @Override
    InputStream encode(final Map<String, Object> repr) {
        new ByteArrayInputStream(mapper.writeValueAsBytes(repr))
    }
}

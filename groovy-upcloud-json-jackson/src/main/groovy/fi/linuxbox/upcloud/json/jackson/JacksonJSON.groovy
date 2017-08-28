package fi.linuxbox.upcloud.json.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import fi.linuxbox.upcloud.json.spi.JSON
import groovy.transform.CompileStatic

import javax.inject.Inject

/**
 *
 */
@CompileStatic
class JacksonJSON implements JSON {
    private final ObjectMapper mapper

    @Inject
    JacksonJSON(final ObjectMapper mapper) {
        this.mapper = mapper
    }

    JacksonJSON() {
        this(new JacksonParserProvider().get())
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

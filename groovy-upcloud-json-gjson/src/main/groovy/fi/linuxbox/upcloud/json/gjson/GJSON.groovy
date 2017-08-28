package fi.linuxbox.upcloud.json.gjson

import fi.linuxbox.upcloud.json.spi.JSON
import groovy.json.JsonOutput
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

import java.nio.charset.Charset

/**
 *
 */
@CompileStatic
class GJSON implements JSON {
    private static final Charset UTF_8 = Charset.forName("UTF-8")

    private final JsonSlurper parser = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)

    @Override
    Map<String, Object> decode(final InputStream data) {
        (Map<String, Object>) parser.parse(data)
    }

    @Override
    InputStream encode(final Map<String, Object> repr) {
        new ByteArrayInputStream(JsonOutput.toJson(repr).getBytes(UTF_8))
    }
}

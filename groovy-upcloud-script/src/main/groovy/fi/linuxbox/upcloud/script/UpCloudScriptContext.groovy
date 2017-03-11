package fi.linuxbox.upcloud.script

import fi.linuxbox.upcloud.http.spi.HTTP
import fi.linuxbox.upcloud.json.spi.JSON

import javax.inject.*
import org.slf4j.*

import fi.linuxbox.upcloud.api.*
import fi.linuxbox.upcloud.builder.*
import fi.linuxbox.upcloud.core.*
import fi.linuxbox.upcloud.http.ahc.*
import fi.linuxbox.upcloud.json.jackson.*

/**
 * A simple environment for scripts.
 */
class UpCloudScriptContext implements Closeable {
    private final Logger log = LoggerFactory.getLogger(UpCloudScriptContext)

    final HTTP http
    final JSON json

    final API api

    final UpCloud upCloud

    final ResourceBuilder builder

    UpCloudScriptContext(@Named("username") String username, @Named("password") String password) {
        http = new AhcHTTP(new AhcClientProvider().get())
        json = new JacksonJSON(new JacksonParserProvider().get())

        api = new API(http, json, username, password)

        upCloud = new UpCloud(api)

        builder = new ResourceBuilder()
    }

    @Override
    void close() {
        http.close()
    }
}

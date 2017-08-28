package fi.linuxbox.upcloud.script

import fi.linuxbox.upcloud.api.UpCloud
import fi.linuxbox.upcloud.builder.ResourceBuilder
import fi.linuxbox.upcloud.core.Session
import fi.linuxbox.upcloud.http.spi.HTTP
import fi.linuxbox.upcloud.json.spi.JSON

import javax.inject.Named

/**
 * A simple environment for scripts.
 */
class UpCloudScriptContext implements Closeable {

    final HTTP http
    final JSON json

    final Session session

    final UpCloud upCloud

    final ResourceBuilder builder

    UpCloudScriptContext(@Named("username") String username, @Named("password") String password) {
        http = HTTPFactory.create()
        json = JSONFactory.create()

        session = new Session(http, json, username, password)

        upCloud = new UpCloud(session)

        builder = new ResourceBuilder()
    }

    @Override
    void close() {
        http.close()
    }
}

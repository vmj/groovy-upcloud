package fi.linuxbox.upcloud.http.ahc

import javax.inject.*
import org.apache.http.impl.nio.client.*
import org.slf4j.*
/**
 *
 */
class AhcClientProvider implements Provider<CloseableHttpAsyncClient> {
    private final Logger log = LoggerFactory.getLogger(AhcClientProvider)

    @Override
    CloseableHttpAsyncClient get() {
        HttpAsyncClients.createMinimal()
    }
}

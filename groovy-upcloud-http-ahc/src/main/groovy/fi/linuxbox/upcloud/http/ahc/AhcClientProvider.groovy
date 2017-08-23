package fi.linuxbox.upcloud.http.ahc

import groovy.transform.CompileStatic
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClients

import javax.inject.Provider

/**
 *
 */
@CompileStatic
class AhcClientProvider implements Provider<CloseableHttpAsyncClient> {

    @Override
    CloseableHttpAsyncClient get() {
        HttpAsyncClients.createMinimal()
    }
}

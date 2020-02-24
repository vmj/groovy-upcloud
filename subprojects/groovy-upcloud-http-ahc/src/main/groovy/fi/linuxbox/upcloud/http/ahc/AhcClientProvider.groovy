/*
 * Groovy UpCloud library - HTTP AHC Module
 * Copyright (C) 2018  Mikko Värri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fi.linuxbox.upcloud.http.ahc

import groovy.transform.CompileStatic
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClients
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.apache.http.nio.conn.NHttpClientConnectionManager
import org.apache.http.nio.reactor.ConnectingIOReactor

import javax.inject.Provider
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong

/**
 */
@CompileStatic
class AhcClientProvider implements Provider<CloseableHttpAsyncClient> {

    @Override
    CloseableHttpAsyncClient get() {
        final cpuCount = Runtime.runtime.availableProcessors()

        // On quad core or lesser, use 4 IO threads
        // On better, use 8 IO threads.
        // More than 8 is probably abusive from server point of view.
        final ioThreadCount = cpuCount < 5 ? 4 : 8

        // The aim is to mimic HttpAsyncClients.createMinimal().

        // But with a custom thread factory for more specific thread names.
        final ThreadFactory tf = new UpCloudThreadFactory()

        // And with default IO config, but with a selected number of IO threads.
        // Default would use cpuCount IO threads.
        final IOReactorConfig cf = IOReactorConfig.custom().setIoThreadCount(ioThreadCount).build()
        final ConnectingIOReactor io = new DefaultConnectingIOReactor(cf, tf)

        // The default connection manager is OK
        final NHttpClientConnectionManager nHttpClientConnectionManager = new PoolingNHttpClientConnectionManager(io)
        // ... except that it defaults to using only 2 IO threads per route.
        // In our use case, rest of the IO threads would always be idle.
        nHttpClientConnectionManager.setDefaultMaxPerRoute(ioThreadCount)


        HttpAsyncClients.createMinimal(nHttpClientConnectionManager)
    }

    /**
     * Otherwise functional equivalent of the one used by minimal client, but with more descriptive thread names.
     */
    private static class UpCloudThreadFactory implements ThreadFactory {
        private final static AtomicLong COUNT = new AtomicLong(1)

        @Override
        Thread newThread(final Runnable r) {
            return new Thread(r, "upcloud-http-" + COUNT.getAndIncrement())
        }
    }
}

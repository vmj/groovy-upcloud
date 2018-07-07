/*
 * Groovy UpCloud library - Script Module
 * Copyright (C) 2018  <mikko@varri.fi>
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
package fi.linuxbox.upcloud.script

import fi.linuxbox.upcloud.http.spi.CompletionCallback
import fi.linuxbox.upcloud.http.spi.HTTP
import fi.linuxbox.upcloud.http.spi.META
import fi.linuxbox.upcloud.http.spi.Request
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.TimeUnit

@CompileStatic
@Slf4j
class HTTPImpl implements HTTP {
    private final ExecutorService ioThreads = Executors.newFixedThreadPool(4)
    private final Random random = new Random()

    @Override
    String getUserAgent() {
        ""
    }

    @Override
    void execute(Request request, InputStream body, CompletionCallback cb) {
        try {
            ioThreads.submit {
                // In reality, we would fire an HTTP request here, and
                // release this IO thread for other work.  We would not
                // block here.
                try {
                    Thread.sleep(2500 + random.nextInt(3500)) // 2.5 - 6 secs
                } catch (InterruptedException e) {
                    log.info("Request interrupted; IO thread pool is shutting down", e)
                    Thread.currentThread().interrupt()
                    return
                }

                // This is what we would get from the wire.
                META meta = new META(status: 200)

                // Pass it to whoever is listening for this HTTP transaction
                try {
                    cb.completed(meta, null, null)
                } catch (final InterruptedException e) {
                    log.info("Response interrupted; script is shutting down", e)
                    Thread.currentThread().interrupt()
                }
            }
        } catch (RejectedExecutionException e) {
            log.debug("Request rejected; IO thread pool is shutting down", e)
        }
    }

    @Override
    void close() throws IOException {
        log.debug("Shutting down IO thread pool")
        ioThreads.shutdownNow()
        try {
            // Wait a while for existing tasks to terminate
            log.debug("Awaiting termination of IO thread pool")
            if (!ioThreads.awaitTermination(60, TimeUnit.SECONDS)) {
                log.debug("Attempting to interrupt IO threads in the pool")
                ioThreads.shutdownNow() // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                log.debug("Awaiting termination of IO thread pool")
                if (!ioThreads.awaitTermination(60, TimeUnit.SECONDS))
                    log.error("IO thread pool did not terminate")
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            log.debug("Interrupted; Attempting to interrupt IO threads in the pool", ie)
            ioThreads.shutdownNow()
            // Preserve interrupt status
            log.debug("Preserving the interrupt status of script thread")
            Thread.currentThread().interrupt()
        }
    }
}

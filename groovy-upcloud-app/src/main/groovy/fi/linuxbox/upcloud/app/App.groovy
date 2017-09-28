/*
 * Groovy UpCloud library - an example application
 * Copyright (C) 2017  <mikko@varri.fi>
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
package fi.linuxbox.upcloud.app

import java.util.concurrent.*
import org.slf4j.*

import fi.linuxbox.upcloud.script.*

import static java.util.concurrent.TimeUnit.*

/**
 *
 */
class App implements Closeable {
    private final Logger log = LoggerFactory.getLogger(App)

    private CountDownLatch cv = null
    private UpCloudScriptContext ctx = null
    private boolean running = false

    public static void main(String[] args) {
        new App(args).run().close()
    }

    App(args) {
        if (args?.length != 2) {
            log.error("USAGE: app username password")
            return
        }
        cv = new CountDownLatch(1)
        ctx = new UpCloudScriptContext(args[0], args[1])
        running = true
    }

    def run(String[] args) {
        if (!running) {
            log.error("Not running")
            return this
        }
        log.info("Starting...")

        ctx.upCloud.account(
                200: { response ->
                    cv.countDown()
                    log.info("${response.account.username}: ${response.account.credits}")
                },
                { response, err ->
                    cv.countDown()
                    if (err) {
                        log.error("got a network error: ${err.message}")
                        if (err.cause)
                            err.cause.printStackTrace()
                    }
                    else {
                        log.error("got a non-OK response: ${response.META.status} ${response.META.message}")
                    }
                })

        log.info("Waiting...")
        cv.await(30, SECONDS)

        this
    }

    @Override
    void close() throws IOException {
        if (running) {
            log.info("Closing...")
            ctx.close()
            running = false
        }
    }

    boolean isRunning() {
        return running
    }
}

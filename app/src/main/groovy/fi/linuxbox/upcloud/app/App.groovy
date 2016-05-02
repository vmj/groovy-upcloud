package fi.linuxbox.upcloud.app

import fi.linuxbox.upcloud.*
import java.util.concurrent.*
import org.slf4j.*

import fi.linuxbox.upcloud.core.*
import fi.linuxbox.upcloud.http.ahc.*
import fi.linuxbox.upcloud.jackson.*

import static java.util.concurrent.TimeUnit.*

/**
 *
 */
class App {
    private final Logger log = LoggerFactory.getLogger(App)

    public static void main(String[] args) {
        new App().run(args)
    }

    def run(String[] args) {
        if (args.length != 2) {
            log.error("USAGE: app username password")
            return
        }
        log.info("Starting...")

        def cv = new CountDownLatch(1)

        def http = new AhcHTTP(new AhcClientProvider().get())
        def json = new JacksonJSON(new JacksonParserProvider().get())

        def api = new API(http, json, args[0], args[1])

        def cloud = new UpCloud(api)

        cloud.account(
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

        log.info("Closing...")
        http.close()
    }
}

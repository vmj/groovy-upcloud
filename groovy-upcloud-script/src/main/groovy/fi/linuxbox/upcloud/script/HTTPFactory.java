package fi.linuxbox.upcloud.script;

import fi.linuxbox.upcloud.http.spi.HTTP;

import java.util.ServiceLoader;

public class HTTPFactory {
    public static HTTP create() {
        final ServiceLoader<HTTP> httpServiceLoader = ServiceLoader.load(HTTP.class);
        for (final HTTP http : httpServiceLoader) {
            if (http != null) {
                return http;
            }
        }
        return null;
    }
}

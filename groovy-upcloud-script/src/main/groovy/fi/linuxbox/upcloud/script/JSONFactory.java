package fi.linuxbox.upcloud.script;

import fi.linuxbox.upcloud.json.spi.JSON;

import java.util.ServiceLoader;

public class JSONFactory {
    public static JSON create() {
        for (final JSON json : ServiceLoader.load(JSON.class)) {
            if (json != null) {
                return json;
            }
        }
        return null;
    }
}

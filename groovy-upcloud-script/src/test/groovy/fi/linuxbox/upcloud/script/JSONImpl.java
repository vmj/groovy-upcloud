package fi.linuxbox.upcloud.script;

import fi.linuxbox.upcloud.json.spi.JSON;

import java.io.InputStream;
import java.util.Map;

public class JSONImpl implements JSON {
    @Override
    public Map<String, Object> decode(final InputStream data) {
        return null;
    }

    @Override
    public InputStream encode(final Map<String, Object> repr) {
        return null;
    }
}

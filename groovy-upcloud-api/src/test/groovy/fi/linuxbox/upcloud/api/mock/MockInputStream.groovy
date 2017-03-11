package fi.linuxbox.upcloud.api.mock

/**
 *
 */
class MockInputStream extends InputStream {
    final public Map<String, Object> repr

    MockInputStream(final Map<String, Object> repr) {
        this.repr = repr
    }

    @Override
    int read() throws IOException {
        return 0
    }
}

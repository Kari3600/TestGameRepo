package com.Kari3600.me.TestGameClient.util;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class TextureInputStream extends InputStream {
    private final ByteBuffer buffer;

    public TextureInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() {
        if (!buffer.hasRemaining()) {
            return -1;  // End of stream
        }
        return buffer.get() & 0xFF;  // Convert to unsigned byte
    }

    @Override
    public int read(byte[] bytes, int off, int len) {
        if (!buffer.hasRemaining()) {
            return -1;  // End of stream
        }

        len = Math.min(len, buffer.remaining());
        buffer.get(bytes, off, len);
        return len;
    }

    @Override
    public int available() {
        return buffer.remaining();
    }
}

package dtm.request_actions.http.simple.implementation;

import dtm.request_actions.http.simple.core.StreamReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HttpResultStreamReader implements StreamReader {

    private final InputStream input;
    private final ByteArrayOutputStream buffer;
    private int pos = 0;
    private boolean finished = false;

    public HttpResultStreamReader(InputStream inputStream) {
        this.input = inputStream;
        this.buffer = new ByteArrayOutputStream();
    }

    @Override
    public int getInt() {
        ensureAvailable(4);
        byte[] bytes = readBytes(4);
        return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    @Override
    public long getLong() {
        ensureAvailable(8);
        byte[] bytes = readBytes(8);
        return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getLong();
    }

    @Override
    public byte[] readBytes(int size) {
        ensureAvailable(size);
        byte[] all = buffer.toByteArray();
        int actualSize = Math.min(size, all.length - pos);
        byte[] result = new byte[actualSize];
        System.arraycopy(all, pos, result, 0, actualSize);
        pos += actualSize;
        return result;
    }

    @Override
    public byte[] readAllBytes() {
        try {
            byte[] remaining = input.readAllBytes();
            buffer.write(remaining);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] all = buffer.toByteArray();
        pos = all.length;
        return all;
    }

    @Override
    public byte[] getReadBytes() {
        byte[] all = buffer.toByteArray();
        if (pos == 0) return new byte[0];
        byte[] read = new byte[pos];
        System.arraycopy(all, 0, read, 0, pos);
        return read;
    }

    public byte[] readOrGetAllBytes() {
        if (isFinished()) {
            return getReadBytes();
        } else {
            return readAllBytes();
        }
    }


    @Override
    public int position() {
        return pos;
    }

    @Override
    public void position(int newPosition) {
        if (newPosition < 0 || newPosition > buffer.size()) throw new IllegalArgumentException();
        pos = newPosition;
    }

    @Override
    public void reset() {
        pos = 0;
    }

    @Override
    public void skip(int bytes) {
        position(pos + bytes);
    }

    @Override
    public boolean isFinished() {
        return finished && pos >= buffer.size();
    }

    private void ensureAvailable(int size) {
        try {
            while (buffer.size() - pos < size) {
                int b = input.read();
                if (b == -1) {
                    finished = true;
                    break;
                }
                buffer.write(b);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        input.close();
    }
}

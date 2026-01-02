package dtm.request_actions.http.simple.core;

public interface StreamReader extends AutoCloseable {
    int getInt();
    long getLong();

    byte[] getReadBytes();
    byte[] readBytes(int size);
    byte[] readAllBytes();
    byte[] readOrGetAllBytes();

    int position();
    void position(int newPosition);

    void reset();
    void skip(int bytes);

    int availableBytes();
    boolean isFinished();
}

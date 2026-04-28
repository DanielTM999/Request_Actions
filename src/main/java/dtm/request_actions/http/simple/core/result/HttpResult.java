package dtm.request_actions.http.simple.core.result;

import dtm.request_actions.http.simple.core.StreamReader;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public interface HttpResult {
    int getStatusCode();
    HttpHeaderResult getHeader();
    Optional<String> getBody();
    Optional<String> ifErrorGet();
    StreamReader getStreamReader();

    default Optional<byte[]> getBodyBytes() {
        return getBody().map(body -> body.getBytes(StandardCharsets.UTF_8));
    }

    default Optional<byte[]> ifErrorGetBytes() {
        return ifErrorGet().map(body -> body.getBytes(StandardCharsets.UTF_8));
    }

    default boolean isRequestSucess() {
        int statusCode = getStatusCode();
        return (statusCode >= 200 && statusCode < 300);
    }

}

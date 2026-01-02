package dtm.request_actions.http.simple.core.result;

import dtm.request_actions.http.simple.core.StreamReader;

import java.util.Optional;

public interface HttpResult {
    int getStatusCode();
    HttpHeaderResult getHeader();
    Optional<String> getBody();
    Optional<String> ifErrorGet();
    StreamReader getStreamReader();
}

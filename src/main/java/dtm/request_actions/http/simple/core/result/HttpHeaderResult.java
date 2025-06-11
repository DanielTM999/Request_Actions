package dtm.request_actions.http.simple.core.result;

import java.util.Optional;

public abstract class HttpHeaderResult {
    public abstract Optional<String> getHeaderValue(String key);
}

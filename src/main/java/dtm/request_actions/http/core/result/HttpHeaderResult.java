package dtm.request_actions.http.core.result;

import java.util.Optional;

public abstract class HttpHeaderResult {
    public abstract Optional<String> getHeaderValue(String key);
}

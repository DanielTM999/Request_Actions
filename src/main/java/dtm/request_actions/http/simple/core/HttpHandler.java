package dtm.request_actions.http.simple.core;

import java.net.http.HttpResponse;

public interface HttpHandler {
    void onResult(HttpResponse<String> responsebase);
}

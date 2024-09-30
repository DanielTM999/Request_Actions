package dtm.request_actions.http.core;

import java.net.http.HttpResponse;

public interface HttpHandler {
    void onResult(HttpResponse<String> responsebase);
}

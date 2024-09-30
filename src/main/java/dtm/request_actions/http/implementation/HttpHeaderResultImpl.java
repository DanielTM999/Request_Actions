package dtm.request_actions.http.implementation;

import java.net.http.HttpResponse;
import java.util.Optional;

import dtm.request_actions.http.core.result.HttpHeaderResult;

public class HttpHeaderResultImpl extends HttpHeaderResult{

    private HttpResponse<String> baseResponse;
    
    HttpHeaderResultImpl(HttpResponse<String> baseResponse){
        this.baseResponse = baseResponse;
    }

    @Override
    public Optional<String> getHeaderValue(String key) {
        try {
            return baseResponse.headers().firstValue(key);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
}
package dtm.request_actions.http.simple.implementation;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Optional;

import dtm.request_actions.http.simple.core.result.HttpHeaderResult;

public class HttpHeaderResultImpl extends HttpHeaderResult{

    private final HttpResponse<InputStream> baseResponse;
    
    HttpHeaderResultImpl(HttpResponse<InputStream> baseResponse){
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
package dtm.request_actions.http.core;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;

import dtm.request_actions.http.core.result.HttpRequestResult;

public interface HttpActionGetAsync {
    <T> Future<HttpRequestResult<T>> getAsync(String url);
    <T> Future<HttpRequestResult<T>> getAsync(String url,  String... urlParams);
    <T> Future<HttpRequestResult<T>> getAsync(URI url);
    <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header);
    <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header,  String... urlParams);
    <T> Future<HttpRequestResult<T>> getAsync(URI url, Map<String, String> header);
}

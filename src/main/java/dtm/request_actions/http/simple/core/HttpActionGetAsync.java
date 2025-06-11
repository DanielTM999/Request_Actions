package dtm.request_actions.http.simple.core;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import dtm.request_actions.http.simple.core.config.RequestConfiguration;
import dtm.request_actions.http.simple.core.result.HttpRequestResult;

public interface HttpActionGetAsync extends HttpConfigurer {
    <T> Future<HttpRequestResult<T>> getAsync(String url);
    <T> Future<HttpRequestResult<T>> getAsync(String url, Consumer<RequestConfiguration> configuration);

    <T> Future<HttpRequestResult<T>> getAsync(String url,  String... urlParams);
    <T> Future<HttpRequestResult<T>> getAsync(String url,  Consumer<RequestConfiguration> configuration, String... urlParams);

    <T> Future<HttpRequestResult<T>> getAsync(URI url);
    <T> Future<HttpRequestResult<T>> getAsync(URI url, Consumer<RequestConfiguration> configuration);

    <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header);
    <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header, Consumer<RequestConfiguration> configuration);

    <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header, String... urlParams);
    <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header, Consumer<RequestConfiguration> configuration, String... urlParams);

    <T> Future<HttpRequestResult<T>> getAsync(URI url, Map<String, String> header);
    <T> Future<HttpRequestResult<T>> getAsync(URI url, Map<String, String> header, Consumer<RequestConfiguration> configuration);
}

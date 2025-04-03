package dtm.request_actions.http.core;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;
import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.http.core.result.HttpRequestResult;

public interface HttpActionDeleteAsync extends HttpConfigurer{
    <T> Future<HttpRequestResult<T>> deleteAsync(String url) throws HttpException;
    <T> Future<HttpRequestResult<T>> deleteAsync(String url, String... urlParams) throws HttpException;
    <T> Future<HttpRequestResult<T>> deleteAsync(URI url) throws HttpException;
    <T> Future<HttpRequestResult<T>> deleteAsync(String url, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> deleteAsync(String url, Map<String, String> header, String... urlParams) throws HttpException;
    <T> Future<HttpRequestResult<T>> deleteAsync(URI url, Map<String, String> header) throws HttpException;
}

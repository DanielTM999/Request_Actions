package dtm.request_actions.http.core;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;
import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.http.core.result.HttpRequestResult;

public interface HttpActionPostAsync extends HttpConfigurer{
    <T> Future<HttpRequestResult<T>> postAsync(String url, String body) throws HttpException;
    <T> Future<HttpRequestResult<T>> postAsync(String url, Object body) throws HttpException;
    <T> Future<HttpRequestResult<T>> postAsync(URI url, String body) throws HttpException;
    <T> Future<HttpRequestResult<T>> postAsync(URI url, Object body) throws HttpException;
    <T> Future<HttpRequestResult<T>> postAsync(String url, String body, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> postAsync(String url, Object body, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> postAsync(URI url, String body, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> postAsync(URI url, Object body, Map<String, String> header) throws HttpException;
}

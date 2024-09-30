package dtm.request_actions.http.core;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;

import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.http.core.result.HttpRequestResult;

public interface HttpActionPutAsync {
    <T> Future<HttpRequestResult<T>> putAsync(String url) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(String url, String body) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(String url, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(String url, Object body) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url, String body) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(String url, String body, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(String url, Object body, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url, String body, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body, Map<String, String> header) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(String url, String body, String... urlParams) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(String url, Object body, String... urlParams) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url, String body, String... urlParams) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body, String... urlParams) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(String url, String body, Map<String, String> header, String... urlParams) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(String url, Object body, Map<String, String> header, String... urlParams) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url, String body, Map<String, String> header, String... urlParams) throws HttpException;
    <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body, Map<String, String> header, String... urlParams) throws HttpException;
}

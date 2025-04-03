package dtm.request_actions.http.core;

import java.net.URI;
import java.util.Map;
import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.http.core.result.HttpRequestResult;

public interface HttpActionPut extends HttpConfigurer{
    <T> HttpRequestResult<T> put(String url) throws HttpException;
    <T> HttpRequestResult<T> put(URI url) throws HttpException;
    <T> HttpRequestResult<T> put(URI url, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> put(String url, String body) throws HttpException;
    <T> HttpRequestResult<T> put(String url, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> put(String url, Object body) throws HttpException;
    <T> HttpRequestResult<T> put(URI url, String body) throws HttpException;
    <T> HttpRequestResult<T> put(URI url, Object body) throws HttpException;
    <T> HttpRequestResult<T> put(String url, String body, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> put(String url, Object body, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> put(URI url, String body, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> put(URI url, Object body, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> put(String url, String body, String... urlParams) throws HttpException;
    <T> HttpRequestResult<T> put(String url, Object body, String... urlParams) throws HttpException;
    <T> HttpRequestResult<T> put(URI url, String body, String... urlParams) throws HttpException;
    <T> HttpRequestResult<T> put(URI url, Object body, String... urlParams) throws HttpException;
    <T> HttpRequestResult<T> put(String url, String body, Map<String, String> header, String... urlParams) throws HttpException;
    <T> HttpRequestResult<T> put(String url, Object body, Map<String, String> header, String... urlParams) throws HttpException;
    <T> HttpRequestResult<T> put(URI url, String body, Map<String, String> header, String... urlParams) throws HttpException;
    <T> HttpRequestResult<T> put(URI url, Object body, Map<String, String> header, String... urlParams) throws HttpException;
}

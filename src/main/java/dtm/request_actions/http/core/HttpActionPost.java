package dtm.request_actions.http.core;

import java.net.URI;
import java.util.Map;
import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.http.core.result.HttpRequestResult;

public interface HttpActionPost {
    <T> HttpRequestResult<T> post(String url, String body) throws HttpException;
    <T> HttpRequestResult<T> post(String url, Object body) throws HttpException;
    <T> HttpRequestResult<T> post(URI url, String body) throws HttpException;
    <T> HttpRequestResult<T> post(URI url, Object body) throws HttpException;
    <T> HttpRequestResult<T> post(String url, String body, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> post(String url, Object body, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> post(URI url, String body, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> post(URI url, Object body, Map<String, String> header) throws HttpException;
}

package dtm.request_actions.http.core;

import java.net.URI;
import java.util.Map;
import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.http.core.result.HttpRequestResult;

public interface HttpActionDelete {
    <T> HttpRequestResult<T> delete(String url) throws HttpException;
    <T> HttpRequestResult<T> delete(String url, String... urlParams) throws HttpException;
    <T> HttpRequestResult<T> delete(URI url) throws HttpException;
    <T> HttpRequestResult<T> delete(String url, Map<String, String> header) throws HttpException;
    <T> HttpRequestResult<T> delete(String url, Map<String, String> header, String... urlParams) throws HttpException;
    <T> HttpRequestResult<T> delete(URI url, Map<String, String> header) throws HttpException;
}

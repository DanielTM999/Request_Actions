package dtm.request_actions.http.implementation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.exceptions.HttpRuntimeException;
import dtm.request_actions.http.core.HttpAction;
import dtm.request_actions.http.core.HttpHandler;
import dtm.request_actions.http.core.mapper.HttpMapper;
import dtm.request_actions.http.core.result.HttpRequestResult;

public class HttpActionImpl implements HttpAction{

    private HttpClient client;
    private HttpMapper httpMapper;
    private List<HttpHandler> httpHandlers;

    public HttpActionImpl() {
        init(null, new DefaultHttpMapper());
    }

    public HttpActionImpl(HttpClient httpClient) {
        init(httpClient, new DefaultHttpMapper());
    }

    public HttpActionImpl(HttpMapper httpMapper) {
        init(null, httpMapper);
    }

    public HttpActionImpl(HttpClient httpClient, HttpMapper httpMapper) {
        init(httpClient, httpMapper);
    }

    public HttpActionImpl(HttpHandler handler) {
        init(null, new DefaultHttpMapper());
        httpHandlers.add(handler);
    }

    public HttpActionImpl(List<HttpHandler> handlers) {
        init(null, new DefaultHttpMapper());
        httpHandlers = handlers;
    }

    public HttpActionImpl(HttpMapper httpMapper, HttpHandler handler) {
        init(null, httpMapper);
        httpHandlers.add(handler);
    }

    public HttpActionImpl(HttpMapper httpMapper, List<HttpHandler> handlers) {
        init(null, httpMapper);
        httpHandlers = handlers;
    }

    @Override
    public void addHandler(HttpHandler handler) {
        this.httpHandlers.add(handler);
    }

    //GET
    @Override
    public <T> HttpRequestResult<T> get(String url) throws HttpException {
        return sendGetRequest(URI.create(url), null);
    }

    @Override
    public <T> HttpRequestResult<T> get(String url, String... urlParams) throws HttpException {
        return sendGetRequest(URI.create(urlConvert(url, urlParams)), null);
    }

    @Override
    public <T> HttpRequestResult<T> get(URI url) throws HttpException {
        return sendGetRequest(url, null);
    }

    @Override
    public <T> HttpRequestResult<T> get(String url, Map<String, String> header) throws HttpException {
        return sendGetRequest(URI.create(url), header);
    }

    @Override
    public <T> HttpRequestResult<T> get(String url, Map<String, String> header, String... urlParams) throws HttpException {
        return sendGetRequest(URI.create(urlConvert(url, urlParams)), header);
    }

    @Override
    public <T> HttpRequestResult<T> get(URI url, Map<String, String> header) throws HttpException {
        return sendGetRequest(url, header);
    }
    

    //Async GET
    @Override
    public <T> Future<HttpRequestResult<T>> getAsync(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> getAsync(String url, String... urlParams)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(urlConvert(url, urlParams));
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> getAsync(URI url)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header, String... urlParams){
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(urlConvert(url, urlParams), header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> getAsync(URI url, Map<String, String> header)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }
    
    

    //POST
    @Override
    public <T> HttpRequestResult<T> post(String url, String body) throws HttpException {
        return sendPostRequest(URI.create(url), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> post(String url, Object body) throws HttpException {
        return sendPostRequest(URI.create(url), httpMapper.mapperToJson(body), null);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, String body) throws HttpException {
        return sendPostRequest(url, body, null);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, Object body) throws HttpException {
        return sendPostRequest(url, httpMapper.mapperToJson(body), null);
    }

    @Override
    public <T> HttpRequestResult<T> post(String url, String body, Map<String, String> header) throws HttpException {
        return sendPostRequest(URI.create(url), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> post(String url, Object body, Map<String, String> header) throws HttpException {
        return sendPostRequest(URI.create(url), httpMapper.mapperToJson(body), header);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, String body, Map<String, String> header) throws HttpException {
        return sendPostRequest(url, body, header);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, Object body, Map<String, String> header) throws HttpException {
        return sendPostRequest(url, httpMapper.mapperToJson(body), header);
    }


    //Async POST
    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(String url, String body) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(String url, Object body) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(URI url, String body) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(URI url, Object body) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(String url, String body, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(String url, Object body, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(URI url, String body, Map<String, String> header)throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(URI url, Object body, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    
    //DELETE
    @Override
    public <T> HttpRequestResult<T> delete(String url) throws HttpException {
        return sendDeleteRequest(URI.create(url), null);
    }

    @Override
    public <T> HttpRequestResult<T> delete(String url, String... urlParams) throws HttpException {
        return sendDeleteRequest(URI.create(urlConvert(url, urlParams)), null);
    }

    @Override
    public <T> HttpRequestResult<T> delete(URI url) throws HttpException {
        return sendDeleteRequest(url, null);
    }

    @Override
    public <T> HttpRequestResult<T> delete(String url, Map<String, String> header) throws HttpException {
        return sendDeleteRequest(URI.create(url), header);
    }

    @Override
    public <T> HttpRequestResult<T> delete(String url, Map<String, String> header, String... urlParams) throws HttpException {
        return sendDeleteRequest(URI.create(urlConvert(url, urlParams)), header);
    }

    @Override
    public <T> HttpRequestResult<T> delete(URI url, Map<String, String> header) throws HttpException {
        return sendDeleteRequest(url, header);
    }    
    

    //Async DELETE
    @Override
    public <T> Future<HttpRequestResult<T>> deleteAsync(String url) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return delete(url);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> deleteAsync(String url, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return delete(url, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> deleteAsync(URI url) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return delete(url);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> deleteAsync(String url, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return delete(url, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> deleteAsync(String url, Map<String, String> header, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return delete(url, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> deleteAsync(URI url, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return delete(url, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }
    

    //PUT
    @Override
    public <T> HttpRequestResult<T> put(String url) throws HttpException {
        return sendPutRequest(URI.create(url), "", null);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url) throws HttpException {
        return sendPutRequest(url, "", null);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, Map<String, String> header) throws HttpException {
        return sendPutRequest(URI.create(url), "", header);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, Map<String, String> header) throws HttpException {
        return sendPutRequest(url, "", header);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, String body) throws HttpException {
        return sendPutRequest(URI.create(url), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, Object body) throws HttpException {
        return sendPutRequest(URI.create(url), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, String body) throws HttpException {
        return sendPutRequest(url, body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, Object body) throws HttpException {
        return sendPutRequest(url, body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, String body, Map<String, String> header) throws HttpException {
        return sendPutRequest(URI.create(url), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, Object body, Map<String, String> header) throws HttpException {
        return sendPutRequest(URI.create(url), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, String body, Map<String, String> header) throws HttpException {
        return sendPutRequest(url, body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, Object body, Map<String, String> header) throws HttpException {
        return sendPutRequest(url, body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, String body, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url, urlParams)), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, Object body, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url, urlParams)), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, String body, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url.toString(), urlParams)), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, Object body, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url.toString(), urlParams)), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, String body, Map<String, String> header, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url, urlParams)), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, Object body, Map<String, String> header, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url, urlParams)), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, String body, Map<String, String> header, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url.toString(), urlParams)), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, Object body, Map<String, String> header, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url.toString(), urlParams)), body, header);
    }


    //Async PUT
    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, String body) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Object body) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, String body) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, String body, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Object body, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, String body, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body, Map<String, String> header) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, String body, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Object body, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, String body, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, String body, Map<String, String> header, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Object body, Map<String, String> header, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, String body, Map<String, String> header, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body, Map<String, String> header, String... urlParams) throws HttpException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }
    

    private void init(HttpClient client, HttpMapper httpMapper){
        if(client == null){
            client = HttpClient.newHttpClient();
        }
        if(httpMapper == null){
            httpMapper = new DefaultHttpMapper();
        }

        this.client = client;
        this.httpHandlers = new ArrayList<>();
        this.httpMapper = httpMapper;
    }

    private String urlConvert(String url, String[] urlParams){
        for (String param : urlParams) {
            url = url.replaceFirst(":param", param);
            url = url.replaceFirst("@param", param);
        }

        return url;
    }

    private <T> HttpRequestResult<T> sendGetRequest(URI url, Map<String, String> headers) throws HttpException{
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(url)
            .GET();

            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }

            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            
            for (HttpHandler httpHandler : httpHandlers) {
                httpHandler.onResult(response);
            }

            return new HttpRequestResultImpl<>(response, httpMapper);
        } catch (Exception e) {
            throw new HttpException(600, e.getMessage());
        }
    }    

    private <T> HttpRequestResult<T> sendPostRequest(URI url, String body, Map<String, String> headers) throws HttpException{
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(url)
            .POST(HttpRequest.BodyPublishers.ofString(body));

            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }

            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            for (HttpHandler httpHandler : httpHandlers) {
                httpHandler.onResult(response);
            }
            return new HttpRequestResultImpl<>(response, httpMapper); 
        } catch (Exception e) {
            throw new HttpException(600, e.getMessage());
        }
    }

    private <T> HttpRequestResult<T> sendPutRequest(URI url, String body, Map<String, String> headers) throws HttpException{
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(url)
            .PUT(HttpRequest.BodyPublishers.ofString(body));

            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }

            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            for (HttpHandler httpHandler : httpHandlers) {
                httpHandler.onResult(response);
            }
            return new HttpRequestResultImpl<>(response, httpMapper); 
        } catch (Exception e) {
            throw new HttpException(600, e.getMessage());
        }
    }

    private <T> HttpRequestResult<T> sendPutRequest(URI url, Object body, Map<String, String> headers) throws HttpException{
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(url)
            .PUT(HttpRequest.BodyPublishers.ofString(httpMapper.mapperToJson(body)));

            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }

            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            for (HttpHandler httpHandler : httpHandlers) {
                httpHandler.onResult(response);
            }
            return new HttpRequestResultImpl<>(response, httpMapper); 
        } catch (Exception e) {
            throw new HttpException(600, e.getMessage());
        }
    }

    private <T> HttpRequestResult<T> sendDeleteRequest(URI url, Map<String, String> headers) throws HttpException{
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(url)
            .DELETE();

            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            for (HttpHandler httpHandler : httpHandlers) {
                httpHandler.onResult(response);
            }
            return new HttpRequestResultImpl<>(response, httpMapper); 
        } catch (Exception e) {
            throw new HttpException(600, e.getMessage());
        }
    }
}


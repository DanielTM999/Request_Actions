package dtm.request_actions.http.simple.implementation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.exceptions.HttpRuntimeException;
import dtm.request_actions.http.simple.annotations.Encode;
import dtm.request_actions.http.simple.annotations.MultipartForm;
import dtm.request_actions.http.simple.core.HttpAction;
import dtm.request_actions.http.simple.core.HttpHandler;
import dtm.request_actions.http.simple.core.HttpType;
import dtm.request_actions.http.simple.core.config.RequestConfiguration;
import dtm.request_actions.http.simple.core.config.RequestConfigurationBody;
import dtm.request_actions.http.simple.core.mapper.HttpMapper;
import dtm.request_actions.http.simple.core.result.HttpRequestResult;

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
    public <T> HttpRequestResult<T> get(String url, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody requestConfiguration = new DefaultRequestConfigurationBody();
        configuration.accept(requestConfiguration);
        return sendGetRequest(URI.create(url), null, requestConfiguration);
    }

    @Override
    public <T> HttpRequestResult<T> get(String url, String... urlParams) throws HttpException {
        return sendGetRequest(URI.create(urlConvert(url, urlParams)), null);
    }

    @Override
    public <T> HttpRequestResult<T> get(String url, Consumer<RequestConfiguration> configuration, String... urlParams) throws HttpException {
        DefaultRequestConfigurationBody requestConfiguration = new DefaultRequestConfigurationBody();
        configuration.accept(requestConfiguration);
        return sendGetRequest(URI.create(urlConvert(url, urlParams)), null, requestConfiguration);
    }

    @Override
    public <T> HttpRequestResult<T> get(URI url) throws HttpException {
        return sendGetRequest(url, null);
    }

    @Override
    public <T> HttpRequestResult<T> get(URI url, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody requestConfiguration = new DefaultRequestConfigurationBody();
        configuration.accept(requestConfiguration);
        return sendGetRequest(url, null, requestConfiguration);
    }

    @Override
    public <T> HttpRequestResult<T> get(String url, Map<String, String> header) throws HttpException {
        return sendGetRequest(URI.create(url), header);
    }

    @Override
    public <T> HttpRequestResult<T> get(String url, Map<String, String> header, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody requestConfiguration = new DefaultRequestConfigurationBody();
        configuration.accept(requestConfiguration);
        return sendGetRequest(URI.create(url), header, requestConfiguration);
    }

    @Override
    public <T> HttpRequestResult<T> get(String url, Map<String, String> header, String... urlParams) throws HttpException {
        return sendGetRequest(URI.create(urlConvert(url, urlParams)), header);
    }

    @Override
    public <T> HttpRequestResult<T> get(String url, Map<String, String> header, Consumer<RequestConfiguration> configuration, String... urlParams) throws HttpException {
        DefaultRequestConfigurationBody requestConfiguration = new DefaultRequestConfigurationBody();
        configuration.accept(requestConfiguration);
        return sendGetRequest(URI.create(urlConvert(url, urlParams)), header, requestConfiguration);
    }

    @Override
    public <T> HttpRequestResult<T> get(URI url, Map<String, String> header) throws HttpException {
        return sendGetRequest(url, header);
    }

    @Override
    public <T> HttpRequestResult<T> get(URI url, Map<String, String> header, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody requestConfiguration = new DefaultRequestConfigurationBody();
        configuration.accept(requestConfiguration);
        return sendGetRequest(url, header, requestConfiguration);
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
    public <T> Future<HttpRequestResult<T>> getAsync(String url, Consumer<RequestConfiguration> configuration) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url, configuration);
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
    public <T> Future<HttpRequestResult<T>> getAsync(String url, Consumer<RequestConfiguration> configuration, String... urlParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url, configuration, urlParams);
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
    public <T> Future<HttpRequestResult<T>> getAsync(URI url, Consumer<RequestConfiguration> configuration) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url, configuration);
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
    public <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header, Consumer<RequestConfiguration> configuration) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url, header, configuration);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header, String... urlParams){
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> getAsync(String url, Map<String, String> header, Consumer<RequestConfiguration> configuration, String... urlParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url, header, configuration, urlParams);
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

    @Override
    public <T> Future<HttpRequestResult<T>> getAsync(URI url, Map<String, String> header, Consumer<RequestConfiguration> configuration) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(url, header, configuration);
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
    public <T> HttpRequestResult<T> post(String url, String body, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody configurationBody = new DefaultRequestConfigurationBody();
        configuration.accept(configurationBody);
        return sendPostRequest(URI.create(url), body, null, configurationBody);
    }

    @Override
    public <T> HttpRequestResult<T> post(String url, Object body) throws HttpException {
        return sendPostRequest(URI.create(url), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> post(String url, Object body, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody configurationBody = new DefaultRequestConfigurationBody();
        configuration.accept(configurationBody);
        return sendPostRequest(URI.create(url), body, null, configurationBody);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, String body) throws HttpException {
        return sendPostRequest(url, body, null);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, String body, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody configurationBody = new DefaultRequestConfigurationBody();
        configuration.accept(configurationBody);
        return sendPostRequest(url, body, null, configurationBody);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, Object body) throws HttpException {
        return sendPostRequest(url, body, null);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, Object body, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody configurationBody = new DefaultRequestConfigurationBody();
        configuration.accept(configurationBody);
        return sendPostRequest(url, body, null, configurationBody);
    }

    @Override
    public <T> HttpRequestResult<T> post(String url, String body, Map<String, String> header) throws HttpException {
        return sendPostRequest(URI.create(url), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> post(String url, String body, Map<String, String> header, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody configurationBody = new DefaultRequestConfigurationBody();
        configuration.accept(configurationBody);
        return sendPostRequest(URI.create(url), body, header, configurationBody);
    }

    @Override
    public <T> HttpRequestResult<T> post(String url, Object body, Map<String, String> header) throws HttpException {
        return sendPostRequest(URI.create(url), (body), header);
    }

    @Override
    public <T> HttpRequestResult<T> post(String url, Object body, Map<String, String> header, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody configurationBody = new DefaultRequestConfigurationBody();
        configuration.accept(configurationBody);
        return sendPostRequest(URI.create(url), body, header, configurationBody);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, String body, Map<String, String> header) throws HttpException {
        return sendPostRequest(url, body, header);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, String body, Map<String, String> header, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody configurationBody = new DefaultRequestConfigurationBody();
        configuration.accept(configurationBody);
        return sendPostRequest(url, body, header, configurationBody);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, Object body, Map<String, String> header) throws HttpException {
        return sendPostRequest(url, (body), header);
    }

    @Override
    public <T> HttpRequestResult<T> post(URI url, Object body, Map<String, String> header, Consumer<RequestConfiguration> configuration) throws HttpException {
        DefaultRequestConfigurationBody configurationBody = new DefaultRequestConfigurationBody();
        configuration.accept(configurationBody);
        return sendPostRequest(url, body, header, configurationBody);
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
    public <T> Future<HttpRequestResult<T>> postAsync(String url, String body, Consumer<RequestConfiguration> configuration)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, configuration);
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
    public <T> Future<HttpRequestResult<T>> postAsync(String url, Object body, Consumer<RequestConfiguration> configuration) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, configuration);
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
    public <T> Future<HttpRequestResult<T>> postAsync(URI url, String body, Consumer<RequestConfiguration> configuration) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, configuration);
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
    public <T> Future<HttpRequestResult<T>> postAsync(URI url, Object body, Consumer<RequestConfiguration> configuration)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, configuration);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(String url, String body, Map<String, String> header) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(String url, String body, Map<String, String> header, Consumer<RequestConfiguration> configuration) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header, configuration);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(String url, Object body, Map<String, String> header)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(String url, Object body, Map<String, String> header, Consumer<RequestConfiguration> configuration) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header, configuration);
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
    public <T> Future<HttpRequestResult<T>> postAsync(URI url, String body, Map<String, String> header, Consumer<RequestConfiguration> configuration)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header, configuration);
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

    @Override
    public <T> Future<HttpRequestResult<T>> postAsync(URI url, Object body, Map<String, String> header, Consumer<RequestConfiguration> configuration) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(url, body, header, configuration);
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
        return sendPutRequest(URI.create(url), (body), null);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, String body) throws HttpException {
        return sendPutRequest(url, body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, Object body) throws HttpException {
        return sendPutRequest(url, (body), null);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, String body, Map<String, String> header) throws HttpException {
        return sendPutRequest(URI.create(url), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, Object body, Map<String, String> header) throws HttpException {
        return sendPutRequest(URI.create(url), (body), header);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, String body, Map<String, String> header) throws HttpException {
        return sendPutRequest(url, body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, Object body, Map<String, String> header) throws HttpException {
        return sendPutRequest(url, (body), header);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, String body, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url, urlParams)), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, Object body, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url, urlParams)), (body), null);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, String body, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url.toString(), urlParams)), body, null);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, Object body, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url.toString(), urlParams)), (body), null);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, String body, Map<String, String> header, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url, urlParams)), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(String url, Object body, Map<String, String> header, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url, urlParams)), (body), header);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, String body, Map<String, String> header, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url.toString(), urlParams)), body, header);
    }

    @Override
    public <T> HttpRequestResult<T> put(URI url, Object body, Map<String, String> header, String... urlParams) throws HttpException {
        return sendPutRequest(URI.create(urlConvert(url.toString(), urlParams)), (body), header);
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
    public <T> Future<HttpRequestResult<T>> putAsync(String url, String body) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Map<String, String> header)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Object body) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, String body) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, String body, Map<String, String> header) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Object body, Map<String, String> header) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, String body, Map<String, String> header) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body, Map<String, String> header)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, String body, String... urlParams)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Object body, String... urlParams)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, String body, String... urlParams)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body, String... urlParams)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, String body, Map<String, String> header, String... urlParams)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(String url, Object body, Map<String, String> header, String... urlParams){
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, String body, Map<String, String> header, String... urlParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public <T> Future<HttpRequestResult<T>> putAsync(URI url, Object body, Map<String, String> header, String... urlParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return put(url, body, header, urlParams);
            } catch (HttpException e) {
                throw new HttpRuntimeException(e.getCode(), e.getMessage(), e);
            }
        });
    }

    @Override
    public void setHttpMapper(HttpMapper httpMapper) {
        this.httpMapper = httpMapper;
    }

    @Override
    public void reset() {
        this.client = null;
        this.httpMapper = null;
        init(null, null);
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

    //get
    private <T> HttpRequestResult<T> sendGetRequest(URI url, Map<String, String> headers) throws HttpException{
        return sendGetRequest(url, headers, new DefaultRequestConfigurationBody());
    }

    private <T> HttpRequestResult<T> sendGetRequest(URI url, Map<String, String> headers, RequestConfigurationBody configurationBody) throws HttpException{
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(url)
            .GET();

            if(configurationBody.getTimeout() > 0){
                requestBuilder.timeout(Duration.ofMillis(configurationBody.getTimeout()));
            }

            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }

            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            
            for (HttpHandler httpHandler : httpHandlers) {
                httpHandler.onResult(response);
            }
            return new HttpRequestResultImpl<>(response, httpMapper, configurationBody.getHttpTypeResponse());
        } catch (Exception e) {
            throw new HttpException(600, e.getMessage());
        }
    }

    //post
    private <T> HttpRequestResult<T> sendPostRequest(URI url, Object body, Map<String, String> headers) throws HttpException{
        return sendPostRequest(url, body, headers, new DefaultRequestConfigurationBody());
    }

    private <T> HttpRequestResult<T> sendPostRequest(URI url, Object body, Map<String, String> headers, RequestConfigurationBody configurationBody) throws HttpException{
        try {
            boolean isMultpartForm = isMultipartForm(body);
            HttpRequest.Builder requestBuilder;
            if(isMultpartForm){
                requestBuilder = buildMultipartRequest(body)
                        .uri(url);
            }else{
                requestBuilder = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(ofString(configurationBody.getHttpTypeBody(), body)));
            }

            if(configurationBody.getTimeout() > 0){
                requestBuilder.timeout(Duration.ofMillis(configurationBody.getTimeout()));
            }

            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }

            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            for (HttpHandler httpHandler : httpHandlers) {
                httpHandler.onResult(response);
            }
            return new HttpRequestResultImpl<>(response, httpMapper, configurationBody.getHttpTypeResponse());
        } catch (Exception e) {
            throw new HttpException(600, e.getMessage());
        }
    }

    //put
    private <T> HttpRequestResult<T> sendPutRequest(URI url, Object body, Map<String, String> headers) throws HttpException{
        return sendPutRequest(url, body, headers, new DefaultRequestConfigurationBody());
    }

    private <T> HttpRequestResult<T> sendPutRequest(URI url, Object body, Map<String, String> headers, RequestConfigurationBody configurationBody) throws HttpException{
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(url)
            .PUT(HttpRequest.BodyPublishers.ofString(ofString(configurationBody.getHttpTypeBody(),body)));

            if(configurationBody.getTimeout() > 0){
                requestBuilder.timeout(Duration.ofMillis(configurationBody.getTimeout()));
            }

            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }

            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            for (HttpHandler httpHandler : httpHandlers) {
                httpHandler.onResult(response);
            }
            return new HttpRequestResultImpl<>(response, httpMapper, configurationBody.getHttpTypeResponse());
        } catch (Exception e) {
            throw new HttpException(600, e.getMessage());
        }
    }


    //delete
    private <T> HttpRequestResult<T> sendDeleteRequest(URI url, Map<String, String> headers) throws HttpException{
        return sendDeleteRequest(url, headers, new DefaultRequestConfigurationBody());
    }

    private <T> HttpRequestResult<T> sendDeleteRequest(URI url, Map<String, String> headers, RequestConfigurationBody configurationBody) throws HttpException{
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(url)
            .DELETE();

            if(configurationBody.getTimeout() > 0){
                requestBuilder.timeout(Duration.ofMillis(configurationBody.getTimeout()));
            }

            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            for (HttpHandler httpHandler : httpHandlers) {
                httpHandler.onResult(response);
            }
            return new HttpRequestResultImpl<>(response, httpMapper, configurationBody.getHttpTypeResponse());
        } catch (Exception e) {
            throw new HttpException(600, e.getMessage());
        }
    }


    private String ofString(HttpType httpType, Object body){
        return (httpType == HttpType.JSON) ? httpMapper.mapperToJson(body) : httpMapper.mapperToXML(body);
    }

    private boolean isMultipartForm(Object body){
        if (body == null) {
            return false;
        }

        Class<?> clazz = body.getClass();
        return clazz.isAnnotationPresent(MultipartForm.class);
    }

    private HttpRequest.Builder buildMultipartRequest(Object body) throws IOException{
        String boundary = "----JavaBoundaryHttpActions" + UUID.randomUUID();
        HttpRequest.BodyPublisher bodyPublisher = buildMultipartBodyPublisher(body, boundary);

        return HttpRequest.newBuilder()
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(bodyPublisher);

    }

    private HttpRequest.BodyPublisher buildMultipartBodyPublisher(Object body, String boundary) throws IOException {
        List<HttpRequest.BodyPublisher> publishers = new ArrayList<>();

        processObjectFieldsPublisher(body, boundary, "", publishers);

        publishers.add(HttpRequest.BodyPublishers.ofByteArray(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8)));

        return HttpRequest.BodyPublishers.concat(publishers.toArray(new HttpRequest.BodyPublisher[0]));
    }

    private void processObjectFieldsPublisher(Object obj, String boundary, String parentPrefix, List<HttpRequest.BodyPublisher> publishers) throws IOException {
        if (obj == null) return;

        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value == null) continue;

                String fieldName = parentPrefix.isEmpty() ? field.getName() : parentPrefix + "." + field.getName();

                if (value instanceof File file) {
                    publishers.add(filePartPublisher(fieldName, file, boundary));
                } else if (value instanceof Path path) {
                    publishers.add(filePartPublisher(fieldName, path.toFile(), boundary));
                } else if (isPrimitiveOrWrapperOrString(value.getClass())) {
                    publishers.add(textPartPublisher(fieldName, value.toString(), boundary));
                } else if (value instanceof Iterable<?> iterable) {
                    processIterablePublisher(fieldName, iterable, boundary, publishers);
                } else {
                    if(clazz.isAnnotationPresent(Encode.class)){
                        Encode encode = clazz.getAnnotation(Encode.class);
                        String content = ofString(encode.value(), value);
                        publishers.add(textPartPublisher(fieldName, content, boundary));
                    }else{
                        processObjectFieldsPublisher(value, boundary, fieldName, publishers);
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar o campo: " + field.getName(), e);
            }
        }
    }

    private void processIterablePublisher(String fieldName, Iterable<?> iterable, String boundary, List<HttpRequest.BodyPublisher> publishers) throws IOException {
        int index = 0;

        for (Object item : iterable) {
            if (item == null) continue;

            Class<?> clazz = item.getClass();
            String indexedName = fieldName + "[" + index + "]";

            if (item instanceof File f) {
                publishers.add(filePartPublisher(indexedName, f, boundary));
            } else if (item instanceof Path p) {
                publishers.add(filePartPublisher(indexedName, p.toFile(), boundary));
            } else if (isPrimitiveOrWrapperOrString(item.getClass())) {
                publishers.add(textPartPublisher(indexedName, item.toString(), boundary));
            } else {
                if(clazz.isAnnotationPresent(Encode.class)){
                    Encode encode = clazz.getAnnotation(Encode.class);
                    String content = ofString(encode.value(), item);
                    publishers.add(textPartPublisher(fieldName, content, boundary));
                }else{
                    processObjectFieldsPublisher(item, boundary, fieldName, publishers);
                }
            }
            index++;
        }
    }

    private HttpRequest.BodyPublisher textPartPublisher(String fieldName, Object value, String boundary) throws IOException {
        String part = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"" + fieldName + "\"\r\n\r\n"
                + value + "\r\n";
        return HttpRequest.BodyPublishers.ofByteArray(part.getBytes(StandardCharsets.UTF_8));
    }

    private HttpRequest.BodyPublisher filePartPublisher(String fieldName, File file, String boundary) throws IOException {
        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) mimeType = "application/octet-stream";

        String header = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + file.getName() + "\"\r\n"
                + "Content-Type: " + mimeType + "\r\n\r\n";

        String footer = "\r\n";

        return HttpRequest.BodyPublishers.concat(
                HttpRequest.BodyPublishers.ofByteArray(header.getBytes(StandardCharsets.UTF_8)),
                HttpRequest.BodyPublishers.ofInputStream(() -> {
                    try {
                        return Files.newInputStream(file.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }),
                HttpRequest.BodyPublishers.ofByteArray(footer.getBytes(StandardCharsets.UTF_8))
        );
    }

    private boolean isPrimitiveOrWrapperOrString(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Boolean.class ||
                clazz == Byte.class ||
                clazz == Character.class ||
                clazz == Short.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Float.class ||
                clazz == Double.class ||
                CharSequence.class.isAssignableFrom(clazz) ||
                clazz == BigDecimal.class ||
                clazz == BigInteger.class ||
                clazz.isEnum() ||
                clazz == LocalDate.class ||
                clazz == LocalDateTime.class ||
                clazz == ZonedDateTime.class ||
                clazz == OffsetDateTime.class ||
                clazz == OffsetTime.class ||
                clazz == Instant.class;
    }

}


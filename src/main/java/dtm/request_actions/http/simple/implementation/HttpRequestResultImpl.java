package dtm.request_actions.http.simple.implementation;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import dtm.request_actions.exceptions.ErrorBaseRuntimeException;
import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.http.simple.core.HttpType;
import dtm.request_actions.http.simple.core.StreamReader;
import dtm.request_actions.http.simple.core.mapper.HttpMapper;
import dtm.request_actions.http.simple.core.result.HttpHeaderResult;
import dtm.request_actions.http.simple.core.result.HttpRequestResult;
import dtm.request_actions.http.simple.core.result.event.HttpErrorEvent;
import dtm.request_actions.http.simple.core.result.event.HttpSucessEvent;

public class HttpRequestResultImpl<T> extends HttpRequestResult<T> {

    private HttpMapper httpMapper;
    private final HttpResponse<InputStream> baseResponse;
    private final StreamReader streamReader;
    private final LazyBody lazyBody;
    private int statusCode;

    private HttpErrorEvent errorEvent;
    private boolean errorEventAsync;

    private HttpSucessEvent<T> sucessEvent;
    private boolean sucessEventAsync;

    HttpRequestResultImpl(HttpResponse<InputStream> baseResponse, HttpMapper httpMapper, HttpType httpType){
        this.baseResponse = baseResponse;
        this.httpMapper = httpMapper;
        this.streamReader = new HttpResultStreamReader(baseResponse.body());
        this.lazyBody = new LazyBody();
        configure();
    }

    @Override
    public void setMapper(HttpMapper mapper) {
        this.httpMapper = mapper;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public HttpHeaderResult getHeader() {
        return new HttpHeaderResultImpl(baseResponse);
    }

    
    @SuppressWarnings("unchecked")
    @Override
    public Optional<String> getBody() {
        try {
            String body = getBodyString();
            if(statusCode != 200){
                addEventError(new Exception("code: "+statusCode), body);
            }else{
                addEventSucess(Optional.of((T)body));
            }
            return Optional.of(body);
        }catch (Exception e) { 
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> getBody(Class<T> referenceToMapper) {
        try{
            String body = getBodyString();
            try {
                Optional<T> result = serialize(referenceToMapper);
                if(statusCode != 200){
                    addEventError(new Exception("code: "+statusCode), body);
                }else{
                    addEventSucess(result);
                }
                return result;
            } catch (Exception e) {
                addEventError(e, body);
                return Optional.empty();
            }
        }catch (Exception e){
            addEventError(e, "");
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> getBody(HttpMapper mapper, Class<T> referenceToMapper) {
       try{
           String body = getBodyString();
           try {
               Optional<T> result = serialize(mapper, referenceToMapper);
               if(statusCode != 200){
                   addEventError(new Exception("code: "+statusCode), body);
               }else{
                   addEventSucess(result);
               }
               return result;
           } catch (Exception e) {
               addEventError(e, body);
               return Optional.empty();
           }
       }catch (Exception e){
           addEventError(e, "");
           return Optional.empty();
       }
    }

    @Override
    public <S> Optional<S> ifErrorGet(Class<S> reference) {
        if(statusCode != 200){
            try {
                return Optional.ofNullable(httpMapper.mapper(getBodyString(), reference));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> ifErrorGet() {
        if(statusCode != 200){
            try {
                return Optional.ofNullable(getBodyString());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public StreamReader getStreamReader() {
        return streamReader;
    }

    @Override
    public void registerErrorEvent(boolean async, HttpErrorEvent errorEvent) {
        this.errorEvent = errorEvent;
        errorEventAsync = async;
    }

    @Override
    public void registerErrorEvent(HttpErrorEvent errorEvent) {
        this.errorEvent = errorEvent;
        errorEventAsync = false;
    }

    @Override
    public void registerSucessEvent(boolean async, HttpSucessEvent<T> sucessEvent) {
        this.sucessEvent = sucessEvent;
        sucessEventAsync = async;
    }

    @Override
    public void registerSucessEvent(HttpSucessEvent<T> sucessEvent) {
        this.sucessEvent = sucessEvent;
        sucessEventAsync = false;
    }

    private void configure(){
        if(baseResponse != null){
            statusCode = baseResponse.statusCode();
        }
    }

    private Optional<T> serialize(HttpMapper mapper, Class<T> referenceToMapper){
        try {
            if(mapper != null){
                setMapper(mapper);
            }
            return Optional.ofNullable(httpMapper.mapper(getBodyString(), referenceToMapper));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<T> serialize(Class<T> referenceToMapper){
        try {
            return Optional.ofNullable(httpMapper.mapper(getBodyString(), referenceToMapper));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void addEventSucess(final Optional<T> obj){
        if(sucessEvent != null){
            if(sucessEventAsync){
                new Thread(() -> {
                    sucessEvent.onSucess(obj);
                }).start();
            }else{
                sucessEvent.onSucess(obj);
            }
        }
    }

    private void addEventError(final Throwable th, final String msg){
        if(errorEvent != null){
            if(errorEventAsync){
                new Thread(() -> {
                    errorEvent.onError(th, msg);
                }).start();
            }else{
                errorEvent.onError(th, msg);
            }
        }
    }

    private String getBodyString(){
        return lazyBody.getOrSet(() -> {
           try(streamReader){
               return new String(streamReader.readOrGetAllBytes(), StandardCharsets.UTF_8);
           }catch (Exception e){
               throw new ErrorBaseRuntimeException(500, e.getMessage(), e);
           }
        });
    }

    private static class LazyBody{
        private String bodyString;
        private boolean initialized = false;

        public String getOrSet(Supplier<String> supplier) {
            if (!initialized) {
                bodyString = supplier.get();
                initialized = true;
            }
            return bodyString;
        }

        public void clear() {
            bodyString = null;
            initialized = false;
        }
    }



}

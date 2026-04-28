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
    private final HttpType httpType;
    private byte[] bodyBytes;
    private String bodyString;
    private int statusCode;

    private HttpErrorEvent errorEvent;
    private boolean errorEventAsync;

    private HttpSucessEvent<T> sucessEvent;
    private boolean sucessEventAsync;

    HttpRequestResultImpl(HttpResponse<InputStream> baseResponse, HttpMapper httpMapper, HttpType httpType){
        this.baseResponse = baseResponse;
        this.httpMapper = httpMapper;
        this.httpType = httpType == null ? HttpType.JSON : httpType;
        this.streamReader = new HttpResultStreamReader(baseResponse.body());
        configure();
    }

    HttpRequestResultImpl(HttpResponse<InputStream> baseResponse, StreamReader streamReader, HttpMapper httpMapper, HttpType httpType){
        this.baseResponse = baseResponse;
        this.httpMapper = httpMapper;
        this.httpType = httpType == null ? HttpType.JSON : httpType;
        this.streamReader = streamReader;
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
            if(isErrorStatus()){
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
    public Optional<byte[]> getBodyBytes() {
        try {
            byte[] body = getBodyBytesRaw();
            if(isErrorStatus()){
                addEventError(new Exception("code: "+statusCode), getBodyString());
            }else{
                addEventSucess(Optional.of((T) body));
            }
            return Optional.of(body);
        }catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> getBody(Class<T> referenceToMapper) {
        try{
            try {
                Optional<T> result = serialize(referenceToMapper);
                if(isErrorStatus()){
                    addEventError(new Exception("code: "+statusCode), getBodyString());
                }else{
                    addEventSucess(result);
                }
                return result;
            } catch (Exception e) {
                addEventError(e, getBodyString());
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
           try {
               Optional<T> result = serialize(mapper, referenceToMapper);
               if(isErrorStatus()){
                   addEventError(new Exception("code: "+statusCode), getBodyString());
               }else{
                   addEventSucess(result);
               }
               return result;
           } catch (Exception e) {
               addEventError(e, getBodyString());
               return Optional.empty();
           }
       }catch (Exception e){
           addEventError(e, "");
           return Optional.empty();
       }
    }

    @Override
    public <S> Optional<S> ifErrorGet(Class<S> reference) {
        if(isErrorStatus()){
            try {
                if(byte[].class.equals(reference)){
                    return Optional.of(reference.cast(getBodyBytesRaw()));
                }
                return Optional.ofNullable(httpMapper.mapper(getBodyString(), reference, httpType));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> ifErrorGet() {
        if(isErrorStatus()){
            try {
                return Optional.ofNullable(getBodyString());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<byte[]> ifErrorGetBytes() {
        if(isErrorStatus()){
            try {
                return Optional.ofNullable(getBodyBytesRaw());
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

    private boolean isErrorStatus(){
        return !isRequestSucess();
    }

    private Optional<T> serialize(HttpMapper mapper, Class<T> referenceToMapper){
        try {
            if(byte[].class.equals(referenceToMapper)){
                return Optional.of(referenceToMapper.cast(getBodyBytesRaw()));
            }
            if(mapper != null){
                setMapper(mapper);
            }
            return Optional.ofNullable(httpMapper.mapper(getBodyString(), referenceToMapper, httpType));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<T> serialize(Class<T> referenceToMapper){
        try {
            if(byte[].class.equals(referenceToMapper)){
                return Optional.of(referenceToMapper.cast(getBodyBytesRaw()));
            }
            return Optional.ofNullable(httpMapper.mapper(getBodyString(), referenceToMapper, httpType));
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
        if(bodyString == null){
            try{
                bodyString =  new String(getBodyBytesRaw(), StandardCharsets.UTF_8);
            }catch (Exception e){
                throw new ErrorBaseRuntimeException(500, e.getMessage(), e);
            }
        }
        return bodyString;
    }

    private byte[] getBodyBytesRaw(){
        if(bodyBytes == null){
            try(streamReader){
                bodyBytes = streamReader.readOrGetAllBytes();
            }catch (Exception e){
                throw new ErrorBaseRuntimeException(500, e.getMessage(), e);
            }
        }
        return bodyBytes;
    }

}

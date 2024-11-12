package dtm.request_actions.http.implementation;

import java.net.http.HttpResponse;
import java.util.Optional;

import dtm.request_actions.http.core.mapper.HttpMapper;
import dtm.request_actions.http.core.result.HttpHeaderResult;
import dtm.request_actions.http.core.result.HttpRequestResult;

public class HttpRequestResultImpl<T> extends HttpRequestResult<T> {

    private HttpMapper httpMapper;
    private HttpResponse<String> baseResponse;
    private int statusCode;

    private HttpErrorEvent errorEvent;
    private boolean errorEventAsync;

    private HttpSucessEvent<T> sucessEvent;
    private boolean sucessEventAsync;

    HttpRequestResultImpl(HttpResponse<String> baseResponse, HttpMapper httpMapper){
        this.baseResponse = baseResponse;
        this.httpMapper = httpMapper;
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
            if(statusCode != 200){
                addEventError(new Exception("code: "+statusCode), baseResponse.body());
            }else{
                addEventSucess(Optional.ofNullable((T)baseResponse.body()));
            }
            return Optional.ofNullable(baseResponse.body());
        }catch (Exception e) { 
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> getBody(Class<T> referenceToMapper) {
        try {
            Optional<T> result = serialize(referenceToMapper);
            if(statusCode != 200){
                addEventError(new Exception("code: "+statusCode), baseResponse.body());
            }else{
                addEventSucess(result);
            }
            return result;
        } catch (Exception e) {
            addEventError(e, baseResponse.body());
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> getBody(HttpMapper mapper, Class<T> referenceToMapper) {
        try {
            Optional<T> result = serialize(mapper, referenceToMapper);
            if(statusCode != 200){
                addEventError(new Exception("code: "+statusCode), baseResponse.body());
            }else{
                addEventSucess(result);
            }
            return result;
        } catch (Exception e) {
            addEventError(e, baseResponse.body());
            return Optional.empty();
        }
    }

    @Override
    public <S> Optional<S> ifErrorGet(Class<S> reference) {
        if(statusCode != 200){
            try {
                return Optional.ofNullable(httpMapper.mapper(baseResponse.body(), reference));
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
                return Optional.ofNullable(baseResponse.body());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
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
            return Optional.ofNullable(httpMapper.mapper(baseResponse.body(), referenceToMapper));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<T> serialize(Class<T> referenceToMapper){
        try {
            return Optional.ofNullable(httpMapper.mapper(baseResponse.body(), referenceToMapper));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void addEventSucess(final Optional<T> obj){
        if(sucessEvent != null){
            if(sucessEventAsync){
                new Thread(() -> {
                    sucessEvent.onSucess(obj);
                }).run();
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
}

package dtm.request_actions.http.core.result;

import java.util.Optional;

import dtm.request_actions.http.core.mapper.HttpMapper;

public abstract class HttpRequestResult<T> {
    public abstract void setMapper(HttpMapper mapper);
    public abstract int getStatusCode();
    public abstract HttpHeaderResult getHeader();
    public abstract Optional<String> getBody();
    public abstract Optional<T> getBody(Class<T> referenceToMapper);
    public abstract Optional<T> getBody(HttpMapper mapper, Class<T> referenceToMapper);
    public abstract <S> Optional<S> ifErrorGet(Class<S> reference);
    public abstract Optional<String> ifErrorGet();
    public abstract void registerErrorEvent(boolean async, HttpErrorEvent errorEvent);
    public abstract void registerErrorEvent(HttpErrorEvent errorEvent);
    public abstract void registerSucessEvent(boolean async, HttpSucessEvent<T> sucessEvent);
    public abstract void registerSucessEvent(HttpSucessEvent<T> sucessEvent);

    public interface HttpErrorEvent {
        void onError(Throwable throwable, String message);
    }

    public interface HttpSucessEvent<T> {
        void onSucess(Optional<T> result);
    }
}

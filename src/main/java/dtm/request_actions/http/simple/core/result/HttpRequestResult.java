package dtm.request_actions.http.simple.core.result;

import java.util.Optional;

import dtm.request_actions.http.simple.core.mapper.HttpMapper;
import dtm.request_actions.http.simple.core.result.event.HttpErrorEvent;
import dtm.request_actions.http.simple.core.result.event.HttpSucessEvent;

public abstract class HttpRequestResult<T> implements HttpResult {
    public abstract void setMapper(HttpMapper mapper);
    public abstract Optional<T> getBody(Class<T> referenceToMapper);
    public abstract Optional<T> getBody(HttpMapper mapper, Class<T> referenceToMapper);
    public abstract <S> Optional<S> ifErrorGet(Class<S> reference);
    public abstract void registerErrorEvent(boolean async, HttpErrorEvent errorEvent);
    public abstract void registerErrorEvent(HttpErrorEvent errorEvent);
    public abstract void registerSucessEvent(boolean async, HttpSucessEvent<T> sucessEvent);
    public abstract void registerSucessEvent(HttpSucessEvent<T> sucessEvent);
}

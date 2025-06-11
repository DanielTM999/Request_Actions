package dtm.request_actions.http.simple.core.config;

import dtm.request_actions.http.simple.core.HttpType;

import java.util.concurrent.TimeUnit;

public abstract class RequestConfiguration {
    public abstract void setTimeout(long timeout);
    public abstract void setTimeout(long timeout, TimeUnit timeUnit);

    public abstract void bodyFormat(HttpType httpType);
    public abstract void responseFormat(HttpType httpType);
}

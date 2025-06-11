package dtm.request_actions.http.simple.core.config;

import dtm.request_actions.http.simple.core.HttpType;

public abstract class RequestConfigurationBody extends RequestConfiguration{
    public abstract long getTimeout();
    public abstract HttpType getHttpTypeBody();
    public abstract HttpType getHttpTypeResponse();
}

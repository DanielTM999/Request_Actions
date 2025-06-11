package dtm.request_actions.http.simple.implementation;

import dtm.request_actions.http.simple.core.HttpType;
import dtm.request_actions.http.simple.core.config.RequestConfigurationBody;

import java.util.concurrent.TimeUnit;

public class DefaultRequestConfigurationBody extends RequestConfigurationBody {
    private long timeout;
    private HttpType httpTypeBody;
    private HttpType httpTypeResponse;

    public DefaultRequestConfigurationBody(){
        timeout = 0;
        httpTypeBody = HttpType.JSON;
        httpTypeResponse = HttpType.JSON;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }


    @Override
    public HttpType getHttpTypeResponse() {
        return httpTypeResponse;
    }

    @Override
    public HttpType getHttpTypeBody() {
        return httpTypeBody;
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void setTimeout(long timeout, TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(timeout);
    }

    @Override
    public void bodyFormat(HttpType httpType) {
        this.httpTypeBody = httpType;
    }

    @Override
    public void responseFormat(HttpType httpType) {
        this.httpTypeResponse = httpType;
    }
}

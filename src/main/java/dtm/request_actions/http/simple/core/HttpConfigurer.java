package dtm.request_actions.http.simple.core;

import dtm.request_actions.http.simple.core.mapper.HttpMapper;

public interface HttpConfigurer {
    void reset();
    HttpMapper getHttpMapper();
    void setHttpMapper(HttpMapper httpMapper);
}

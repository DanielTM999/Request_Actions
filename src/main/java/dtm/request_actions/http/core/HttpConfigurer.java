package dtm.request_actions.http.core;

import dtm.request_actions.http.core.mapper.HttpMapper;

public interface HttpConfigurer {
    void setRequestJSON();
    void setResponseJSON();
    void setRequestXML();
    void setResponseXML();
    void reset();
    void setHttpMapper(HttpMapper httpMapper);
}

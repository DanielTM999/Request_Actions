package dtm.request_actions.http.core;

public interface HttpConfigurer {
    void setRequestJSON();
    void setResponseJSON();
    void setRequestXML();
    void setResponseXML();
}

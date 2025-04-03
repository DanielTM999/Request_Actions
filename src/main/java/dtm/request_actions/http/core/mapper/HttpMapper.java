package dtm.request_actions.http.core.mapper;

import dtm.request_actions.http.core.HttpType;

public interface HttpMapper {
    default HttpType getResponseType(){return HttpType.JSON;}
    default void setResponseType(HttpType httpType){};
    abstract <T> T mapper(String baseRequestResponse, Class<T> referenceMapper);
    abstract String mapperToJson(Object baseRequestResponse);
    abstract String mapperToXML(Object baseRequestResponse);
}

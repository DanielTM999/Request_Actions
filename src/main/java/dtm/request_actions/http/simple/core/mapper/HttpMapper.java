package dtm.request_actions.http.simple.core.mapper;

import dtm.request_actions.http.simple.core.HttpType;

public interface HttpMapper {
    default <T> T mapper(String baseRequestResponse, Class<T> referenceMapper){
        return mapper(baseRequestResponse, referenceMapper, HttpType.JSON);
    }
    <T> T mapper(String baseRequestResponse, Class<T> referenceMapper, HttpType httpType);
    String mapperToJson(Object baseRequestResponse);
    String mapperToXML(Object baseRequestResponse);
}

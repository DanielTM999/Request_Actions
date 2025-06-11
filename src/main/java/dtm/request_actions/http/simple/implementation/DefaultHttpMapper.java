package dtm.request_actions.http.simple.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import dtm.request_actions.http.simple.core.HttpType;
import dtm.request_actions.http.simple.core.mapper.HttpMapper;

public class DefaultHttpMapper implements HttpMapper{

    @Override
    public <T> T mapper(String baseRequestResponse, Class<T> referenceMapper, HttpType httpType) {
        if(referenceMapper.equals(String.class)){
            return referenceMapper.cast(baseRequestResponse);
        }else{
            try {
                if(httpType.equals(HttpType.JSON)){
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue(baseRequestResponse, referenceMapper);
                }else{
                    XmlMapper xmlMapper = new XmlMapper();
                    return xmlMapper.readValue(baseRequestResponse, referenceMapper);
                }
            } catch (Exception e) {
               return null;
            }
        }
    }

    @Override
    public String mapperToJson(Object baseRequestResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(baseRequestResponse);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public String mapperToXML(Object baseRequestResponse) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.writeValueAsString(baseRequestResponse);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    
}

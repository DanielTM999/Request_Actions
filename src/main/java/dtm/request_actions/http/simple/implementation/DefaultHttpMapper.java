package dtm.request_actions.http.simple.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import dtm.request_actions.http.simple.core.HttpType;
import dtm.request_actions.http.simple.core.mapper.HttpMapper;

public class DefaultHttpMapper implements HttpMapper{

    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public <T> T mapper(String baseRequestResponse, Class<T> referenceMapper, HttpType httpType) {
        if(String.class.equals(referenceMapper)){
            return referenceMapper.cast(baseRequestResponse);
        }else{
            try {
                if(httpType.equals(HttpType.JSON)){
                    if(JsonNode.class.equals(referenceMapper)){
                        return referenceMapper.cast(jsonMapper.readTree(baseRequestResponse));
                    }

                    return jsonMapper.readValue(baseRequestResponse, referenceMapper);
                }else{
                    return xmlMapper.readValue(baseRequestResponse, referenceMapper);
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
               return null;
            }
        }
    }

    @Override
    public String mapperToJson(Object baseRequestResponse) {
        try {
            return jsonMapper.writeValueAsString(baseRequestResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

    @Override
    public String mapperToXML(Object baseRequestResponse) {
        try {
            return xmlMapper.writeValueAsString(baseRequestResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
            return null;
        }
    }
    
}

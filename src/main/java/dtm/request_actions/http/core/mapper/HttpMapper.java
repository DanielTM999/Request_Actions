package dtm.request_actions.http.core.mapper;

public interface HttpMapper {
    public abstract <T> T mapper(String baseRequestResponse, Class<T> referenceMapper);
    public abstract String mapperToJson(Object baseRequestResponse);
    public abstract String mapperToXML(Object baseRequestResponse);
}

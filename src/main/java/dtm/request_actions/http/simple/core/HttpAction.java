package dtm.request_actions.http.simple.core;

public interface HttpAction extends 
    HttpActionGet, HttpActionGetAsync,
    HttpActionPost, HttpActionPostAsync, 
    HttpActionDelete, HttpActionDeleteAsync,
    HttpActionPut, HttpActionPutAsync,
    HttpConfigurer
{
    void addHandler(HttpHandler handler);
}

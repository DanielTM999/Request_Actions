package dtm.request_actions.http.simple.core;

public interface ServerEventEmiter {
    void onEvent(String event, String content);
    void onError(Throwable throwable);
}

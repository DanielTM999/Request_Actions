package dtm.request_actions.http.simple.core.result.event;

public interface HttpErrorEvent {
    void onError(Throwable throwable, String message);
}
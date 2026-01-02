package dtm.request_actions.http.simple.core.result.event;

import java.util.Optional;

public interface HttpSucessEvent<T> {
    void onSucess(Optional<T> result);
}

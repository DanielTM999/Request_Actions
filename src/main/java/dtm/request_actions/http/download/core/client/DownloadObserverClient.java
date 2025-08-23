package dtm.request_actions.http.download.core.client;

import dtm.request_actions.http.download.core.config.ObserverConfiguration;

import java.util.List;
import java.util.Map;

public interface DownloadObserverClient {
    default void observerConfiguration(ObserverConfiguration observerConfiguration) {}
    default void onStart(long expectedSize, Map<String, List<String>> headers) {}
    default void onProgress(long bytesRead, long expectedSize, Map<String, List<String>> headers) {}
    default void onComplete(byte[] content, Map<String, List<String>> headers) {}
    default void onError(Throwable exception) {}
    default void onDisconect() {}
}

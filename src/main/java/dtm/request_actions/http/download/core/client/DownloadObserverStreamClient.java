package dtm.request_actions.http.download.core.client;

import dtm.request_actions.http.download.core.config.ObserverConfiguration;

import java.util.List;
import java.util.Map;

public interface DownloadObserverStreamClient {
    default void observerConfiguration(ObserverConfiguration observerConfiguration) {}
    default void onStart(long expectedSize, Map<String, List<String>> headers) {}
    default void onProgress(byte[] content, long bytesRead, long expectedSize, Map<String, List<String>> headers) {}
    default void onComplete(Map<String, List<String>> headers) {}
    default void onError(Throwable exception) {}
    default void onDisconect() {}
}

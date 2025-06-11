package dtm.request_actions.http.download.core.client;

import dtm.request_actions.http.download.core.config.ObserverConfiguration;

public interface DownloadObserverClient {
    default void observerConfiguration(ObserverConfiguration observerConfiguration) {}
    default void onStart(long expectedSize) {}
    default void onProgress(long bytesRead, long expectedSize) {}
    default void onComplete(byte[] content) {}
    default void onError(Throwable exception) {}
    default void onDisconect() {}
}

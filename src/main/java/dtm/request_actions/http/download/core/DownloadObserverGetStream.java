package dtm.request_actions.http.download.core;

import dtm.request_actions.http.download.core.client.DownloadObserverStreamClient;

import java.net.URI;
import java.util.Map;

public interface DownloadObserverGetStream {
    void newDownloadGetStream(String url, DownloadObserverStreamClient client);
    void newDownloadGetStream(URI url, DownloadObserverStreamClient client);
    void newDownloadGetStream(String url, Map<String, String> headers, DownloadObserverStreamClient client);
    void newDownloadGetStream(URI url, Map<String, String> headers, DownloadObserverStreamClient client);
}

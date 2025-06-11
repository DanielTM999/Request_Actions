package dtm.request_actions.http.download.core;

import dtm.request_actions.http.download.core.client.DownloadObserverClient;
import java.net.URI;
import java.util.Map;

public interface DownloadObserverGet {
    void newDownloadGet(String url, DownloadObserverClient client);
    void newDownloadGet(URI url, DownloadObserverClient client);
    void newDownloadGet(String url, Map<String, String> headers, DownloadObserverClient client);
    void newDownloadGet(URI url, Map<String, String> headers, DownloadObserverClient client);
}

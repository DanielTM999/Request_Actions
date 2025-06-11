package dtm.request_actions.http.download.service;

import dtm.request_actions.exceptions.DownloadSizeExceededException;
import dtm.request_actions.exceptions.HttpException;
import dtm.request_actions.http.download.concrete.DefaultObserverConfiguration;
import dtm.request_actions.http.download.core.DownloadObserver;
import dtm.request_actions.http.download.core.DownloadObserverGet;
import dtm.request_actions.http.download.core.client.DownloadObserverClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadObserverService implements DownloadObserver {

    private final ExecutorService executorService;

    public DownloadObserverService(){
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public DownloadObserverService(ExecutorService executorService){
        this.executorService = executorService;
    }

    @Override
    public void newDownloadGet(String url, DownloadObserverClient client) {
        CompletableFuture.runAsync(() -> execute(URI.create(url), null, client, "GET"), executorService);
    }

    @Override
    public void newDownloadGet(URI url, DownloadObserverClient client) {
        CompletableFuture.runAsync(() -> execute(url, null, client, "GET"), executorService);
    }

    @Override
    public void newDownloadGet(String url, Map<String, String> headers, DownloadObserverClient client) {
        CompletableFuture.runAsync(() -> execute(URI.create(url), headers, client, "GET"), executorService);
    }

    @Override
    public void newDownloadGet(URI url, Map<String, String> headers, DownloadObserverClient client) {
        CompletableFuture.runAsync(() -> execute(url, headers, client, "GET"), executorService);
    }


    private void execute(URI url, Map<String, String> headers, DownloadObserverClient client, String httpMethod){
        DefaultObserverConfiguration observerConfiguration = new DefaultObserverConfiguration();
        HttpURLConnection connection = null;
        headers = (headers != null) ? headers : new HashMap<>();

        try {
            client.observerConfiguration(observerConfiguration);
            URL javaUrl = url.toURL();
            connection = (HttpURLConnection) javaUrl.openConnection();
            connection.setRequestMethod(httpMethod);

            int timeout = (int) observerConfiguration.getTimeout();
            if (timeout > 0) {
                connection.setConnectTimeout(timeout);
            }

            int readTimeout = (int) observerConfiguration.getReadTimeout();
            if (readTimeout > 0){
                connection.setReadTimeout(readTimeout);
            }

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            connection.connect();

            int status = connection.getResponseCode();
            if (status >= 400) {
                try (InputStream errorStream = connection.getErrorStream()) {
                    String errorMessage = (errorStream != null)
                            ? new String(errorStream.readAllBytes())
                            : "Erro HTTP " + status;
                    throw new HttpException(status, errorMessage);
                }
            }

            int contentLength = connection.getContentLength();
            client.onStart(contentLength);

            try (InputStream input = connection.getInputStream(); ByteArrayOutputStream output = new ByteArrayOutputStream()){
                byte[] buffer = new byte[observerConfiguration.getBufferSize()];
                int bytesRead;
                long totalRead = 0;
                long maxSizeDownload = observerConfiguration.getMaxSizeDownload();
                boolean checkMaxSize = maxSizeDownload > 0;

                while ((bytesRead = input.read(buffer)) != -1) {
                    totalRead += bytesRead;
                    if (checkMaxSize) {
                        if (totalRead > maxSizeDownload) {
                            throw new DownloadSizeExceededException(maxSizeDownload, totalRead);
                        }
                    }
                    output.write(buffer, 0, bytesRead);
                    client.onProgress(totalRead, contentLength);
                }

                client.onComplete(output.toByteArray());
            }

        }catch (Exception e){
            client.onError(e);
        }finally {
            if (connection != null) connection.disconnect();
            client.onDisconect();
        }

    }
}

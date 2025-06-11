package dtm.request_actions.http.download.concrete;


import dtm.request_actions.http.download.core.config.ObserverConfigurationGetter;

import java.util.concurrent.TimeUnit;

public class DefaultObserverConfiguration extends ObserverConfigurationGetter {
    private long timeout;
    private long readTimeout;
    private long maxSizeDownload;
    private int bufferSize;

    public DefaultObserverConfiguration(){
        timeout = -1;
        maxSizeDownload = -1;
        readTimeout = -1;
        bufferSize = 8192;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public long getReadTimeout() {
        return readTimeout;
    }

    @Override
    public long getMaxSizeDownload() {
        return maxSizeDownload;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public void setBufferSize(int size) {
        if (size <= 0 || size > 1048576) {
            throw new IllegalArgumentException("Buffer size must be between 1 and 1MB");
        }
        this.bufferSize = size;
    }

    @Override
    public void setReadTimeout(long timeout) {
        this.readTimeout = timeout;
    }

    @Override
    public void setReadTimeout(long timeout, TimeUnit timeUnit) {
        this.readTimeout = timeUnit.toMillis(timeout);
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void setTimeout(long timeout, TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(timeout);
    }

    @Override
    public void setMaxSizeDownload(long bytesSize) {
        this.maxSizeDownload = bytesSize;
    }
}

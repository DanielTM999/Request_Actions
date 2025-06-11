package dtm.request_actions.http.download.core.config;

public abstract class ObserverConfigurationGetter extends ObserverConfiguration{

    public abstract long getTimeout();
    public abstract long getReadTimeout();
    public abstract long getMaxSizeDownload();
    public abstract int getBufferSize();

}

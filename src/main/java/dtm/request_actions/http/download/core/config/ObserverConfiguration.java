package dtm.request_actions.http.download.core.config;

import java.util.concurrent.TimeUnit;

public abstract class ObserverConfiguration {

    public abstract  void setBufferSize(int size);

    public abstract void setReadTimeout(long timeout);
    public abstract void setReadTimeout(long timeout, TimeUnit timeUnit);

    public abstract void setTimeout(long timeout);
    public abstract void setTimeout(long timeout, TimeUnit timeUnit);

    public abstract void setMaxSizeDownload(long bytesSize);

}

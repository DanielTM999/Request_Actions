package dtm.request_actions.exceptions;

public class DownloadSizeExceededException extends Exception {
    private final long maxAllowed;
    private final long attempted;

    public DownloadSizeExceededException(long maxAllowed, long attempted) {
        super("Download excedeu o tamanho máximo permitido: " + maxAllowed + " bytes (tentado: " + attempted + " bytes)");
        this.maxAllowed = maxAllowed;
        this.attempted = attempted;
    }

    public long getMaxAllowed() {
        return maxAllowed;
    }

    public long getAttempted() {
        return attempted;
    }
}

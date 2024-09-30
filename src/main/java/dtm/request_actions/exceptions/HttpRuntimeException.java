package dtm.request_actions.exceptions;

public class HttpRuntimeException extends ErrorBaseRuntimeException{
    public HttpRuntimeException(String msg) {
        super(msg);
    }

    public HttpRuntimeException(int code, String msg) {
        super(code, msg);
    }

    public HttpRuntimeException(Throwable th, String msg) {
        super(th, msg);
    }

    public HttpRuntimeException(int code, String msg, Throwable th) {
        super(code, msg, th);
    }
}

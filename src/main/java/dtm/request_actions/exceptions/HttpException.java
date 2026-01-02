package dtm.request_actions.exceptions;

public class HttpException extends ErrorBaseRuntimeException{
    
    public HttpException(String msg) {
        super(msg);
    }

    public HttpException(int code, String msg) {
        super(code, msg);
    }

    public HttpException(Throwable th, String msg) {
        super(th, msg);
    }

    public HttpException(int code, String msg, Throwable th) {
        super(code, msg, th);
    }
}

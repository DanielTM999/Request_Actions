package dtm.request_actions.exceptions;



public class ErrorBaseRuntimeException extends RuntimeException{
    protected int code = 403;

    public ErrorBaseRuntimeException(String msg){
        super(msg);
    }

    public ErrorBaseRuntimeException(int code, String msg){
        super(msg);
        this.code = code;
    }

    public ErrorBaseRuntimeException(Throwable th, String msg){
        super(msg, th);
    }

    public ErrorBaseRuntimeException(int code, String msg, Throwable th){
        super(msg, th);
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}

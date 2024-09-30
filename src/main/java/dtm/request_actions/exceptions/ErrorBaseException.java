package dtm.request_actions.exceptions;



public class ErrorBaseException extends Exception{
    protected int code = 403;

    public ErrorBaseException(String msg){
        super(msg);
    }

    public ErrorBaseException(int code, String msg){
        super(msg);
        this.code = code;
    }

    public ErrorBaseException(Throwable th, String msg){
        super(msg, th);
    }

    public ErrorBaseException(int code, String msg, Throwable th){
        super(msg, th);
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}

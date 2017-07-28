package org.trc.exception;


public class CouponException extends RuntimeException{

    private String code;

    private String message;

    public CouponException(String code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

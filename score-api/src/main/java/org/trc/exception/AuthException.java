package org.trc.exception;

import org.trc.enums.ExceptionEnum;

/**
 * Created by hzwzhen on 2017/6/14.
 */
public class AuthException extends RuntimeException{

    /**
     * 异常枚举
     */
    private ExceptionEnum exceptionEnum;
    /**
     * 错误信息
     */
    private String message;

    public AuthException(ExceptionEnum exceptionEnum, String message){
        super(message);
        this.exceptionEnum = exceptionEnum;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public void setExceptionEnum(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}

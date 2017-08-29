package org.trc.exception;

import org.trc.enums.ExceptionEnum;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/29
 */
public class ExpireException extends RuntimeException{

    /**
     * 异常枚举
     */
    private ExceptionEnum exceptionEnum;
    /**
     * 错误信息
     */
    private String message;

    public ExpireException(ExceptionEnum exceptionEnum, String message){
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

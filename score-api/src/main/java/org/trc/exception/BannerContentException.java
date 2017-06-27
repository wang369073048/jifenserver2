package org.trc.exception;

import org.trc.enums.ExceptionEnum;

/**
 * Created by wangzhen
 */
public class BannerContentException extends RuntimeException{

//    public static String ABNORMAL_PARAMETER = "200001";//参数检查异常
//    public static String ENTITY_NOT_EXIST = "300001";//店铺不存在
//    public static String GET_EXCEPTION = "300002";//查找出错
//    public static String INSERT_EXCEPTION = "300003";//插入出入
//    public static String UPDATE_EXCEPTION = "300004";//更新出错
//    public static String QUERY_LIST_EXCEPTION = "300009";//查询列表失败

    /**
     * 异常枚举
     */
    private ExceptionEnum exceptionEnum;
    /**
     * 错误信息
     */
    private String message;

    public BannerContentException(ExceptionEnum exceptionEnum, String message){
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

package org.trc.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by wangzhen
 */
public enum ExceptionEnum {
    /**
     * 异常编码按模块划分：
     * banner管理:800开头
     * bannerContent管理:810开头
     * 权限管理:900开头
     * 外部调用:1000开头
     * 数据库:3000开头
     * 系统异常:4000开头
     */

    SYSTEM_EXCEPTION("4000100","系统异常"),
    SYSTEM_BUSY("4000101","系统繁忙"),
    ERROR_ILLEGAL_OPERATION("4000102", "操作不合法"),
    SYSTEM_ACCREDIT_QUERY_EXCEPTION("4000301","授权相关查询异常"),
    SYSTEM_ACCREDIT_SAVE_EXCEPTION("4000302","授权相关保存异常"),
    SYSTEM_ACCREDIT_UPDATE_EXCEPTION("4000303","授权相关更新异常"),
    SYSTEM_SYS_ROLE_STATE_UPDATE_EXCEPTION("4000304","系统状态不能修改异常"),


    QUERY_LIST_EXCEPTION("4000305","查询列表异常"),
    INSERT_EXCEPTION("4000306","新增异常"),
    UPDATE_EXCEPTION("4000307","更新异常"),

    FILE_UPLOAD_EXCEPTION("1000100","文件上传异常"),
    FILE_DOWNLOAD_EXCEPTION("1000101","文件下载异常"),
    FILE_SHOW_EXCEPTION("1000102","文件显示异常"),

    DATABASE_DUPLICATE_KEY_EXCEPTION("3000100","数据库主键重复异常"),
    DATABASE_PERMISSION_DENIED_EXCEPTION("3000101","数据库数据访问权限异常"),
    DATABASE_QUERY_TIME_OUT_EXCEPTION("3000102","数据库查询超时异常"),
    DATABASE_DEADLOCK_DATA_ACESS_EXCEPTION("3000103","数据库死锁访问数据异常"),

    DATABASE_DATA_VERSION_EXCEPTION("3000104","数据库的流水记录正在使用"),
    DATABASE_SAVE_SERIAL_EXCEPTION("3000105","保存流水号异常"),

    USER_CENTER_QUERY_EXCEPTION("600101","用户中心查询异常"),

    SCORECHANGGE_QUERY_EXCEPTION("700100","积分流水查询异常"),
    SCORECHANGGE_SAVE_EXCEPTION("700101","积分流水保存异常"),
    SCORECHANGGE_DELETE_EXCEPTION("700102","积分流水删除异常"),
    SCORECHANGGE_UPDATE_EXCEPTION("700103","积分流水更新异常"),

    MANAGER_QUERY_EXCEPTION("700100","管理员查询异常"),
    MANAGER_SAVE_EXCEPTION("700101","管理员保存异常"),
    MANAGER_DELETE_EXCEPTION("700102","管理员删除异常"),
    MANAGER_UPDATE_EXCEPTION("700103","管理员更新异常"),

    BANNER_QUERY_EXCEPTION("800100","banner查询异常"),
    BANNER_SAVE_EXCEPTION("800101","banner保存异常"),
    BANNER_UPDATE_EXCEPTION("800102","banner更新异常"),
    BANNER_ID_NOT_EXIST("800103","bannerId不存在"),

    BANNERCONTENT_QUERY_EXCEPTION("810100","bannerContent查询异常"),
    BANNERCONTENT_SAVE_EXCEPTION("810101","bannerContent保存异常"),
    BANNERCONTENT_UPDATE_EXCEPTION("810102","bannerContent更新异常"),
    BANNERCONTENT_ID_NOT_EXIST("810103","bannerContentId不存在"),

    AUTH_QUERY_EXCEPTION("820100","权限查询异常"),
    AUTH_SAVE_EXCEPTION("820101","权限保存异常"),
    AUTH_UPDATE_EXCEPTION("820102","权限更新异常"),
    AUTH_ID_NOT_EXIST("820103","权限Id不存在"),
    
    /**start 权限资源管理操作相关异常 add by xab *******/
    ACCREDIT_RESOURCE_QUERY_EXCEPTION("820104","权限资源查询异常"),
    ACCREDIT_RESOURCE_SAVE_EXCEPTION("820105","权限资源保存异常"),
    ACCREDIT_RESOURCE_UPDATE_EXCEPTION("820106","权限资源更新异常"),
    /**end *******/

    CONVERTER_QUERY_EXCEPTION("830100","兑换规则查询异常"),
    CONVERTER_SAVE_EXCEPTION("830101","兑换规则保存异常"),
    CONVERTER_UPDATE_EXCEPTION("830102","兑换规则更新异常"),
    CONVERTER_LIMIT_PARAM_ERROR("830103","兑换限额参数有误"),

    COUPON_QUERY_EXCEPTION("840100","卡券查询异常"),
    COUPON_SAVE_EXCEPTION("840101","卡券保存异常"),
    COUPON_DELETE_EXCEPTION("840101","卡券删除异常"),
    COUPON_UPDATE_EXCEPTION("840102","卡券更新异常"),

    CATEGORY_QUERY_EXCEPTION("850100","bannerContent查询异常"),
    CATEGORY_SAVE_EXCEPTION("850101","bannerContent保存异常"),
    CATEGORY_UPDATE_EXCEPTION("850102","bannerContent更新异常"),
    CATEGORY_ID_NOT_EXIST("850103","bannerContentId不存在"),

    GOODS_QUERY_EXCEPTION("860100","商品查询异常"),
    GOODS_SAVE_EXCEPTION("860101","商品保存异常"),
    GOODS_UPDATE_EXCEPTION("860102","商品更新异常"),
    GOODS_ID_NOT_EXIST("860103","商品Id不存在"),
    GOODS_CAN_NOT_BE_DOWNED("860104","商品不允许下架或删除"),

    GOODSRECOMMEND_QUERY_EXCEPTION("870100","推荐商品查询异常"),
    GOODSRECOMMEND_SAVE_EXCEPTION("860101","推荐商品保存异常"),
    GOODSRECOMMEND_UPDATE_EXCEPTION("860102","推荐商品更新异常"),
    GOODSRECOMMEND_ID_NOT_EXIST("860103","推荐商品Id不存在"),
    GOODSRECOMMEND_CAN_NOT_BE_DOWNED("860104","推荐商品不允许下架或删除"),

    LOGISTICS_QUERY_EXCEPTION("890100","物流信息查询异常"),
    LOGISTICS_SAVE_EXCEPTION("890101","订单保存异常"),
    LOGISTICS_DELETE_EXCEPTION("890103","订单删除异常"),
    LOGISTICS_UPDATE_EXCEPTION("890104","订单更新异常"),

    ORDER_QUERY_EXCEPTION("880100","订单查询异常"),
    ORDER_SAVE_EXCEPTION("880101","订单保存异常"),
    ORDER_DELETE_EXCEPTION("880103","订单删除异常"),
    ORDER_UPDATE_EXCEPTION("880104","订单更新异常"),
    ORDER_ENTITY_NOT_EXIST("880105","对应的订单不存在"),
    ORDER_ERROR_ILLEGAL_OPERATION("880106", "操作不合法"),

    QUANTITY_BEYOND_THE_LIMIT("",""),
    FAILED_INFORMATION_PROCESSING("880107","商品限购信息处理失败！"),
    WINNING_PROBABILITY_ILLEGAL("880108","中奖几率不合法"),

    SHOP_QUERY_EXCEPTION("890100","商铺查询异常"),
    SHOP_SAVE_EXCEPTION("890101","商铺保存异常"),
    SHOP_DELETE_EXCEPTION("890103","商铺删除异常"),
    SHOP_UPDATE_EXCEPTION("890104","商铺更新异常"),

    USER_BE_FORBIDDEN("900001","用户被禁用，请联系管理员"),
    USER_NOT_LOGIN("900002","用户未登录"),
    USER_NOT_HAVE_PERMISSION("900003","用户无此权限"),


    PARAM_CHECK_EXCEPTION("2000200","参数校验错误"),
    PARAM_ERROR_ILLEGAL("2000300", "参数不合法");
    private String code;
    private String message;

    ExceptionEnum(String code, String message){
        this.code = code;
        this.message = message;
    }


    /**
     *
     * @Title: getExceptionEnumByCode
     * @Description: 根据枚举编码获取枚举
     * @param @param name
     * @param @return
     * @return CommonExceptionEnum
     * @throws
     */
    public static ExceptionEnum getExceptionEnumByCode(String code){
        for(ExceptionEnum exceptionEnum : ExceptionEnum.values()){
            if(StringUtils.equals(exceptionEnum.getCode(), code)){
                return exceptionEnum;
            }
        }
        return null;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}

package org.trc.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.trc.enums.CommonExceptionEnum;
import org.trc.exception.ParamValidException;

import javax.ws.rs.QueryParam;

/**
 * Created by wangzhen
 */
public class QueryModel {

    @QueryParam("isValid")
    @Length(max = 2, message = "是否启用编码长度不能超过2个")
    private String isValid;
    @QueryParam("startTime")
    private Long startTime;
    @QueryParam("endTime")
    private Long endTime;

    /**
     * 排序方向变量
     */
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    /**
     * 多个排序字段分割符号
     */
    public static final String ORDER_SPLIT = ",";

    /**
     排序字段，多个用逗号","分割
     */
    @QueryParam("orderBy")
    protected String orderBy = null;
    /**
     排序方向，多个用逗号","分割。个数和顺序与排序字段orderBy个数和顺序对应
     */
    @QueryParam("order")
    protected String order = null;

    /**
     * 获得排序字段,无默认值. 多个排序字段时用','分隔.
     */
    public String getOrderBy() {
        checkOrder(this.orderBy, this.order);
        return orderBy;
    }

    /**
     * 获取排序字段数组
     * @return
     */
    public String[] getOrderBys(){
        checkOrder(this.orderBy, this.order);
        if(StringUtils.isNotEmpty(this.orderBy))
            return this.orderBy.split(ORDER_SPLIT);
        else
            return new String[0];
    }

    /**
     * 设置排序字段,多个排序字段时用','分隔.
     */
    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 返回Page对象自身的setOrderBy函数,可用于连续设置。
     */
    public QueryModel orderBy(final String theOrderBy) {
        if(StringUtils.isNotEmpty(this.orderBy))
            this.orderBy = this.orderBy + "," + theOrderBy;
        else
            this.orderBy = theOrderBy;
        return this;
    }

    /**
     * 获得排序方向, 无默认值.
     */
    public String getOrder() {
        return order;
    }

    /**
     * 获取排序方向数组
     * @return
     */
    public String[] getOrders(){
        if(StringUtils.isNotEmpty(this.order))
            return this.order.split(ORDER_SPLIT);
        else
            return new String[0];
    }

    /**
     * 设置排序方式向.
     *
     * @param order
     * 可选值为desc或asc,多个排序字段时用','分隔.
     */
    public void setOrder(final String order) {
        String lowcaseOrder = StringUtils.lowerCase(order);
        // 检查order字符串的合法值
        String[] orders = StringUtils.split(lowcaseOrder, ORDER_SPLIT);
        for (String orderStr : orders) {
            if (!StringUtils.equals(DESC, orderStr)
                    && !StringUtils.equals(ASC, orderStr)) {
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
            }
        }
        this.order = lowcaseOrder;
    }

    /**
     * 返回Page对象自身的setOrder函数,可用于连续设置。
     */
    public QueryModel order(final String theOrder) {
        if(StringUtils.isNotEmpty(this.order))
            this.order = this.order + "," + theOrder;
        else
            this.orderBy = theOrder;
        return this;
    }

    /**
     * 检查排序字段和排序方向是否相符和设置排序
     * 1、首先检查排序字段和排序方向是否都包含逗号","分割符号
     * 2、如果有逗号分割符号，那么在判断排序字段和排序方向个数是否相符
     * @param orderBy
     * @param order
     */
    private void checkOrder(String orderBy, String order){
        if(StringUtils.isNotEmpty(orderBy)) {
            if(StringUtils.contains(orderBy, ORDER_SPLIT)){
                String[] orderBySplit = orderBy.split(ORDER_SPLIT);
                String[] orderSplit = order.split(ORDER_SPLIT);
                if(orderBySplit != orderSplit)
                    throw new ParamValidException(CommonExceptionEnum.PARAM_CHECK_EXCEPTION,
                            CommonUtil.joinStr("分页查询排序字段[",orderBy,"]和排序方向[",order,"]不相符").toString());
            }
        }
    }

    public String getIsValid() {
        return isValid;
    }
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}

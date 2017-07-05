package org.trc.domain.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
public class SettlementQuery implements Serializable{

    private Long shopId;

    private String exchangeCurrency;

    private List<Integer> orderStates;

    private Integer orderState;

    private String userId;

    private String accountDay;

    private String phone;

    /**
     * 1实物订单;2虚拟订单
     */
    private Integer orderType;

    /**
     * 订单单号
     */
    private String orderNum;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     *  账单起始时间
     */
    private Date startTime;

    /**
     *  账单结束时间
     */
    private Date endTime;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getExchangeCurrency() {
        return exchangeCurrency;
    }

    public void setExchangeCurrency(String exchangeCurrency) {
        this.exchangeCurrency = exchangeCurrency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountDay() {
        return accountDay;
    }

    public void setAccountDay(String accountDay) {
        this.accountDay = accountDay;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public List<Integer> getOrderStates() {
        return orderStates;
    }

    public void setOrderStates(List<Integer> orderStates) {
        this.orderStates = orderStates;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SettlementQuery{" +
                "shopId=" + shopId +
                ", exchangeCurrency='" + exchangeCurrency + '\'' +
                ", orderStates=" + orderStates +
                ", orderState=" + orderState +
                ", userId='" + userId + '\'' +
                ", accountDay='" + accountDay + '\'' +
                ", phone='" + phone + '\'' +
                ", orderType=" + orderType +
                ", orderNum='" + orderNum + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

}

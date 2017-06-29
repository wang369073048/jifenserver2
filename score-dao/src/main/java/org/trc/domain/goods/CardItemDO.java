package org.trc.domain.goods;

import org.trc.domain.CommonDO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by george on 2017/4/11.
 */
@Table(name = "card_item")
public class CardItemDO extends CommonDO{

    /**
     * 批次号
     */
    @Column(name = "batchNumber")
    private String batchNumber;

    /**
     * 券码
     */
    private String code;

    /**
     * 状态(0:未发放，1:已发放)
     */
    private Integer state;

    @Column(name = "userId")
    private String userId;

    @Column(name = "orderCode")
    private String orderCode;

    /**
     * 发放时间
     */
    @Column(name = "releaseTime")
    private Date releaseTime;

    /**
     * 临时属性 数量
     */
    private Integer quantity;

    /**
     * 卡券名称 temporary property
     */
    @Column(name = "couponName")
    private String couponName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    @Override
    public String toString() {
        return "CardItemDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", batchNumber='" + batchNumber + '\'' +
                ", code='" + code + '\'' +
                ", state=" + state +
                ", userId='" + userId + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", createTime=" + createTime +
                ", releaseTime=" + releaseTime +
                ", quantity=" + quantity +
                ", couponName='" + couponName + '\'' +
                '}';
    }

}

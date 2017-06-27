package org.trc.domain.goods;

import org.trc.domain.CommonDO;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by george on 2017/4/11.
 */
public class CardCouponsDO extends CommonDO{

    /**
     * 卡券名称
     */
    @Column(name = "couponName")
    private String couponName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 批次号
     */
    @Column(name = "batchNumber")
    private String batchNumber;

    /**
     * 库存
     */
    private Integer stock = 0;

    /**
     * 创建时间
     */
    private Long version = 1l;

    /**
     * 状态(0:未删除的，1:被删除)
     */
    @Column(name = "isDeleted")
    private Integer isDeleted = 0;

    /**
     * 起始有效期
     */
    @Column(name = "validStartTime")
    private Date validStartTime;

    /**
     * 结束有效期
     */
    @Column(name = "validEndTime")
    private Date validEndTime;


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

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(Date validStartTime) {
        this.validStartTime = validStartTime;
    }

    public Date getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(Date validEndTime) {
        this.validEndTime = validEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CardCouponsDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", couponName='" + couponName + '\'' +
                ", remark='" + remark + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", stock=" + stock +
                ", version=" + version +
                ", isDeleted=" + isDeleted +
                ", validStartTime=" + validStartTime +
                ", validEndTime=" + validEndTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

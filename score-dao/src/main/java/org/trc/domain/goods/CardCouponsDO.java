package org.trc.domain.goods;

import org.trc.domain.CommonDO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/9.
 */
@Table(name = "card_coupons")
public class CardCouponsDO implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id; //主键
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
    @Column(name = "stock")
    private Integer stock;

    /**
     * 创建时间
     */
    @Column(name = "version")
    private Long version;

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

    /**
     *  业务方ID
     */
    @Column(name = "shopId")
    protected Long shopId;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    protected Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    protected Date updateTime;


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

package org.trc.domain.goods;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/9.
 */
@Table(name = "card_item_abandoned")
public class CardItemAbandonedDO implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id; //主键

    /**
     * 店铺id
     */
    @Column(name = "shopId")
    private Long shopId;

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
     * 状态(0:未发放)
     */
    private Integer state;

    @Column(name = "createTime")
    private Date createTime;

    /**
     * 废弃时间
     */
    @Column(name = "abandonedTime")
    private Date abandonedTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAbandonedTime() {
        return abandonedTime;
    }

    public void setAbandonedTime(Date abandonedTime) {
        this.abandonedTime = abandonedTime;
    }

    @Override
    public String toString() {
        return "CardItemAbandonedDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", batchNumber='" + batchNumber + '\'' +
                ", code='" + code + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", abandonedTime=" + abandonedTime +
                '}';
    }

}

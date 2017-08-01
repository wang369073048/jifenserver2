package org.trc.domain.goods;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "purchase_restrictions")
public class PurchaseRestrictionsDO implements Serializable{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * -1:表示不限购，否则为正整数
     */
    @Column(name = "limitQuantity")
    private Integer limitQuantity;

    /**
     * 商品id
     */
    @Column(name = "goodsId")
    private Long goodsId;

    /**
     * 限制起始时间
     */
    @Column(name = "limitStartTime")
    private Date limitStartTime;

    /**
     * 限购版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(Integer limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Date getLimitStartTime() {
        return limitStartTime;
    }

    public void setLimitStartTime(Date limitStartTime) {
        this.limitStartTime = limitStartTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
}

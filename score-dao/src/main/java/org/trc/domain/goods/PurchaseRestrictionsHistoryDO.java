package org.trc.domain.goods;

import java.io.Serializable;
import java.util.Date;


public class PurchaseRestrictionsHistoryDO implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * -1:表示不限购，否则为正整数
     */
    private Integer limitQuantity;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 限制起始时间
     */
    private Date limitStartTime;

    /**
     * 限购版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

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
}

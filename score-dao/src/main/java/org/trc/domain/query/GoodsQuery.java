package org.trc.domain.query;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by george on 2017/7/5.
 */
public class GoodsQuery implements Serializable{

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 是否上架,0上架,1下架
     */
    private Integer isUp;

    /**
     * 是否奖品
     */
    private Integer whetherPrizes;

    /**
     * 展示分类id
     */
    private Long classificationId;

    private Date operateTimeMin;

    private Date operateTimeMax;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getIsUp() {
        return isUp;
    }

    public void setIsUp(Integer isUp) {
        this.isUp = isUp;
    }

    public Integer getWhetherPrizes() {
        return whetherPrizes;
    }

    public void setWhetherPrizes(Integer whetherPrizes) {
        this.whetherPrizes = whetherPrizes;
    }

    public Long getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Long classificationId) {
        this.classificationId = classificationId;
    }

    public Date getOperateTimeMin() {
        return operateTimeMin;
    }

    public void setOperateTimeMin(Date operateTimeMin) {
        this.operateTimeMin = operateTimeMin;
    }

    public Date getOperateTimeMax() {
        return operateTimeMax;
    }

    public void setOperateTimeMax(Date operateTimeMax) {
        this.operateTimeMax = operateTimeMax;
    }

}

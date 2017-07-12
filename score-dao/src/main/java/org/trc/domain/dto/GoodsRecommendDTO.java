package org.trc.domain.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * author: XiongXin
 * description: 推荐商品视图数据类
 * time: 2016/12/13.
 */
public class GoodsRecommendDTO implements Serializable {

    /**
     * 推荐商品编号
     */
    private Long recommendId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 推荐商品编号
     */
    private Long category;
    /**
     * 商品主图
     */
    private String mainImg;
    /**
     * 虚拟卡券批次号
     */
    private String batchNumber;
    /**
     * 商品链接
     */
    private String targetUrl;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 积分市场价
     */
    private Integer priceMarket;
    /**
     * 积分兑换价
     */
    private Integer priceScore;
    /**
     * 有效开始日期
     */
    private Date validStartTime;

    /**
     * 有效结束日期
     */
    private Date validEndTime;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 商品Id
     */
    private String goodsId;
    /**
     * 兑换数量
     */
    private Integer exchangeQuantity;

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPriceMarket() {
        return priceMarket;
    }

    public void setPriceMarket(Integer priceMarket) {
        this.priceMarket = priceMarket;
    }

    public Integer getPriceScore() {
        return priceScore;
    }

    public void setPriceScore(Integer priceScore) {
        this.priceScore = priceScore;
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getExchangeQuantity() {
        return exchangeQuantity;
    }

    public void setExchangeQuantity(Integer exchangeQuantity) {
        this.exchangeQuantity = exchangeQuantity;
    }
}

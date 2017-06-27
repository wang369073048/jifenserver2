package org.trc.domain.goods;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.trc.domain.CommonDO;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/22.
 */
public class GoodsDO extends CommonDO{

    /**
     * 所属类目
     */
    private Long category;

    /**
     * 品牌名称
     */
    @Column(name = "brandName")
    private String brandName;

    /**
     * 商品名称
     */
    @Column(name = "goodsName")
    private String goodsName;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 商品货号
     */
    @Column(name = "goodsNo")
    private String goodsNo;

    /**
     * 虚拟商品批次号
     */
    @Column(name = "batchNumber")
    private String batchNumber;

    /**
     * 商品编号
     */
    @Column(name = "goodsSn")
    private String goodsSn;

    /**
     * 商品主图
     */
    @Column(name = "mainImg")
    private String mainImg;

    /**
     * 附属图片(json格式)
     */
    @Column(name = "mediumImg")
    private String mediumImg;

    /**
     * 市场价
     */
    @Column(name = "priceMarket")
    private Integer priceMarket;

    /**
     * 积分兑换价格
     */
    @Column(name = "priceScore")
    private Integer priceScore;

    /**
     * 商品链接
     */
    @Column(name = "targetUrl")
    private String targetUrl;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 警报库存
     */
    @Column(name = "stockWarn")
    private Integer stockWarn;

    /**
     * 兑换数量
     */
    @Column(name = "exchangeQuantity")
    private Integer exchangeQuantity = 0;

    /**
     * 虚拟兑换量
     */
    @Column(name = "virtualExchangeQuantity")
    private Integer virtualExchangeQuantity = 0;

    /**
     * 是否上架,0上架,1下架
     */
    @Column(name = "isUp")
    private Integer isUp;

    /**
     * 商品详情介绍
     */
    private String content;

    /**
     *
     */
    @Column(name = "versionLock")
    private Integer versionLock = 1;

    /**
     * 0 正常 ;1 已删除
     */
    @Column(name = "isDeleted")
    private Integer isDeleted;

    /**
     * 有效开始日期
     */
    @Column(name = "validStartTime")
    private Date validStartTime;

    /**
     * 有效结束日期
     */
    @Column(name = "validEndTime")
    private Date validEndTime;

    /**
     * 自动上架时间
     */
    @Column(name = "autoUpTime")
    private Date autoUpTime;

    /**
     * 自动下架时间
     */
    @Column(name = "autoDownTime")
    private Date autoDownTime;

    /**
     * 上架时间
     */
    @Column(name = "upTime")
    private Date upTime;

    /**
     * 快照时间
     */
    @Column(name = "snapshotTime")
    private Date snapshotTime;

    public void calMainImg(){
        if(StringUtils.isEmpty(this.mainImg)){
            if(StringUtils.hasText(this.mediumImg)){
                JSONArray array = JSONArray.parseArray(this.mediumImg);
                this.mainImg = ((JSONObject)array.get(0)).toJSONString();
            }
        }
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getMediumImg() {
        return mediumImg;
    }

    public void setMediumImg(String mediumImg) {
        this.mediumImg = mediumImg;
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

    public Integer getStockWarn() {
        return stockWarn;
    }

    public void setStockWarn(Integer stockWarn) {
        this.stockWarn = stockWarn;
    }

    public Integer getExchangeQuantity() {
        return exchangeQuantity;
    }

    public void setExchangeQuantity(Integer exchangeQuantity) {
        this.exchangeQuantity = exchangeQuantity;
    }

    public Integer getVirtualExchangeQuantity() {
        return virtualExchangeQuantity;
    }

    public void setVirtualExchangeQuantity(Integer virtualExchangeQuantity) {
        this.virtualExchangeQuantity = virtualExchangeQuantity;
    }

    public Integer getIsUp() {
        return isUp;
    }

    public void setIsUp(Integer isUp) {
        this.isUp = isUp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getVersionLock() {
        return versionLock;
    }

    public void setVersionLock(Integer versionLock) {
        this.versionLock = versionLock;
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

    public Date getAutoUpTime() {
        return autoUpTime;
    }

    public void setAutoUpTime(Date autoUpTime) {
        this.autoUpTime = autoUpTime;
    }

    public Date getAutoDownTime() {
        return autoDownTime;
    }

    public void setAutoDownTime(Date autoDownTime) {
        this.autoDownTime = autoDownTime;
    }

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public Date getSnapshotTime() {
        return snapshotTime;
    }

    public void setSnapshotTime(Date snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "GoodsDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", category=" + category +
                ", brandName='" + brandName + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", barcode='" + barcode + '\'' +
                ", goodsNo='" + goodsNo + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", goodsSn='" + goodsSn + '\'' +
                ", mainImg='" + mainImg + '\'' +
                ", mediumImg='" + mediumImg + '\'' +
                ", priceMarket=" + priceMarket +
                ", priceScore=" + priceScore +
                ", targetUrl='" + targetUrl + '\'' +
                ", stock=" + stock +
                ", stockWarn=" + stockWarn +
                ", exchangeQuantity=" + exchangeQuantity +
                ", virtualExchangeQuantity=" + virtualExchangeQuantity +
                ", isUp=" + isUp +
                ", content='" + content + '\'' +
                ", versionLock=" + versionLock +
                ", isDeleted=" + isDeleted +
                ", validStartTime=" + validStartTime +
                ", validEndTime=" + validEndTime +
                ", autoUpTime=" + autoUpTime +
                ", autoDownTime=" + autoDownTime +
                ", upTime=" + upTime +
                ", snapshotTime=" + snapshotTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

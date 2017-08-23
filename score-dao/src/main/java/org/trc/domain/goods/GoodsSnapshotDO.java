package org.trc.domain.goods;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "goods_snapshot")
public class GoodsSnapshotDO implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
    * 商品id
    */
	@Column(name = "goodsId")
	private Long goodsId;

	/**
	 * 店铺id
	 */
	@Column(name = "shopId")
	private Long shopId;

	/**
	 * 所属分类
	 */
	private Long category;

	/**
    * 商品版本 
    */
	private Integer version;

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
	 * 卡券外链
	 */
	@Column(name = "targetUrl")
	private String targetUrl;

	/**
    * 商品详情介绍
    */
	private String content;

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
    * 创建时间
    */
	@Column(name = "createTime")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
}

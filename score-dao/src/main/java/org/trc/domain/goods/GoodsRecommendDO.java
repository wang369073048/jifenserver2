package org.trc.domain.goods;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/9.
 */
@Table(name = "goods_recommend")
public class GoodsRecommendDO implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id; //主键

	/**
    * 店铺ID
    */
	@Column(name = "shopId")
	private Long shopId;

	/**
    * 商品ID
    */
	@Column(name = "goodsId")
	private Long goodsId;

	/**
    * 排序
    */
	private int sort;

	/**
    * 操作人ID
    */
	@Column(name = "operatorUserId")
	private String operatorUserId;

	/**
    * 0 正常 ;1 已删除
    */
	@Column(name = "isDeleted")
	private boolean isDeleted;

	/**
    * 创建时间
    */
	@Column(name = "createTime")
	private Date createTime;

	/**
    * 修改时间
    */
	@Column(name = "updateTime")
	private Date updateTime;


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

    public Long getGoodsId() {
    	return goodsId;
    }

    public void setGoodsId(Long goodsId) {
    	this.goodsId = goodsId;
    }

    public int getSort() {
    	return sort;
    }

    public void setSort(int sort) {
    	this.sort = sort;
    }

    public String getOperatorUserId() {
    	return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
    	this.operatorUserId = operatorUserId;
    }

    public boolean getIsDeleted() {
    	return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
    	this.isDeleted = isDeleted;
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

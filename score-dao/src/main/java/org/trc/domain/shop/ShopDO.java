package org.trc.domain.shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
@Table(name = "shop")
public class ShopDO implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 预警电话
	 */
	@Column(name = "warnPhone")
	private String warnPhone;
	
    /**
     * 所有者
     */
    private String phone;

	/**
     *  所有者userId
     */
    @Column(name = "userId")
    private String userId;

	/**
    * 店铺名称
    */
    @Column(name = "shopName")
	private String shopName;

	/**
    * 渠道编码
    */
    @Column(name = "channelCode")
	private String channelCode;

    /**
     * 店铺logo
     */
    private String logo;


	/**
    * 客服电话
    */
    @Column(name = "servicePhone")
	private String servicePhone;

	/**
    * 
    */
	private String description;

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
    
    public String getWarnPhone() {
		return warnPhone;
	}

	public void setWarnPhone(String warnPhone) {
		this.warnPhone = warnPhone;
	}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopName() {
    	return shopName;
    }

    public void setShopName(String shopName) {
    	this.shopName = shopName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getServicePhone() {
    	return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
    	this.servicePhone = servicePhone;
    }

    public String getDescription() {
    	return description;
    }

    public void setDescription(String description) {
    	this.description = description;
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

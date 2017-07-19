package org.trc.domain.order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments: AddressDO模型
 * since Date： 2017/7/3
 */
@Table(name = "address")
public class AddressDO implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
    * 主键ID
    */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
    * 用户ID
    */
	@Column(name = "userId")
	private String userId;

	/**
    * 省行政编码
    */
	@Column(name = "provinceCode")
	private String provinceCode;

	/**
	 * 非持久化属性，省
	 */
	private String province;

	/**
    * 城市行政编码
    */
	@Column(name = "cityCode")
	private String cityCode;

	/**
	 * 非持久化属性，市
	 */
	private String city;

	/**
    * 地区行政编码
    */
	@Column(name = "areaCode")
	private String areaCode;

	/**
	 * 非持久化属性，地区
	 */
	private String area;

	/**
    * 具体地址
    */
	private String address;

	/**
    * 收货人姓名
    */
	@Column(name = "receiverName")
	private String receiverName;

	/**
    * 联系人手机号
    */
	private String phone;

	/**
    * 邮编
    */
	private String postcode;

	/**
	 * 是否默认地址
	 */
	@Column(name = "isDefault")
	private boolean isDefault;

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

    public String getUserId() {
    	return userId;
    }

    public void setUserId(String userId) {
    	this.userId = userId;
    }

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAddress() {
    	return address;
    }

    public void setAddress(String address) {
    	this.address = address;
    }

    public String getReceiverName() {
    	return receiverName;
    }

    public void setReceiverName(String receiverName) {
    	this.receiverName = receiverName;
    }

    public String getPhone() {
    	return phone;
    }

    public void setPhone(String phone) {
    	this.phone = phone;
    }

    public String getPostcode() {
    	return postcode;
    }

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	public void setPostcode(String postcode) {
    	this.postcode = postcode;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "AddressDO{" +
				"id=" + id +
				", userId='" + userId + '\'' +
				", provinceCode='" + provinceCode + '\'' +
				", cityCode='" + cityCode + '\'' +
				", areaCode='" + areaCode + '\'' +
				", address='" + address + '\'' +
				", receiverName='" + receiverName + '\'' +
				", phone='" + phone + '\'' +
				", postcode='" + postcode + '\'' +
				", isDefault=" + isDefault +
				", isDeleted=" + isDeleted +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
package org.trc.domain.order;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
@Table(name = "orderAddressDO")
public class OrderAddressDO implements Serializable{

	/**
    * 主键ID
    */
	private Long id;

	/**
    * 
    */
	private Long orderId;

	/**
	 *
	 */
	private String orderNum;

	private String provinceCode;

	/**
	 * 非持久化属性，省
	 */
	private String province;

	/**
    * 城市
    */
	private String cityCode;

	/**
	 * 非持久化属性，市
	 */
	private String city;

	/**
    * 地区
    */
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
	private String receiverName;

	/**
    * 联系人手机号
    */
	private String phone;

	/**
    * 邮编
    */
	private String postcode;

	public OrderAddressDO() {
	}

	public OrderAddressDO(Long orderId) {
		this.orderId = orderId;
	}

    public OrderAddressDO(AddressDO addressDO) {
		this.provinceCode = addressDO.getProvinceCode();
		this.cityCode = addressDO.getCityCode();
		this.areaCode = addressDO.getAreaCode();
		this.address = addressDO.getAddress();
		this.receiverName = addressDO.getReceiverName();
		this.phone = addressDO.getPhone();
		this.postcode = addressDO.getPostcode();
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }

    public Long getOrderId() {
    	return orderId;
    }

    public void setOrderId(Long orderId) {
    	this.orderId = orderId;
    }

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
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

    public void setPostcode(String postcode) {
    	this.postcode = postcode;
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
}
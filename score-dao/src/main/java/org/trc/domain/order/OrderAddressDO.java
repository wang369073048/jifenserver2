package org.trc.domain.order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
@Table(name = "order_address")
public class OrderAddressDO implements Serializable{

	/**
    * 主键ID
    */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
    * 
    */
	@Column(name = "orderId")
	private Long orderId;

	/**
	 *非持久化属性
	 */
	@Transient
	private String orderNum;

	@Column(name = "provinceCode")
	private String provinceCode;

	/**
	 * 非持久化属性，省
	 */
	@Transient
	private String province;

	/**
    * 城市
    */
	@Column(name = "cityCode")
	private String cityCode;

	/**
	 * 非持久化属性，市
	 */
	@Transient
	private String city;

	/**
    * 地区
    */
	@Column(name = "areaCode")
	private String areaCode;

	/**
	 * 非持久化属性，地区
	 */
	@Transient
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
    @Column(name = "createTime")
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

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
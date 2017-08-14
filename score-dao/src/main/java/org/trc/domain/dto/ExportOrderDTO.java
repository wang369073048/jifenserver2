package org.trc.domain.dto;


import org.trc.constants.TemporaryContext;

import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
public class ExportOrderDTO implements Serializable {

    private Date orderDate;

    private String orderNum;

    private Long shopId;

    private String username;

    private String barCode;

    private String goodsName;

    private Integer goodsCount;

    private String goodsNo;

    private Long orderId;

    private String receiverName;

    private String phone;

    private String province;

    private String city;

    private String area;

    private String address;
    
    private Integer orderState;

    /**
     * 物流公司名称
     */
    private String companyName;

    /**
     * 物流单号
     */
    private String logisticsNum;
    
    private Integer price;

    private Integer payment;

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLogisticsNum() {
		return logisticsNum;
	}

	public void setLogisticsNum(String logisticsNum) {
		this.logisticsNum = logisticsNum;
	}

	public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public String getShopName(){
        if (null!=this.shopId){
            return TemporaryContext.getShopNameById(this.shopId);
        }else{
            return "";
        }
    }

    @Override
    public String toString() {
        return "ExportOrderDTO{" +
                "orderDate=" + orderDate +
                ", orderNum='" + orderNum + '\'' +
                ", shopId=" + shopId +
                ", username='" + username + '\'' +
                ", barCode='" + barCode + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsCount=" + goodsCount +
                ", goodsNo='" + goodsNo + '\'' +
                ", orderId=" + orderId +
                ", receiverName='" + receiverName + '\'' +
                ", phone='" + phone + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", companyName='" + companyName + '\'' +
                ", logisticsNum='" + logisticsNum + '\'' +
                ", price=" + price +
                ", payment=" + payment +
                '}';
    }
}

package org.trc.domain.dto;

import org.trc.domain.order.OrdersDO;


/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
public class OrderDTO extends OrdersDO{

    /**
     * 商品id
     */
    public Long goodsId;

    /**
     *  用户id
     */
    public String userId;

    /**
     *  商家userId
     */
    public String sellerUserId;

    /**
     * 用户姓名
     */
    public String username;

    /**
     * 商家姓名
     */
    public String sellerUsername;
    
    /**
     * 用户头像
     */
    public String avatar;

    /**
     * 商品数量
     */
    public Integer quantity;

    /**
     * 单价
     */
    public Integer price;

    /**
     * 实际付款价格
     */
    public Integer payment;

    /**
     * 1未发货(已兑换);2已发货;3已完成(已收货);4已取消
     */
    public Integer orderState;

    /**
     *  收货地址Id
     */
    public Long addressId;

    public String address;

    public String receiverName;

    public String receiverPhone;

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

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
    @Override
    public String toString() {
        return "OrderDTO{" +
                "goodsId=" + goodsId +
                ", userId='" + userId + '\'' +
                ", sellerUserId='" + sellerUserId + '\'' +
                ", username='" + username + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", payment=" + payment +
                ", orderState=" + orderState +
                ", addressId=" + addressId +
                '}';
    }

}

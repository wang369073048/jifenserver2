package org.trc.domain.dto;


import java.io.Serializable;
import java.util.Date;


/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
public class OrderDTO implements Serializable{

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 非持久化属性，店铺名称
     */
    private String shopName;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 商品货号
     */
    private String goodsNo;

    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品version
     */
    private Integer goodsVersion;

    /**
     *
     */
    private Integer goodsCount;

    /**
     *
     */
    private String minImg;


    /**
     * 1实物订单;2虚拟订单
     */
    private Integer orderType;

    /**
     * 1-积分兑换；2-积分抽奖
     */
    private Integer source;

    /**
     * 物流费用
     */
    private Integer freight;

    /**
     *
     */
    private Integer versionLock;

    /**
     * 0 正常 ;1 已删除
     */
    private boolean isDeleted;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 确认收货时间
     */
    private Date confirmTime;

    /**
     * 退款时间
     */
    private Date returnTime;

    private Integer scoreCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     *  操作时间最小值 非持久化属性
     */
    private Date operateTimeMin;

    /**
     *  操作时间最大值 非持久化属性
     */
    private Date operateTimeMax;

    /**
     * 卡券编码，tab键分隔，临时属性
     */
    private String couponCode;

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
     * 1未发货(已兑换);2已发货;3已完成(已收货);4已取消;5已退款
     */
    public Integer orderState;

    /**
     *  收货地址Id
     */
    public Long addressId;

    public String address;

    public String receiverName;

    public String receiverPhone;

    public Integer getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(Integer scoreCount) {
        this.scoreCount = scoreCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsVersion() {
        return goodsVersion;
    }

    public void setGoodsVersion(Integer goodsVersion) {
        this.goodsVersion = goodsVersion;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getFreight() {
        return freight;
    }

    public void setFreight(Integer freight) {
        this.freight = freight;
    }

    public Integer getVersionLock() {
        return versionLock;
    }

    public void setVersionLock(Integer versionLock) {
        this.versionLock = versionLock;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
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

    public Date getOperateTimeMin() {
        return operateTimeMin;
    }

    public void setOperateTimeMin(Date operateTimeMin) {
        this.operateTimeMin = operateTimeMin;
    }

    public Date getOperateTimeMax() {
        return operateTimeMax;
    }

    public void setOperateTimeMax(Date operateTimeMax) {
        this.operateTimeMax = operateTimeMax;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
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

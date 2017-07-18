package org.trc.domain.order;

import org.apache.ibatis.annotations.Param;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
@Table(name = "orders")
public class OrdersDO implements Serializable{
    @Transient
    public OrderAddressDO orderAddressDO;
    @Transient
    public LogisticsDO logisticsDO;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单编号
     */
    @Column(name = "orderNum")
    private String orderNum;

    /**
     * 店铺id
     */
    @Column(name = "shopId")
    private Long shopId;

    /**
     * 非持久化属性，店铺名称
     */
    @Column(name = "shopName")
    private String shopName;

    /**
     * 商品Id
     */
    @Column(name = "goodsId")
    private Long goodsId;

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
     * 商品名称
     */
    @Column(name = "goodsName")
    private String goodsName;
    /**
     * 商品version
     */
    @Column(name = "goodsVersion")
    private Integer goodsVersion;

    /**
     *
     */
    @Column(name = "goodsCount")
    private Integer goodsCount;

    /**
     *
     */
    @Column(name = "minImg")
    private String minImg;

    /**
     *
     */
    @Column(name = "userId")
    private String userId;

    /**
     *
     */
    private String username;

    /**
     * 1未发货(已兑换);2已发货;3已完成(已收货);4已取消
     */
    @Column(name = "orderState")
    private Integer orderState;

    /**
     * 1实物订单;2虚拟订单
     */
    @Column(name = "orderType")
    private Integer orderType;

    /**
     * 1-积分兑换；2-积分抽奖
     */
    private Integer source;

    /**
     * 单价
     */
    private Integer price;

    /**
     * 实际付款价格
     */
    private Integer payment;

    /**
     * 物流费用
     */
    private Integer freight;

    /**
     *
     */
    @Column(name = "versionLock")
    private Integer versionLock;

    /**
     * 0 正常 ;1 已删除
     */
    @Column(name = "isDeleted")
    private boolean isDeleted;

    /**
     * 发货时间
     */
    @Column(name = "deliveryTime")
    private Date deliveryTime;

    /**
     * 确认收货时间
     */
    @Column(name = "confirmTime")
    private Date confirmTime;

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

    /**
     *  操作时间最小值 非持久化属性
     */
    @Transient
    private Date operateTimeMin;

    /**
     *  操作时间最大值 非持久化属性
     */
    @Transient
    private Date operateTimeMax;

    /**
     * 卡券编码，tab键分隔，临时属性
     */
    @Transient
    private String couponCode;

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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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

    public Integer getGoodsCount() {
        return goodsCount;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getPayment() {
        return payment;
    }

    public Integer getFreight() {
        return freight;
    }

    public void setFreight(Integer freight) {
        this.freight = freight;
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

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getMinImg() {
        return minImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
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

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
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

    public OrderAddressDO getOrderAddressDO() {
        return orderAddressDO;
    }

    public void setOrderAddressDO(OrderAddressDO orderAddressDO) {
        this.orderAddressDO = orderAddressDO;
    }

    public LogisticsDO getLogisticsDO() {
        return logisticsDO;
    }

    public void setLogisticsDO(LogisticsDO logisticsDO) {
        this.logisticsDO = logisticsDO;
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

    @Override
    public String toString() {
        return "OrdersDO{" +
                "orderAddressDO=" + orderAddressDO +
                ", logisticsDO=" + logisticsDO +
                ", id=" + id +
                ", orderNum='" + orderNum + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", goodsId=" + goodsId +
                ", barcode='" + barcode + '\'' +
                ", goodsNo='" + goodsNo + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsVersion=" + goodsVersion +
                ", goodsCount=" + goodsCount +
                ", minImg='" + minImg + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", orderState=" + orderState +
                ", orderType=" + orderType +
                ", source=" + source +
                ", price=" + price +
                ", payment=" + payment +
                ", freight=" + freight +
                ", versionLock=" + versionLock +
                ", isDeleted=" + isDeleted +
                ", deliveryTime=" + deliveryTime +
                ", confirmTime=" + confirmTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operateTimeMin=" + operateTimeMin +
                ", operateTimeMax=" + operateTimeMax +
                ", couponCode='" + couponCode + '\'' +
                '}';
    }
}

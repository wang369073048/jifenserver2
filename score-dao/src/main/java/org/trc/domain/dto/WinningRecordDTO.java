package org.trc.domain.dto;

import org.trc.constants.LuckyDraw;
import org.trc.domain.order.LogisticsDO;
import org.trc.domain.order.OrderAddressDO;
import org.trc.domain.query.DateQuery;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by george on 2017/6/1.
 */
public class WinningRecordDTO extends DateQuery implements Serializable{

    private Long id;

    /**
     * 店铺id
     */
    private Long shopId;

    private String userId;

    /**
     * 抽奖活动id
     */
    private Long luckyDrawId;

    /**
     * 中奖奖品id
     */
    private Long activityPrizeId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 平台:PC|APP
     */
    private String platform;

    /**
     * 抽奖时间
     */
    private Date drawTime;

    /**
     * 抽奖手机号
     */
    private String lotteryPhone;

    /**
     * 是否免费抽奖:0-非免费 1-免费
     */
    private Integer whetherFree;

    /**
     * 积分消费
     */
    private Integer expenditure;

    /**
     * 请求编号
     */
    private String requestNo;

    /**
     * 奖品名称
     */
    private String prizeName;

    /**
     * 奖品类型
     */
    private String prizeType;

    /**
     * 单次中奖奖品数量
     */
    private Integer numberOfPrizes;

    private Long goodsId;

    /**
     * 商品货号
     */
    private String goodsNo;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 中奖手机号
     */
    private String winningPhone;

    /**
     * 商品类型
     */
    private String goodsType;

    /**
     * 奖品状态:0-未发放1-已发放2-发放失败
     */
    private Integer state;

    private String phone;

    private String province;

    private String city;

    private String area;

    private String address;

    private String remark;

    /**
     * 收货地址,实物奖品可能用到
     */
    private OrderAddressDO orderAddress;

    /**
     * 物流信息，实物奖品可能用到
     */
    private LogisticsDO logisticsDO;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getLuckyDrawId() {
        return luckyDrawId;
    }

    public void setLuckyDrawId(Long luckyDrawId) {
        this.luckyDrawId = luckyDrawId;
    }

    public Long getActivityPrizeId() {
        return activityPrizeId;
    }

    public void setActivityPrizeId(Long activityPrizeId) {
        this.activityPrizeId = activityPrizeId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformDesc() {
        if("PC".equals(this.platform)){
            return "PC端";
        }else if("APP".equals(this.platform)){
            return "移动端";
        }else{
            return "异常";
        }
    }

    public Date getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Date drawTime) {
        this.drawTime = drawTime;
    }

    public String getLotteryPhone() {
        return lotteryPhone;
    }

    public void setLotteryPhone(String lotteryPhone) {
        this.lotteryPhone = lotteryPhone;
    }

    public Integer getWhetherFree() {
        return whetherFree;
    }

    public void setWhetherFree(Integer whetherFree) {
        this.whetherFree = whetherFree;
    }

    public Integer getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Integer expenditure) {
        this.expenditure = expenditure;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public Integer getNumberOfPrizes() {
        return numberOfPrizes;
    }

    public void setNumberOfPrizes(Integer numberOfPrizes) {
        this.numberOfPrizes = numberOfPrizes;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getWinningPhone() {
        return winningPhone;
    }

    public void setWinningPhone(String winningPhone) {
        this.winningPhone = winningPhone;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeDesc() {
        if(LuckyDraw.GoodsType.VIRTUAL.equals(this.goodsType)){
            return "虚拟";
        }else if(LuckyDraw.GoodsType.MATERIAL.equals(this.goodsType)){
            return "实物";
        }else{
            return null;
        }
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateDesc() {
        if(0==this.state.intValue()){
            return "未发放";
        }else if(1==this.state.intValue()){
            return "已发放";
        }else if(2==this.state.intValue()){
            return "发放失败";
        }else{
            return "异常";
        }
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public OrderAddressDO getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(OrderAddressDO orderAddress) {
        this.orderAddress = orderAddress;
        if(null != orderAddress){
            this.province = orderAddress.getProvince();
            this.city = orderAddress.getCity();
            this.area = orderAddress.getArea();
            this.phone = orderAddress.getPhone();
            this.address = orderAddress.getAddress();
        }
    }

    public LogisticsDO getLogisticsDO() {
        return logisticsDO;
    }

    public void setLogisticsDO(LogisticsDO logisticsDO) {
        this.logisticsDO = logisticsDO;
    }

    @Override
    public String toString() {
        return "WinningRecordDTO{" +
                "id=" + id +
                ",shopId=" + shopId +
                ", userId='" + userId + '\'' +
                ", luckyDrawId=" + luckyDrawId +
                ", activityPrizeId=" + activityPrizeId +
                ", activityName='" + activityName + '\'' +
                ", platform='" + platform + '\'' +
                ", drawTime=" + drawTime +
                ", lotteryPhone='" + lotteryPhone + '\'' +
                ", whetherFree=" + whetherFree +
                ", expenditure=" + expenditure +
                ", requestNo='" + requestNo + '\'' +
                ", prizeName='" + prizeName + '\'' +
                ", prizeType='" + prizeType + '\'' +
                ", numberOfPrizes=" + numberOfPrizes +
                ", goodsId=" + goodsId +
                ", goodsNo='" + goodsNo + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", winningPhone='" + winningPhone + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", state=" + state +
                '}';
    }
}

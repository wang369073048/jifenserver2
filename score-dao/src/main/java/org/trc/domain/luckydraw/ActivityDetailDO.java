package org.trc.domain.luckydraw;

import org.trc.constants.LuckyDraw;
import org.trc.domain.query.DateQuery;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class ActivityDetailDO extends DateQuery implements Serializable{

    /**
     * 主键 winningRecordId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 奖品id
     */
    private Long activityPrizeId;

    /**
     * 店铺id
     */
    private Long shopId;

    private String userId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 抽奖活动id
     */
    private Long luckyDrawId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 平台:PC|APP
     */
    private String platform;

    /**
     * 抽奖日期,格式:YYYY-MM-DD
     */
    private String drawDate;

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
     * 是否中奖:0-未中奖 1-中奖
     */
    private Integer whetherWinning;

    /**
     * 奖品名称
     */
    private String prizeName;

    /**
     * 单次中奖奖品数量
     */
    private Integer numberOfPrizes;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品货号
     */
    private String goodsNo;

    /**
     *  商品类型
     */
    private String goodsType;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 中奖手机号
     */
    private String winningPhone;

    /**
     * 奖品类型
     */
    private String prizeType;

    /**
     * 奖品状态:0-未发放1-已发放2-发放失败
     */
    private Integer state;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityPrizeId() {
        return activityPrizeId;
    }

    public void setActivityPrizeId(Long activityPrizeId) {
        this.activityPrizeId = activityPrizeId;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getLuckyDrawId() {
        return luckyDrawId;
    }

    public void setLuckyDrawId(Long luckyDrawId) {
        this.luckyDrawId = luckyDrawId;
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

    public String getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(String drawDate) {
        this.drawDate = drawDate;
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

    public Integer getWhetherWinning() {
        return whetherWinning;
    }

    public void setWhetherWinning(Integer whetherWinning) {
        this.whetherWinning = whetherWinning;
    }

    public String getWhetherWinningDesc(){
        if(null == this.whetherWinning || 0 == this.whetherWinning){
            return "未中奖";
        }else if(1 == this.whetherWinning){
            return "已中奖";
        }else{
            return "异常";
        }
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

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeDesc(){
        if(LuckyDraw.GoodsType.VIRTUAL.equals(this.goodsType)){
            return "虚拟";
        }else if(LuckyDraw.GoodsType.MATERIAL.equals(this.goodsType)){
            return "实物";
        }else{
            return null;
        }
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

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ActivityDetailDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", userId='" + userId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", luckyDrawId=" + luckyDrawId +
                ", activityName='" + activityName + '\'' +
                ", platform='" + platform + '\'' +
                ", drawDate='" + drawDate + '\'' +
                ", drawTime=" + drawTime +
                ", lotteryPhone='" + lotteryPhone + '\'' +
                ", whetherFree=" + whetherFree +
                ", expenditure=" + expenditure +
                ", requestNo='" + requestNo + '\'' +
                ", prizeName='" + prizeName + '\'' +
                ", numberOfPrizes=" + numberOfPrizes +
                ", goodsId='" + goodsId + '\'' +
                ", goodsNo='" + goodsNo + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", winningPhone='" + winningPhone + '\'' +
                ", prizeType=" + prizeType +
                ", state=" + state +
                ", updateTime=" + updateTime +
                '}';
    }
}

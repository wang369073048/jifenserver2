package org.trc.domain.luckydraw;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
@Table(name = "winning_record")
public class WinningRecordDO implements Serializable{

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 店铺id
     */
    @Column(name = "shopId")
    private Long shopId;
    @Column(name = "userId")
    private String userId;

    /**
     * 抽奖活动id
     */
    @Column(name = "luckyDrawId")
    private Long luckyDrawId;

    /**
     * 中奖奖品id
     */
    @Column(name = "activityPrizeId")
    private Long activityPrizeId;

    /**
     * 活动名称
     */
    @Column(name = "activityName")
    private String activityName;

    /**
     * 平台:PC|APP
     */
    @Column(name = "platform")
    private String platform;

    /**
     * 抽奖日期,格式:YYYY-MM-DD
     */
    @Column(name = "drawDate")
    private String drawDate;

    /**
     * 抽奖时间
     */
    @Column(name = "drawTime")
    private Date drawTime;

    /**
     * 抽奖手机号
     */
    @Column(name = "lotteryPhone")
    private String lotteryPhone;

    /**
     * 是否免费抽奖:0-非免费 1-免费
     */
    @Column(name = "whetherFree")
    private Integer whetherFree;

    /**
     * 积分消费
     */
    @Column(name = "expenditure")
    private Integer expenditure;

    /**
     * 请求编号
     */
    @Column(name = "requestNo")
    private String requestNo;

    /**
     * 奖品名称
     */
    @Column(name = "prizeName")
    private String prizeName;

    /**
     * 商品类型  1虚拟，2实物
     */
    @Column(name = "goodsType")
    private String goodsType;

    /**
     * 单次中奖奖品数量
     */
    @Column(name = "numberOfPrizes")
    private Integer numberOfPrizes;
    @Column(name = "goodsId")
    private Long goodsId;

    /**
     * 商品货号
     */
    @Column(name = "goodsNo")
    private String goodsNo;

    /**
     * 订单编号
     */
    @Column(name = "orderNum")
    private String orderNum;

    /**
     * 中奖手机号
     */
    @Column(name = "winningPhone")
    private String winningPhone;

    /**
     * 奖品类型
     */
    @Column(name = "prizeType")
    private String prizeType;

    /**
     * 奖品状态:0-未发放1-已发放2-发放失败
     */
    private Integer state;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

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

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "WinningRecordDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", userId='" + userId + '\'' +
                ", luckyDrawId=" + luckyDrawId +
                ", activityPrizeId=" + activityPrizeId +
                ", activityName='" + activityName + '\'' +
                ", platform='" + platform + '\'' +
                ", drawDate='" + drawDate + '\'' +
                ", drawTime=" + drawTime +
                ", lotteryPhone='" + lotteryPhone + '\'' +
                ", whetherFree=" + whetherFree +
                ", expenditure=" + expenditure +
                ", requestNo='" + requestNo + '\'' +
                ", prizeName='" + prizeName + '\'' +
                ", goodsType='" + goodsType + '\'' +

                ", numberOfPrizes=" + numberOfPrizes +
                ", goodsId=" + goodsId +
                ", goodsNo='" + goodsNo + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", winningPhone='" + winningPhone + '\'' +
                ", prizeType='" + prizeType + '\'' +
                ", state=" + state +
                ", version=" + version +
                ", updateTime=" + updateTime +
                '}';
    }
}

package org.trc.domain.goods;

import java.io.Serializable;
import java.util.Date;


public class ParticipationRecordDO implements Serializable{

    /**
     * 主键
     */
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

    public Integer getWhetherWinning() {
        return whetherWinning;
    }

    public void setWhetherWinning(Integer whetherWinning) {
        this.whetherWinning = whetherWinning;
    }

    @Override
    public String toString() {
        return "ParticipationRecordDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", userId='" + userId + '\'' +
                ", luckyDrawId=" + luckyDrawId +
                ", activityName='" + activityName + '\'' +
                ", platform='" + platform + '\'' +
                ", drawDate='" + drawDate + '\'' +
                ", drawTime=" + drawTime +
                ", lotteryPhone='" + lotteryPhone + '\'' +
                ", whetherFree=" + whetherFree +
                ", expenditure=" + expenditure +
                ", requestNo='" + requestNo + '\'' +
                ", whetherWinning=" + whetherWinning +
                '}';
    }
}

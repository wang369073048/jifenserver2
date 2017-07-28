package org.trc.domain.luckydraw;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
@Table(name = "lucky_draw")
public class LuckyDrawDO implements Serializable {

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

    /**
     * 平台:PC|APP
     */
    private String platform;

    /**
     * 活动名称
     */
    @Column(name = "activityName")
    private String activityName;

    /**
     * 开始时间
     */
    @Column(name = "startTime")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "endTime")
    private Date endTime;

    /**
     * 免费抽奖次数
     */
    @Column(name = "freeLotteryTimes")
    private Integer freeLotteryTimes;

    /**
     * 免费抽奖类型:PER_DAY每天,THE_WHOLE_ACTIVITY全程
     */
    @Column(name = "freeDrawType")
    private String freeDrawType;

    /**
     * 积分消费
     */
    private Integer expenditure;

    /**
     * 每日抽奖次数限制
     */
    @Column(name = "dailyDrawLimit")
    private Integer dailyDrawLimit;

    /**
     * 客户端背景图片
     */
    @Column(name = "appBackground")
    private String appBackground;

    /**
     * pc端背景图片
     */
    @Column(name = "webBackground")
    private String webBackground;

    /**
     * 是否删除：0未删除，1已删除
     */
    @Column(name = "isDeleted")
    private Integer isDeleted;

    /**
     * 活动规则
     */
    @Column(name = "activityRules")
    private String activityRules;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

    /**
     * 活动奖品
     */
    @Transient
    private List<ActivityPrizesDO> activityPrizesList;

    /**
     * 活动状态
     */
    @Transient
    private Integer state;
    @Transient
    private Date operateTimeMin;          //操作时间最小值
    @Transient
    private Date operateTimeMax;           //操作时间最大值

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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getFreeLotteryTimes() {
        return freeLotteryTimes;
    }

    public void setFreeLotteryTimes(Integer freeLotteryTimes) {
        this.freeLotteryTimes = freeLotteryTimes;
    }

    public String getFreeDrawType() {
        return freeDrawType;
    }

    public void setFreeDrawType(String freeDrawType) {
        this.freeDrawType = freeDrawType;
    }

    public Integer getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Integer expenditure) {
        this.expenditure = expenditure;
    }

    public Integer getDailyDrawLimit() {
        return dailyDrawLimit;
    }

    public void setDailyDrawLimit(Integer dailyDrawLimit) {
        this.dailyDrawLimit = dailyDrawLimit;
    }

    public String getAppBackground() {
        return appBackground;
    }

    public void setAppBackground(String appBackground) {
        this.appBackground = appBackground;
    }

    public String getWebBackground() {
        return webBackground;
    }

    public void setWebBackground(String webBackground) {
        this.webBackground = webBackground;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getActivityRules() {
        return activityRules;
    }

    public void setActivityRules(String activityRules) {
        this.activityRules = activityRules;
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

    public List<ActivityPrizesDO> getActivityPrizesList() {
        return activityPrizesList;
    }

    public void setActivityPrizesList(List<ActivityPrizesDO> activityPrizesList) {
        this.activityPrizesList = activityPrizesList;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    @Override
    public String toString() {
        return "LuckyDrawDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", platform='" + platform + '\'' +
                ", activityName='" + activityName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", freeLotteryTimes=" + freeLotteryTimes +
                ", freeDrawType='" + freeDrawType + '\'' +
                ", expenditure=" + expenditure +
                ", dailyDrawLimit=" + dailyDrawLimit +
                ", appBackground='" + appBackground + '\'' +
                ", webBackground='" + webBackground + '\'' +
                ", isDeleted=" + isDeleted +
                ", activityRules='" + activityRules + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", state=" + state +
                ", operateTimeMin=" + operateTimeMin +
                ", operateTimeMax=" + operateTimeMax +
                '}';
    }
}

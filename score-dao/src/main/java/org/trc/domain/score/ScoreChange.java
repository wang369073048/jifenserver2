package org.trc.domain.score;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 积分变更表
 */
@Table(name = "score_change")
public class ScoreChange implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                       //主键id;
    @Column(name = "userId")
    private String userId;                //唯一用户标示
    @Column(name = "userName")
    private String userName;             //用户名
    @Column(name = "theOtherUserId")
    private String theOtherUserId;       //对方用户标示
    @Column(name = "theOtherUserName")
    private String theOtherUserName;             //对方名
    @Column(name = "scoreId")
    private Long scoreId;                 //积分账户id
    @Column(name = "foreignCurrency")
    private Long foreignCurrency;        //外币变更数量

    private Long score;                   //积分变更数量
    @Column(name = "scoreBalance")
    private Long scoreBalance;           //积分余额
    @Column(name = "freezingScoreBalance")
    private Long freezingScoreBalance;  //冻结积分余额
    @Column(name = "orderCode")
    private String orderCode;            //单据编码
    @Column(name = "shopId")
    private Long shopId;                //店铺Id
    @Column(name = "channelCode")
    private String channelCode;          //渠道编码
    @Column(name = "businessCode")
    private String businessCode;         //业务编码
    @Column(name = "flowType")
    private String flowType;             //变更类型(income:收入，expenditure:支出)
    @Column(name = "exchangeCurrency")
    private String exchangeCurrency;    //外币

    private String remark;               //备注
    @Column(name = "operationTime")
    private Date operationTime;         //操作时间
    @Column(name = "expirationTime")
    private Date expirationTime;        //过期时间
    @Column(name = "createTime")
    private Date createTime;               //创建时间

    public ScoreChange() {

    }

    public ScoreChange(String userId, String userName, String theOtherUserId, String theOtherUserName, Long scoreId, Long score, Long scoreBalance, Long freezingScoreBalance,
                       String orderCode, Long shopId, String channelCode, String businessCode, String flowType, String remark,
                       String exchangeCurrency, Date expirationTime, Date createTime) {
        this.userId = userId;
        this.userName = userName;
        this.theOtherUserId = theOtherUserId;
        this.theOtherUserName = theOtherUserName;
        this.scoreId = scoreId;
        this.score = score;
        this.scoreBalance = scoreBalance;
        this.freezingScoreBalance = freezingScoreBalance;
        this.orderCode = orderCode;
        this.shopId = shopId;
        this.channelCode = channelCode;
        this.businessCode = businessCode;
        this.flowType = flowType;
        this.remark = remark;
        this.exchangeCurrency = exchangeCurrency;
        this.expirationTime = expirationTime;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTheOtherUserId() {
        return theOtherUserId;
    }

    public void setTheOtherUserId(String theOtherUserId) {
        this.theOtherUserId = theOtherUserId;
    }

    public String getTheOtherUserName() {
        return theOtherUserName;
    }

    public void setTheOtherUserName(String theOtherUserName) {
        this.theOtherUserName = theOtherUserName;
    }

    public Long getScoreId() {
        return scoreId;
    }

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

    public Long getForeignCurrency() {
        return foreignCurrency;
    }

    public void setForeignCurrency(Long foreignCurrency) {
        this.foreignCurrency = foreignCurrency;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getScoreBalance() {
        return scoreBalance;
    }

    public void setScoreBalance(Long scoreBalance) {
        this.scoreBalance = scoreBalance;
    }

    public Long getFreezingScoreBalance() {
        return freezingScoreBalance;
    }

    public void setFreezingScoreBalance(Long freezingScoreBalance) {
        this.freezingScoreBalance = freezingScoreBalance;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExchangeCurrency() {
        return exchangeCurrency;
    }

    public void setExchangeCurrency(String exchangeCurrency) {
        this.exchangeCurrency = exchangeCurrency;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ScoreChange{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", theOtherUserId='" + theOtherUserId + '\'' +
                ", scoreId=" + scoreId +
                ", score=" + score +
                ", scoreBalance=" + scoreBalance +
                ", freezingScoreBalance=" + freezingScoreBalance +
                ", orderCode='" + orderCode + '\'' +
                ", shopId=" + shopId +
                ", channelCode='" + channelCode + '\'' +
                ", businessCode='" + businessCode + '\'' +
                ", flowType='" + flowType + '\'' +
                ", exchangeCurrency='" + exchangeCurrency + '\'' +
                ", remark='" + remark + '\'' +
                ", operationTime=" + operationTime +
                ", expirationTime=" + expirationTime +
                ", createTime=" + createTime +
                '}';
    }

}

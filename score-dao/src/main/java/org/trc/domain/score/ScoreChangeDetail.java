package org.trc.domain.score;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangzhen
 */
@Table(name = "score_change_detail")
public class ScoreChangeDetail implements Serializable {

    private static final long serialVersionUID = -8458923171021735734L;

    private Long id;                   //主键id

    private String userId;            //唯一用户标示

    private String orderCode;        //单据编码

    private Long scoreId;            //积分账户id

    private Long scoreChildId;       //积分子账户id

    private Long score;              //变更积分

    private Long scoreBalance;      //积分余额

    private Long freezingScoreBalance;  //冻结积分余额

    private String flowType;        //变更类型

    private Date expirationTime;   //过期时间

    private Date createTime;         //创建时间

    public ScoreChangeDetail() {

    }

    public ScoreChangeDetail(String userId, String orderCode, Long scoreId, Long scoreChildId, Long score,
                             Long scoreBalance, Long freezingScoreBalance, String flowType, Date expirationTime, Date createTime) {
        this.userId = userId;
        this.orderCode = orderCode;
        this.scoreId = scoreId;
        this.scoreChildId = scoreChildId;
        this.score = score;
        this.scoreBalance = scoreBalance;
        this.freezingScoreBalance = freezingScoreBalance;
        this.flowType = flowType;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getScoreId() {
        return scoreId;
    }

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

    public Long getScoreChildId() {
        return scoreChildId;
    }

    public void setScoreChildId(Long scoreChildId) {
        this.scoreChildId = scoreChildId;
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

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
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
        return "ScoreChangeDetail{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", scoreId=" + scoreId +
                ", scoreChildId=" + scoreChildId +
                ", score=" + score +
                ", scoreBalance=" + scoreBalance +
                ", freezingScoreBalance=" + freezingScoreBalance +
                ", flowType='" + flowType + '\'' +
                ", expirationTime=" + expirationTime +
                ", createTime=" + createTime +
                '}';
    }
}

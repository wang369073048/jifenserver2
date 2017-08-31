package org.trc.domain.score;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/8
 */
@Table(name = "score")
public class Score implements Serializable {

    private static final long serialVersionUID = -4534921822828807204L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    //主键id;
    @Column(name = "userType")
    private String userType;               //用户类型
    @Column(name = "userId")
    private String userId;              //用户id;
    @Column(name = "source")
    private String source;              //来源渠道
    @Column(name = "previousScore")
    private Long previousScore;         //前一日积分余额
    @Column(name = "score")
    private Long score;                 //积分
    @Column(name = "previousFreezingScore")
    private Long previousFreezingScore; //前一日积分冻结余额
    @Column(name = "freezingScore")
    private Long freezingScore;        //冻结积分
    @Column(name = "accumulativeScore")
    private Long accumulativeScore;    //累计获得积分
    @Column(name = "version")
    private Long version;               //版本号，并发乐观锁，初始版本1，更新+1
    @Column(name = "createTime")
    private Date createTime;             //创建时间
    @Column(name = "updateTime")
    private Date updateTime;            //修改时间

    public Score() {

    }

    public Score(String userId, String userType, Long score, String channelCode, Date createTime) {
        this.userId = userId;
        this.userType = userType;
        this.score = score;
        this.freezingScore = 0l;
        this.accumulativeScore = score;
        this.source = channelCode;
        this.version = 1l;
        this.createTime = createTime;
        this.updateTime = createTime;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getFreezingScore() {
        return freezingScore;
    }

    public void setFreezingScore(Long freezingScore) {
        this.freezingScore = freezingScore;
    }

    public Long getAccumulativeScore() {
        return accumulativeScore;
    }

    public void setAccumulativeScore(Long accumulativeScore) {
        this.accumulativeScore = accumulativeScore;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    public Long getPreviousScore() {
        return previousScore;
    }

    public void setPreviousScore(Long previousScore) {
        this.previousScore = previousScore;
    }

    public Long getPreviousFreezingScore() {
        return previousFreezingScore;
    }

    public void setPreviousFreezingScore(Long previousFreezingScore) {
        this.previousFreezingScore = previousFreezingScore;
    }

    public void addScore(Long addScore) {
        if (this.score != null) {
            this.score += addScore;
        }
        if (this.accumulativeScore != null) {
            this.accumulativeScore += addScore;
        }
    }

    public void consume(long consumeScore) {
        if (null != this.score && this.score >= consumeScore) {
            this.score -= consumeScore;
        }
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "Score [id=" + id + ", userType=" + userType + ", userId=" + userId + ", source=" + source
                + ", previousScore=" + previousScore + ", score=" + score + ", previousFreezingScore="
                + previousFreezingScore + ", freezingScore=" + freezingScore + ", accumulativeScore="
                + accumulativeScore + ", version=" + version + ", createTime=" + createTime + ", updateTime="
                + updateTime + "]";
    }

    public static enum ScoreUserType {
        SELLER,
        BUYER
    }

}

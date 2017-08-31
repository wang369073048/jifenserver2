package org.trc.domain.score;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by george on 2016/12/29.
 */
@Table(name = "score_settlement")
public class ScoreSettlement implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "scoreId")
    private Long scoreId;
	@Column(name = "dailyBalance")
    private Long dailyBalance;
	@Column(name = "accountDay")
    private String accountDay;
	@Column(name = "createTime")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScoreId() {
        return scoreId;
    }

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

    public Long getDailyBalance() {
        return dailyBalance;
    }

    public void setDailyBalance(Long dailyBalance) {
        this.dailyBalance = dailyBalance;
    }

    public String getAccountDay() {
        return accountDay;
    }

    public void setAccountDay(String accountDay) {
        this.accountDay = accountDay;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}

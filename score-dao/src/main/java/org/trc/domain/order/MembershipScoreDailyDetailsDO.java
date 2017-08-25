package org.trc.domain.order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "membership_score_daily_details")
public class MembershipScoreDailyDetailsDO implements Serializable {


	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
	
	@Column(name = "userId")
    private String userId;
	
	@Transient
    private String userPhone;
	
	@Column(name = "accountDay")
    private String accountDay;

	@Column(name = "exchangeInNum")
    private Long exchangeInNum;

	@Column(name = "consumeNum")
    private Long consumeNum;
	
	@Column(name = "lotteryConsumeNum")
    private Long lotteryConsumeNum;//add by xab 抽奖消费积分
	
	@Column(name = "consumeCorrectNum")
    private Long consumeCorrectNum;//add by xab 消费冲正，也就是退积分

	@Column(name = "balance")
    private Long  balance;

	@Column(name = "createTime")
    private Date createTime;

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

    public String getAccountDay() {
        return accountDay;
    }

    public void setAccountDay(String accountDay) {
        this.accountDay = accountDay;
    }

    public Long getExchangeInNum() {
        return exchangeInNum;
    }

    public void setExchangeInNum(Long exchangeInNum) {
        this.exchangeInNum = exchangeInNum;
    }

    public Long getConsumeNum() {
        return consumeNum;
    }

    public void setConsumeNum(Long consumeNum) {
        this.consumeNum = consumeNum;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getLotteryConsumeNum() {
		return lotteryConsumeNum;
	}

	public void setLotteryConsumeNum(Long lotteryConsumeNum) {
		this.lotteryConsumeNum = lotteryConsumeNum;
	}

	public Long getConsumeCorrectNum() {
		return consumeCorrectNum;
	}

	public void setConsumeCorrectNum(Long consumeCorrectNum) {
		this.consumeCorrectNum = consumeCorrectNum;
	}
	
	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	@Override
    public String toString() {
        return "MembershipDcoreDailyDetailsDO{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", accountDay='" + accountDay + '\'' +
                ", exchangeInNum=" + exchangeInNum +
                ", consumeNum=" + consumeNum +
                ", lotteryConsumeNum=" + lotteryConsumeNum +
                ", consumeCorrectNum=" + consumeCorrectNum +
                ", balance=" + balance +
                ", createTime=" + createTime +
                '}';
    }
}

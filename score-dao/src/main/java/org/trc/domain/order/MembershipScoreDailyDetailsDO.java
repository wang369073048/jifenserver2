package org.trc.domain.order;

import java.io.Serializable;
import java.util.Date;

public class MembershipScoreDailyDetailsDO implements Serializable {

    private Long    id;

    private String  userId;

    private String  accountDay;

    private Long    exchangeInNum;

    private Long    consumeNum;

    private Long    balance;

    private Date    createTime;

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

    @Override
    public String toString() {
        return "MembershipDcoreDailyDetailsDO{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", accountDay='" + accountDay + '\'' +
                ", exchangeInNum=" + exchangeInNum +
                ", consumeNum=" + consumeNum +
                ", balance=" + balance +
                ", createTime=" + createTime +
                '}';
    }
}

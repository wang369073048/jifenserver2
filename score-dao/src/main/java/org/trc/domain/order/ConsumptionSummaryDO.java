package org.trc.domain.order;


import org.trc.constants.TemporaryContext;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name = "consumption_summary")
public class ConsumptionSummaryDO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "userId")
    private String userId;
    @Column(name = "accountDay")
    private String accountDay;
    @Column(name = "exchangeCurrency")
    private String exchangeCurrency;
    @Column(name = "shopId")
    private Long shopId;
    private String phone;
    @Column(name = "exchangeInNum")
    private Long exchangeInNum;
    @Column(name = "consumeNum")
    private Long consumeNum;
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

    public String getExchangeCurrency() {
        return exchangeCurrency;
    }

    public void setExchangeCurrency(String exchangeCurrency) {
        this.exchangeCurrency = exchangeCurrency;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        if(null==this.shopId){
            return null;
        }
        return TemporaryContext.getShopNameById(this.shopId);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ConsumptionSummaryDO{" +
                "id=" + id +
                ", userId=" + userId +
                ", accountDay='" + accountDay + '\'' +
                ", exchangeCurrency='" + exchangeCurrency + '\'' +
                ", shopId=" + shopId +
                ", phone='" + phone + '\'' +
                ", exchangeInNum=" + exchangeInNum +
                ", consumeNum=" + consumeNum +
                ", createTime=" + createTime +
                '}';
    }
}

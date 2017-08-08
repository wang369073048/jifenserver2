package org.trc.domain.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wamgzhen.
 */
public class ScoreReq implements Serializable {

    private String expenditureAccount;              //支出账户userId

    private String expenditureAccountName;         //支出账户用户名userName

    private String incomeAccount;                   //收入账户userId

    private String incomeAccountName;              //收入账户username

    private Long shopId;                            //收入店铺Id

    private String flowType;                        //变更类型(income:收入，expenditure:支出,freeze:冻结)(兑入业务需用此字段)

    private String channelCode;                     //渠道编码
    
    private String businessCode;                    //业务编码

    private String orderCode;                       //单据编码，全局唯一，建议带上系统前缀以及时间属性

    private Long amount;                            //兑换金额(兑入业务需用此字段)

    private Long score;                             //积分

    private Date operateTime;                       //时间戳取毫秒

    private String exchangeCurrency;                //兑换币种

    private Date expirationTime;                    //过期时间(兑入业务需用此字段)

    private String remark;                          //备注

    public String getExpenditureAccount() {
        return expenditureAccount;
    }

    public void setExpenditureAccount(String expenditureAccount) {
        this.expenditureAccount = expenditureAccount;
    }

    public String getExpenditureAccountName() {
        return expenditureAccountName;
    }

    public void setExpenditureAccountName(String expenditureAccountName) {
        this.expenditureAccountName = expenditureAccountName;
    }

    public String getIncomeAccount() {
        return incomeAccount;
    }

    public void setIncomeAccount(String incomeAccount) {
        this.incomeAccount = incomeAccount;
    }

    public String getIncomeAccountName() {
        return incomeAccountName;
    }

    public void setIncomeAccountName(String incomeAccountName) {
        this.incomeAccountName = incomeAccountName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
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

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Date getScoreExpirationTime(){
        if(null==this.expirationTime){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,2030);
            calendar.set(Calendar.MONTH,0);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            return calendar.getTime();
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.expirationTime);
            Calendar expirationTime = (Calendar) calendar.clone();
            expirationTime.add(Calendar.MONTH,1);
            expirationTime.set(Calendar.DAY_OF_MONTH,1);
            expirationTime.set(Calendar.HOUR_OF_DAY,0);
            expirationTime.set(Calendar.MINUTE,0);
            expirationTime.set(Calendar.SECOND,0);
            expirationTime.set(Calendar.MILLISECOND,0);
            return expirationTime.getTime();
        }
    }

}

package org.trc.domain.dto;

import org.trc.constants.ScoreCst;
import org.trc.constants.TemporaryContext;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by george on 2017/3/11.
 */
public class FlowDTO implements Serializable {

    private Date serialDate;

    private String serialNum;

    private String exchangeCurrency;

    private Long foreignCurrency;

    private Long score;

    private String phone;

    public Date getSerialDate() {
        return serialDate;
    }

    public void setSerialDate(Date serialDate) {
        this.serialDate = serialDate;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getExchangeCurrency() {
        if(null==this.exchangeCurrency){
            return null;
        }else {
            ScoreCst.ExchangeCurrency ec = Enum.valueOf(ScoreCst.ExchangeCurrency.class, this.exchangeCurrency);
            return ec.getValue();
        }
    }

    public void setExchangeCurrency(String exchangeCurrency) {
        this.exchangeCurrency = exchangeCurrency;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShopName(){
        if (null!=this.exchangeCurrency){
            return TemporaryContext.getShopNameByExchangeCurrency(this.exchangeCurrency);
        }else{
            return "";
        }
    }

    @Override
    public String toString() {
        return "FlowDTO{" +
                "serialDate=" + serialDate +
                ", serialNum='" + serialNum + '\'' +
                ", exchangeCurrency='" + exchangeCurrency + '\'' +
                ", foreignCurrency=" + foreignCurrency +
                ", score=" + score +
                ", phone='" + phone + '\'' +
                '}';
    }
}

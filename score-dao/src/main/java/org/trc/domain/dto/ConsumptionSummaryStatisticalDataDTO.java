package org.trc.domain.dto;

import java.io.Serializable;


public class ConsumptionSummaryStatisticalDataDTO implements Serializable{

    private Integer totalExchangeCount;

    private Long exchangeNum;

    private Integer totalConsumptionCount;

    private Long consumptionNum;

    public Integer getTotalExchangeCount() {
        return totalExchangeCount;
    }

    public void setTotalExchangeCount(Integer totalExchangeCount) {
        this.totalExchangeCount = totalExchangeCount;
    }

    public Long getExchangeNum() {
        return exchangeNum;
    }

    public void setExchangeNum(Long exchangeNum) {
        this.exchangeNum = exchangeNum;
    }

    public Integer getTotalConsumptionCount() {
        return totalConsumptionCount;
    }

    public void setTotalConsumptionCount(Integer totalConsumptionCount) {
        this.totalConsumptionCount = totalConsumptionCount;
    }

    public Long getConsumptionNum() {
        return consumptionNum;
    }

    public void setConsumptionNum(Long consumptionNum) {
        this.consumptionNum = consumptionNum;
    }

    @Override
    public String toString() {
        return "ConsumptionSummaryStatisticalDataDTO{" +
                "totalExchangeCount=" + totalExchangeCount +
                ", exchangeNum=" + exchangeNum +
                ", totalConsumptionCount=" + totalConsumptionCount +
                ", consumptionNum=" + consumptionNum +
                '}';
    }
}

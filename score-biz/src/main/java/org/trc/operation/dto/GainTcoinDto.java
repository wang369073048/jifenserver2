package org.trc.operation.dto;

import java.io.Serializable;

/**
 * Created by george on 2017/7/19.
 */
public class GainTcoinDto implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 发放T币数量
     */
    private Long amount;

    /**
     * 请求流水号
     */
    private String requestNo;

    /**
     * 中奖类型
     */
    private String winningType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getWinningType() {
        return winningType;
    }

    public void setWinningType(String winningType) {
        this.winningType = winningType;
    }

}

package org.trc.domain.settlement;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by george on 2016/12/28.
 */
public class SettlementDO implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 所属店铺id
     */
    private Long shopId;

    /**
     * 余额
     */
    private Long balance;

    /**
     * 前一日余额
     */
    private Long previousBalance;

    /**
     * 会计日
     */
    private String accountDay;

    /**
     * 订单单据号
     */
    private String billNum;

    /**
     * 所属店铺
     */
    private String shopName;

    /**
     * 订单数量
     */
    private Integer quantity;

    /**
     * 合计金额
     */
    private Long totalAmount;

    /**
     * 总运费
     */
    private Long totalFreight;

    /**
     *  账单起始时间
     */
    private Date startTime;

    /**
     *  账单结束时间
     */
    private Date endTime;

    /**
     * 订单结算时间
     */
    private Date settlementTime;

    /**
     * 结算状态
     */
    private Integer settlementState;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalFreight() {
        return totalFreight;
    }

    public void setTotalFreight(Long totalFreight) {
        this.totalFreight = totalFreight;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(Date settlementTime) {
        this.settlementTime = settlementTime;
    }

    public Integer getSettlementState() {
        return settlementState;
    }

    public void setSettlementState(Integer settlementState) {
        this.settlementState = settlementState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(Long previousBalance) {
        this.previousBalance = previousBalance;
    }

    public String getAccountDay() {
        return accountDay;
    }

    public void setAccountDay(String accountDay) {
        this.accountDay = accountDay;
    }
}

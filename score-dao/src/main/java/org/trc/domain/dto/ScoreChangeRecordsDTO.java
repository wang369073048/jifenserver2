package org.trc.domain.dto;

import java.io.Serializable;
import java.util.Date;

import org.trc.domain.score.ScoreChange;
/**
 * 
 * @Description: 用户积分流水明细
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月23日 下午8:05:52
 */
public class ScoreChangeRecordsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String userId;//唯一用户标示
	
	public String userName;//会员名称
	
	public String shopName; //店铺名称
	
	public String orderCode;//订单编号
	
    public String userPhone; //手机号
    
    public String businessCode;//业务编码
    
    public String businessName;//业务名称
    
    public Long score;//积分变更数量
    
    public Date operationTime;//操作时间
    
    public String remark;//备注
    
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
    public String toString() {
        return "ScoreChangeDetailsDTO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", shopName='" + shopName + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", businessCode=" + businessCode +
                ", businessName=" + businessName +
                ", score=" + score +
                ", operationTime=" + operationTime +
                ", remark=" + remark +
                '}';
    }

}

package org.trc.domain.score;

import java.io.Serializable;
import java.util.Date;

public class ScoreExchangeFlow implements Serializable {

	private static final long serialVersionUID = 3187644787901451851L;

	// 主键
	private Long id;

	private Long amount;

	private Long score;

	private String requestNo;

	private String direction;

	private String operatorUserId;

	private Date createTime;

	private Date updateTime;

	private String result;

	public ScoreExchangeFlow(){
		super();
	}
	
	public ScoreExchangeFlow(Long amount, Long score, String requestNo, String direction, String operatorUserId,
			String result) {
		super();
		this.amount = amount;
		this.score = score;
		this.requestNo = requestNo;
		this.direction = direction;
		this.operatorUserId = operatorUserId;
		this.createTime = new Date();
		this.updateTime = new Date();
		this.result = result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getOperatorUserId() {
		return operatorUserId;
	}

	public void setOperatorUserId(String operatorUserId) {
		this.operatorUserId = operatorUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ScoreExchangeFlow{" +
				"id=" + id +
				", amount=" + amount +
				", score=" + score +
				", requestNo='" + requestNo + '\'' +
				", direction='" + direction + '\'' +
				", operatorUserId='" + operatorUserId + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", result='" + result + '\'' +
				'}';
	}

	public static enum FlowType {
		IN, // 兑入
		OUT // 兑出
	}
	
	public static enum FlowResultType {
		INITIAL,//	初始
		LACK_OF_EXCHANGE_AMOUNT,// 额度不足
		FOREIGN_OPERATION_FAILURE,// 外币兑换失败
		SOCKET_TIME_OUT,//	响应超时
		SCORE_OPERATION_FAILED,// 外币扣除成功，积分增加失败
		CORRECTION_FAILED, // 冲正成功，兑换失败
		ERROR, // 错误，需人工检查
		SUCCESS // 成功
	}
}

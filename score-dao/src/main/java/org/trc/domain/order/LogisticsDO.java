package org.trc.domain.order;

import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
public class LogisticsDO implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
    * 主键ID
    */
	private Long id;

	/**
    * 
    */
	private Long orderId;

	/**
    * 物流公司名称
    */
	private String companyName;

	/**
	 * 物流公司编码
	 */
	private String shipperCode;

	/**
    * 物流单号
    */
	private String logisticsNum;

	/**
	 * 物流费用
	 */
	private Integer freight;

	/**
    * 操作人账户
    */
	private String operatorUserId;

	/**
    * 操作人姓名
    */
	private String operatorName;

	/**
    * 创建时间
    */
	private Date createTime;

	/**
    * 修改时间
    */
	private Date updateTime;

	public LogisticsDO() {
	}

	public LogisticsDO(Long orderId) {
		this.orderId = orderId;
	}

	public LogisticsDO(Long orderId, String companyName, String shipperCode, String logisticsNum, Integer freight, String operatorUserId, String
			operatorName) {
		this.orderId = orderId;
		this.companyName = companyName;
		this.shipperCode = shipperCode;
		this.logisticsNum = logisticsNum;
		this.freight = freight;
		this.operatorUserId = operatorUserId;
		this.operatorName = operatorName;
	}

	public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }

    public Long getOrderId() {
    	return orderId;
    }

    public void setOrderId(Long orderId) {
    	this.orderId = orderId;
    }

    public String getCompanyName() {
    	return companyName;
    }

    public void setCompanyName(String companyName) {
    	this.companyName = companyName;
    }

	public String getShipperCode() {
		return shipperCode;
	}

	public void setShipperCode(String shipperCode) {
		this.shipperCode = shipperCode;
	}

	public String getLogisticsNum() {
    	return logisticsNum;
    }

    public void setLogisticsNum(String logisticsNum) {
    	this.logisticsNum = logisticsNum;
    }

	public Integer getFreight() {
		return freight;
	}

	public void setFreight(Integer freight) {
		this.freight = freight;
	}

	public String getOperatorUserId() {
    	return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
    	this.operatorUserId = operatorUserId;
    }

    public String getOperatorName() {
    	return operatorName;
    }

    public void setOperatorName(String operatorName) {
    	this.operatorName = operatorName;
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
}
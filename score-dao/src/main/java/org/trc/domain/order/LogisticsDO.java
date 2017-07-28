package org.trc.domain.order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
@Table(name = "logistics")
public class LogisticsDO implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
    * 
    */
	@Column(name = "orderId")
	private Long orderId;

	/**
    * 物流公司名称
    */
	@Column(name = "companyName")
	private String companyName;

	/**
	 * 物流公司编码
	 */
	@Column(name = "shipperCode")
	private String shipperCode;

	/**
    * 物流单号
    */
	@Column(name = "logisticsNum")
	private String logisticsNum;

	/**
	 * 物流费用
	 */
	private Integer freight;

	/**
    * 操作人账户
    */
	@Column(name = "operatorUserId")
	private String operatorUserId;

	/**
    * 操作人姓名
    */
	@Column(name = "operatorName")
	private String operatorName;

	/**
    * 创建时间
    */
	@Column(name = "createTime")
	private Date createTime;

	/**
    * 修改时间
    */
	@Column(name = "updateTime")
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
package org.trc.domain.order;

import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
public class OrderLocusDO implements Serializable{

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
    * 变更前状态
    */
	private Integer beforeStatus;

	/**
    * 变更后状态
    */
	private Integer afterStatus;

	/**
    * 创建时间
    */
	private Date createTime;

	public OrderLocusDO() {
	}

	public OrderLocusDO(Long orderId, Integer beforeStatus, Integer afterStatus, Date createTime) {
		this.orderId = orderId;
		this.beforeStatus = beforeStatus;
		this.afterStatus = afterStatus;
		this.createTime = createTime;
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

	public Integer getBeforeStatus() {
		return beforeStatus;
	}

	public void setBeforeStatus(Integer beforeStatus) {
		this.beforeStatus = beforeStatus;
	}

	public Integer getAfterStatus() {
		return afterStatus;
	}

	public void setAfterStatus(Integer afterStatus) {
		this.afterStatus = afterStatus;
	}

	public Date getCreateTime() {
    	return createTime;
    }

    public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }
}
package org.trc.domain.order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/3
 */
@Table(name = "order_locus")
public class OrderLocusDO implements Serializable{

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
    * 变更前状态
    */
	@Column(name = "beforeStatus")
	private Integer beforeStatus;

	/**
    * 变更后状态
    */
	@Column(name = "afterStatus")
	private Integer afterStatus;

	/**
    * 创建时间
    */
	@Column(name = "createTime")
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
package org.trc.domain.order;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
@Table(name = "orders_extend")
public class OrdersExtendDO implements Serializable {

    @Column(name = "orderId")
    private Long orderId;
    @Column(name = "orderNum")
    private String orderNum;
    private String remark;
    @Column(name = "returnTime")
    private Date returnTime;
    @Column(name = "createTime")
    private Date createTime;
    @Column(name = "updateTime")
    private Date updateTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
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

    @Override
    public String toString() {
        return "OrdersExtendDO{" +
                "orderId=" + orderId +
                ", orderNum='" + orderNum + '\'' +
                ", remark='" + remark + '\'' +
                ", returnTime=" + returnTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}

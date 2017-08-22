package org.trc.operation.dto;

import java.io.Serializable;

/**
 * Created by george on 2017/8/14.
 */
public class GainTairanCardDto implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 兑换优惠券事件id
     */
    private Integer eid;

    /**
     * 活动别名
     */
    private String alias;

    /**
     * 请求流水号
     */
    private String requestNo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    @Override
    public String toString() {
        return "GainTairanCardDto{" +
                "userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                ", eid=" + eid +
                ", alias='" + alias + '\'' +
                ", requestNo='" + requestNo + '\'' +
                '}';
    }
}

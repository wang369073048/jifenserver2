package org.trc.operation.dto;

import java.io.Serializable;

/**
 * Created by george on 2017/7/19.
 */
public class GainFinancialCardDto implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 兑换卡券事件id
     */
    private String eid;

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

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

}

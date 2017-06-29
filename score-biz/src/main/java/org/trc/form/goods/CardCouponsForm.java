package org.trc.form.goods;

import org.trc.util.QueryModel;

import javax.ws.rs.QueryParam;

/**
 * Created by hzwzhen on 2017/6/28.
 */
public class CardCouponsForm extends QueryModel{

    @QueryParam("couponName")
    private String couponName;
    @QueryParam("batchNumber")
    private String batchNumber;
    @QueryParam("shopId")
    private Long shopId;

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}

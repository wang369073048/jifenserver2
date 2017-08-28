package org.trc.domain.dto;


import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreChangeRecordQueryDTO implements Serializable {

    private static final long serialVersionUID = -7730875460302652742L;

    private String userId;                //用户id;

    private String channelCode;         //渠道号

    private Long shopId;                //店铺id
    
    private String theOtherUserId;                //对方用户ID

    private List<String> businessCodeList;          //业务编码

    private String exchangeCurrency;      //兑换币种

    private String orderCode;             //单据编码，全局唯一，建议带上系统前缀以及时间属性

    private String flowType;              //变更类型(income:收入，expenditure:支出,freeze:冻结)

    private Long scoreMin;                //最小兑换积分数

    private Long scoreMax;                //最大兑换积分数

    private Date operateTimeMin;          //操作时间最小值

    private Date operateTimeMax;           //操作时间最大值

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public List<String> getBusinessCodeList() {
        return businessCodeList;
    }

    public void setBusinessCodeList(List<String> businessCodeList) {
        this.businessCodeList = businessCodeList;
    }

    public String getExchangeCurrency() {
        return exchangeCurrency;
    }

    public void setExchangeCurrency(String exchangeCurrency) {
        this.exchangeCurrency = exchangeCurrency;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public Long getScoreMin() {
        return scoreMin;
    }

    public void setScoreMin(Long scoreMin) {
        this.scoreMin = scoreMin;
    }

    public Long getScoreMax() {
        return scoreMax;
    }

    public void setScoreMax(Long scoreMax) {
        this.scoreMax = scoreMax;
    }

    public Date getOperateTimeMin() {
        return operateTimeMin;
    }

    public void setOperateTimeMin(Date operateTimeMin) {
        this.operateTimeMin = operateTimeMin;
    }

    public Date getOperateTimeMax() {
        return operateTimeMax;
    }

    public void setOperateTimeMax(Date operateTimeMax) {
        this.operateTimeMax = operateTimeMax;
    }
    
    public String getTheOtherUserId() {
		return theOtherUserId;
	}

	public void setTheOtherUserId(String theOtherUserId) {
		this.theOtherUserId = theOtherUserId;
	}

	public void calBusinessCodes(String businessCodes){
        if (StringUtils.isNotEmpty(businessCodes)) {
            String[] busiArray = businessCodes.split(",");
            if (null != busiArray && busiArray.length > 0) {
                List<String> businessCodeList = new ArrayList<>();
                for (String businessCode : busiArray) {
                    businessCodeList.add(businessCode);
                }
                this.setBusinessCodeList(businessCodeList);
            }
        }
    }
}

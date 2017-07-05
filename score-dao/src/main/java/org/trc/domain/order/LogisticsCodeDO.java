package org.trc.domain.order;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by george on 2017/1/7.
 */
public class LogisticsCodeDO implements Serializable{

    private Long id;

    /**
     * 快递公司编码
     */
    private String companyCode;

    /**
     * 快递公司名称
     */
    private String companyName;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

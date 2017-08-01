package org.trc.domain.goods;

import org.trc.domain.CommonDO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Table(name = "category")
public class CategoryDO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id; //主键
    @Column(name = "operatorUserId")
    protected String operatorUserId; //操作人ID

    @Column(name = "createTime")
    protected Date createTime; //创建时间
    @Column(name = "updateTime")
    protected Date updateTime; //更新时间

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 类目编码
     */
    private String code;

    /**
     * 类目名称
     */
    @Column(name = "categoryName")
    private String categoryName;

    /**
     *
     */
    @Column(name = "isVirtual")
    private Integer isVirtual;

    /**
     * 是否允许修改
     */
    @Column(name = "allowUpdates")
    private boolean allowUpdates;

    /**
     * LOGO地址
     */
    @Column(name = "logoUrl")
    private String logoUrl;

    /**
     * 排序
     */
    private int sort;

    /**
     * 备注
     */
    private String description;

    /**
     * 0 正常 ;1 已删除
     */
    @Column(name = "isDeleted")
    private boolean isDeleted;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Integer isVirtual) {
        this.isVirtual = isVirtual;
    }

    public boolean isAllowUpdates() {
        return allowUpdates;
    }

    public void setAllowUpdates(boolean allowUpdates) {
        this.allowUpdates = allowUpdates;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "CategoryDO{" +
                "id=" + id +
                ", pid=" + pid +
                ", categoryName='" + categoryName + '\'' +
                ", allowUpdates=" + allowUpdates +
                ", logoUrl='" + logoUrl + '\'' +
                ", sort=" + sort +
                ", description='" + description + '\'' +
                ", operatorUserId='" + operatorUserId + '\'' +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

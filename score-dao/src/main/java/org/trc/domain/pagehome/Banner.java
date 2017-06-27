package org.trc.domain.pagehome;

import org.trc.domain.CommonDO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/9.
 * banner管理
 */
@Table(name = "banner")
public class Banner extends CommonDO {

    /**
     * 名称
     */
    private String name;

    /**
     * 内容ID
     */
    @Column(name = "contentId")
    private Long contentId;

    /**
     * 是否上架,0上架,1下架
     */
    @Column(name = "isUp")
    private Boolean isUp;

    /**
     * 排序
     */
    private int sort;

    /**
     * 0 正常 ;1 已删除
     */
    @Column(name = "isDeleted")
    private boolean isDeleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Boolean getIsUp() {
        return isUp;
    }

    public void setIsUp(Boolean isUp) {
        this.isUp = isUp;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "name='" + name + '\'' +
                ", contentId=" + contentId +
                ", isUp=" + isUp +
                ", sort=" + sort +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", operatorUserId='" + operatorUserId + '\'' +
                ", shopId=" + shopId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

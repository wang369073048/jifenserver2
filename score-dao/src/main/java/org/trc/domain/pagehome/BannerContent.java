package org.trc.domain.pagehome;

import org.trc.domain.CommonDO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/12.
 */
@Table(name = "banner_content")
public class BannerContent extends CommonDO{


    /**
     * 标题
     */
    private String title;

    /**
     * 图片url
     */
    @Column(name = "imgUrl")
    private String imgUrl;

    /**
     * 跳转url
     */
    @Column(name = "targetUrl")
    private String targetUrl;

    /**
     * 0 正常 ;1 已删除
     */
    @Column(name = "isDeleted")
    private boolean isDeleted;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "BannerContent{" +
                "title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
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

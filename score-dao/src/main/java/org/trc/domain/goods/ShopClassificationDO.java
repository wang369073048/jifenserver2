package org.trc.domain.goods;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by george on 2017/7/4.
 */
@Table(name = "shop_classification")
public class ShopClassificationDO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 冗余字段
     */
    private String uuid;

    /**
     * 店铺id
     */
    @Column(name = "shopId")
    private Long shopId;

    /**
     * 名称
     */
    @Column(name = "classificationName")
    private String classificationName;

    /**
     * 图片
     */
    @Column(name = "pictureUrl")
    private String pictureUrl;

    /**
     * 排序：介于0-100的整数，默认为10
     */
    private Integer sort;

    /**
     * 备注
     */
    private String description;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "ShopClassificationDO{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", shopId=" + shopId +
                ", classificationName='" + classificationName + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", sort=" + sort +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}

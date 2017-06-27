package org.trc.domain.score;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Table(name = "score_converter")
public class ScoreConverter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                         //主键id
    @Column(name = "shopId")
    private Long shopId;                    //店铺Id
    @Column(name = "channelCode")
    private String channelCode;             //频道编码
    @Column(name = "exchangeCurrency")
    private String exchangeCurrency;        //兑换币种
    private Integer amount;                 //兑换额度
    private Integer score;                   //积分
    private String direction;                //兑换方向
    @Column(name = "personEverydayInLimit")
    private Long personEverydayInLimit;        //每人每天可兑入限额（可兑换的外币数量）
    @Column(name = "personEverydayOutLimit")
    private Long personEverydayOutLimit;        //每人每天可兑出限额（可兑换的外币数量）
    @Column(name = "channelEverydayInLimit")
    private Long channelEverydayInLimit;       //渠道每天可兑入限额（可兑换的外币数量）
    @Column(name = "channelEverydayOutLimit")
    private Long channelEverydayOutLimit;       //渠道每天可兑出限额（可兑换的外币数量）
    @Column(name = "isDeleted")
    private Integer isDeleted;                   //状态 0:存在；1：删除
    @Column(name = "createBy")
    private String createBy;                 //创建人
    @Column(name = "createTime")
    private Date createTime;                 //创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getExchangeCurrency() {
        return exchangeCurrency;
    }

    public void setExchangeCurrency(String exchangeCurrency) {
        this.exchangeCurrency = exchangeCurrency;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Long getPersonEverydayInLimit() {
        return personEverydayInLimit;
    }

    public void setPersonEverydayInLimit(Long personEverydayInLimit) {
        this.personEverydayInLimit = personEverydayInLimit;
    }

    public Long getPersonEverydayOutLimit() {
        return personEverydayOutLimit;
    }

    public void setPersonEverydayOutLimit(Long personEverydayOutLimit) {
        this.personEverydayOutLimit = personEverydayOutLimit;
    }

    public Long getChannelEverydayInLimit() {
        return channelEverydayInLimit;
    }

    public void setChannelEverydayInLimit(Long channelEverydayInLimit) {
        this.channelEverydayInLimit = channelEverydayInLimit;
    }

    public Long getChannelEverydayOutLimit() {
        return channelEverydayOutLimit;
    }

    public void setChannelEverydayOutLimit(Long channelEverydayOutLimit) {
        this.channelEverydayOutLimit = channelEverydayOutLimit;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ScoreConverter{" +
                "id=" + id +
                ", channelCode='" + channelCode + '\'' +
                ", exchangeCurrency='" + exchangeCurrency + '\'' +
                ", amount=" + amount +
                ", score=" + score +
                ", direction='" + direction + '\'' +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                '}';
    }
}

package org.trc.domain.auth;

import org.trc.domain.CommonDO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Table(name = "score_auth")
public class Auth implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id; //主键
    @Column(name = "shopId")
    protected Long shopId; //业务方ID
    @Column(name = "channelCode")
    private String channelCode; //平台code
    @Column(name = "exchangeCurrency")
    private String exchangeCurrency; //外币
    @Column(name = "userId")
    private String userId; //用户id
    private String phone; //用户账号
    @Column(name = "contactsUser")
    private String contactsUser; //用户姓名
    @Column(name = "createTime")
    protected Date createTime; //创建时间
    @Column(name = "isDeleted")
    private Integer isDeleted;


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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getContactsUser() {
        return contactsUser;
    }

    public void setContactsUser(String contactsUser) {
        this.contactsUser = contactsUser;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}

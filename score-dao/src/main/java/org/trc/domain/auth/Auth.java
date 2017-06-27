package org.trc.domain.auth;

import org.trc.domain.CommonDO;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Table(name = "score_auth")
public class Auth extends CommonDO{

    @Column(name = "channelCode")
    private String channelCode; //平台code
    @Column(name = "exchangeCurrency")
    private String exchangeCurrency; //外币
    @Column(name = "userId")
    private String userId; //用户id
    private String phone; //用户账号
    @Column(name = "contactsUser")
    private String contactsUser; //用户姓名
    @Column(name = "isDeleted")
    private Integer isDeleted;
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

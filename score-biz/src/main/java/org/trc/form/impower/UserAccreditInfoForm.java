package org.trc.form.impower;

import org.hibernate.validator.constraints.Length;
import org.trc.util.QueryModel;

import javax.ws.rs.QueryParam;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public class UserAccreditInfoForm extends QueryModel {
    /**
     * 用户姓名
     */
    @QueryParam("name")
    @Length(max = 64)
    private String name;
    @QueryParam("phone")
    @Length(max = 16)
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

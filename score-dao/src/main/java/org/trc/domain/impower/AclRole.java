package org.trc.domain.impower;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.trc.domain.CommonDO;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.ws.rs.FormParam;


/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public class AclRole extends ImpowerCommonDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @FormParam("name")
    @NotEmpty
    @Length(max = 64, message = "角色名称字母和数字不能超过64个,汉字不能超过32个")
    private String name;
    @FormParam("roleType")
    //@NotEmpty
    @Length(max = 32, message = "角色类型字母和数字不能超过32个,汉字不能超过16个")
    private String roleType;
    private Integer isValid;
    @FormParam("remark")
    @Length(max = 1024, message = "角色信息备注字母和数字不能超过1024个,汉字不能超过512个")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}

package org.trc.domain.impower;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**

 */

/**
 * 角色权限关系表
 * Created by sone on 2017/5/11.
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public class AclRoleResourceRelation extends ImpowerCommonDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roleId;

    private Long resourceCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(Long resourceCode) {
        this.resourceCode = resourceCode;
    }
}

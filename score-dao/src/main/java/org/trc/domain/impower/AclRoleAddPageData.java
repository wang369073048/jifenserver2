package org.trc.domain.impower;

import javax.ws.rs.FormParam;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public class AclRoleAddPageData extends AclRole {

    @FormParam("roleJurisdiction")
    private String  roleJurisdiction;

    public String getRoleJurisdiction() {
        return roleJurisdiction;
    }

    public void setRoleJurisdiction(String roleJurisdiction) {
        this.roleJurisdiction = roleJurisdiction;
    }

}

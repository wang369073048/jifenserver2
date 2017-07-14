package org.trc.biz.impower;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public interface IAclRoleResourceRelationBiz {
    /**
     * 角色和权限关联表的更新
     * @param roleJurisdiction
     * @param roleId
     * @throws Exception
     */
    void updateRoleJurisdictionRelations(String roleJurisdiction, Long roleId);

    /**
     * 角色和权限关联表的保存
     * @param roleJurisdiction
     * @param roleId
     * @return
     * @throws Exception
     */
    void  saveRoleJurisdictionRelations(String roleJurisdiction, Long roleId);

}

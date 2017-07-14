package org.trc.service.impower;

import org.trc.IBaseService;
import org.trc.domain.impower.AclRoleResourceRelation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public interface IAclRoleResourceRelationService extends IBaseService<AclRoleResourceRelation,Long> {
    /**
     * 根据角色的id查询 对应的权限id
     * @param roleId
     * @return
     * @throws Exception
     */
    List<Long> selectJurisdictionIdList(Long roleId) ;

    /**
     * 根据角色id删除该角色对应的权限
     * @param roleId
     * @return
     * @throws Exception
     */
    int deleteByRoleId(Long roleId);

    /**
     * 根据多个角色id查询角色信息
     * @param roleIds
     * @return
     * @throws Exception
     */
    List<AclRoleResourceRelation> selectListByRoleIds(Long... roleIds);
}

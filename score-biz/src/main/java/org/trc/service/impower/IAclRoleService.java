package org.trc.service.impower;

import org.trc.IBaseService;
import org.trc.domain.impower.AclRole;


import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public interface IAclRoleService extends IBaseService<AclRole,Long> {
    /**
     * 根据角色id，查询使用者(授权用户)的数量
     * @param roleId 角色id
     * @return 授权用户的数量
     * @throws Exception
     */
    int findNumFromRoleAndAccreditInfoByRoleId(Long roleId);

    List<AclRole> findRoleList(List<Long> roleIds);
}


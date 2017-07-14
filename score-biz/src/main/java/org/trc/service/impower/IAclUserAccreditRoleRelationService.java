package org.trc.service.impower;

import org.trc.IBaseService;
import org.trc.domain.impower.AclUserAccreditRoleRelation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public interface IAclUserAccreditRoleRelationService extends IBaseService<AclUserAccreditRoleRelation, Long> {

    /**
     * 根据userAccreditId删除关联的角色
     */
    int deleteByUserAccreditId(Long userAccreditId);

    /**
     * 根据用户授权信息表id查询用户角色id
     *
     * @param userAccreditId
     * @return
     * @throws Exception
     */
    List<AclUserAccreditRoleRelation> selectListByUserAcId(Long userAccreditId);

    /**
     * 根据角色的id修改关联表中的状态
     */
    void updateStatusByRoleId(Map<String, Object> map);

}

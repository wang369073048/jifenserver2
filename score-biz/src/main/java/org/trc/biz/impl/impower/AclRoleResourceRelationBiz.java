package org.trc.biz.impl.impower;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.impower.IAclRoleResourceRelationBiz;
import org.trc.domain.impower.AclRoleResourceRelation;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.RoleException;
import org.trc.service.impower.IAclRoleResourceRelationService;
import org.trc.util.AssertUtil;
import org.trc.util.CommonUtil;
import org.trc.util.StringUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
@Service("roleJurisdictionRelationBiz")
public class AclRoleResourceRelationBiz implements IAclRoleResourceRelationBiz {

    private Logger logger = LoggerFactory.getLogger(AclRoleResourceRelationBiz.class);

    @Resource
    private IAclRoleResourceRelationService roleJuridictionRelationService;

    @Override
    @Transactional
    public void updateRoleJurisdictionRelations(String roleJurisdiction, Long roleId){

        AssertUtil.notNull(roleId,"角色和权限关联保存失败，角色id为空");
        //1.先根据角色id，删除所有的该角色对应的权限
        int count = roleJuridictionRelationService.deleteByRoleId(roleId);
        if (count==0){ //初始化系统角色或者新增角色时，必须有对应的权限<权限不能为空>
            String msg = CommonUtil.joinStr("根据角色id,角色和权限关联删除失败").toString();
            logger.error(msg);
            throw  new RoleException(ExceptionEnum.SYSTEM_ACCREDIT_UPDATE_EXCEPTION, msg);
        }
        //2.保存关联信息
        saveRoleJurisdictionRelations(roleJurisdiction,roleId);

    }

    @Override
    public void saveRoleJurisdictionRelations(String roleJurisdiction, Long roleId){
        System.out.println(roleJurisdiction);
        AssertUtil.notNull(roleId,"角色和权限关联保存失败，角色id为空");
        AssertUtil.notBlank(roleJurisdiction,"根据权限id,角色和权限关联保存失败,参数name[]为空");
        Long[]  roleJurisdictions= StringUtil.splitByComma(roleJurisdiction);
        List<AclRoleResourceRelation> aclRoleResourceRelationList =new ArrayList<>();
        for (Long roleJurisdictionLong:roleJurisdictions ){
            AclRoleResourceRelation aclRoleResourceRelation =new AclRoleResourceRelation();
            aclRoleResourceRelation.setRoleId(roleId);
            aclRoleResourceRelation.setResourceCode(roleJurisdictionLong);
            //aclRoleResourceRelation.setIsValid(ValidEnum.VALID.getCode());
            //ParamsUtil.setBaseDO(aclRoleResourceRelation);
            //aclRoleResourceRelation.setCreateOperator();
            aclRoleResourceRelation.setCreateTime(Calendar.getInstance().getTime());
            aclRoleResourceRelation.setUpdateTime(aclRoleResourceRelation.getCreateTime());
            aclRoleResourceRelation.setIsDeleted(0);
            aclRoleResourceRelationList.add(aclRoleResourceRelation);
        }
        int count=0;
        count=roleJuridictionRelationService.insertList(aclRoleResourceRelationList);
        if(count==0){
            String msg = CommonUtil.joinStr("保存角色和权限关系",  "数据库操作失败").toString();
            logger.error(msg);
            throw new RoleException(ExceptionEnum.SYSTEM_ACCREDIT_SAVE_EXCEPTION, msg);
        }

    }
}

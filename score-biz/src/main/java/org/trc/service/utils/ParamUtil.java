package org.trc.service.utils;

import org.trc.domain.impower.AclRole;
import org.trc.domain.impower.ImpowerCommonDO;
import org.trc.enums.ZeroToNineEnum;

import java.util.Calendar;
import java.util.Date;


public class ParamUtil {

    public static void setBaseDO(ImpowerCommonDO impowerCommonDO){
        Date currentDate = Calendar.getInstance().getTime();
        if(null == impowerCommonDO.getCreateTime())
            impowerCommonDO.setCreateTime(currentDate);
        if(null == impowerCommonDO.getUpdateTime())
            impowerCommonDO.setUpdateTime(currentDate);
        impowerCommonDO.setIsDeleted(Integer.valueOf(ZeroToNineEnum.ZERO.getCode()));
        if (impowerCommonDO instanceof AclRole) {
            AclRole aclRole = (AclRole)impowerCommonDO;
            if(aclRole.getIsValid() == null) {
                aclRole.setIsValid(Integer.valueOf(ZeroToNineEnum.ONE.getCode()));
            }
        }
    }

}

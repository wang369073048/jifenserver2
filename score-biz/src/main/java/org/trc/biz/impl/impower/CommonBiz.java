package org.trc.biz.impl.impower;

import org.apache.commons.lang3.StringUtils;
import org.trc.domain.impower.AclUserAccreditInfo;
import org.trc.domain.impower.ImpowerCommonDO;
import org.trc.service.impower.IAclUserAccreditInfoService;
import org.trc.util.AssertUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/13
 */
public class CommonBiz {

    public void handleUserName(IAclUserAccreditInfoService userAccreditInfoService, List list) {
        if(AssertUtil.collectionIsEmpty(list)){
            return;
        }
        Set<String> userIdsSet=new HashSet<>();
        for (Object obj:list) {
            if(obj instanceof ImpowerCommonDO){
                userIdsSet.add(((ImpowerCommonDO)obj).getCreateOperator());
            }
        }
        String[] userIdArr=new String[userIdsSet.size()];
        userIdsSet.toArray(userIdArr);
        Map<String,AclUserAccreditInfo> mapTemp = userAccreditInfoService.selectByIds(userIdArr);
        for (Object obj:list) {
            if(obj instanceof ImpowerCommonDO){
                if(!StringUtils.isBlank(((ImpowerCommonDO)obj).getCreateOperator())){
                    if(mapTemp!=null){
                        AclUserAccreditInfo aclUserAccreditInfo =mapTemp.get(((ImpowerCommonDO)obj).getCreateOperator());
                        if(aclUserAccreditInfo !=null){
                            ((ImpowerCommonDO)obj).setCreateOperator(aclUserAccreditInfo.getName());
                        }
                    }
                }
            }
        }
    }
}

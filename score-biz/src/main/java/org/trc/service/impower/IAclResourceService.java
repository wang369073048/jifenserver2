package org.trc.service.impower;

import org.trc.IBaseService;
import org.trc.domain.impower.AclResource;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public interface IAclResourceService extends IBaseService<AclResource, Long> {

    List<AclResource> selectJurisdictionListByCodes(Long... codes);

    void insertOne(AclResource aclResource) ;

}

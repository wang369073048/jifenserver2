package org.trc.service.shop;

import org.trc.IBaseService;
import org.trc.domain.shop.ManagerDO;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
public interface IManagerService extends IBaseService<ManagerDO,Long> {

    ManagerDO getManagerByUserId(String userId);
}

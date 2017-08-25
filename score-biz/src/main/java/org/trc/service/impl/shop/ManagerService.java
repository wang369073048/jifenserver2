package org.trc.service.impl.shop;

import org.springframework.stereotype.Service;
import org.trc.domain.shop.ManagerDO;
import org.trc.service.impl.BaseService;
import org.trc.service.shop.IManagerService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
@Service(value = "managerService")
public class ManagerService extends BaseService<ManagerDO,Long> implements IManagerService{

}

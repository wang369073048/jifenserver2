package org.trc.service.impl.order;

import org.springframework.stereotype.Service;
import org.trc.domain.order.AreaDO;
import org.trc.service.impl.BaseService;
import org.trc.service.order.IAreaService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/18
 */
@Service(value = "areaService")
public class AreaService extends BaseService<AreaDO,Long> implements IAreaService{
}

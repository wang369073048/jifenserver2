package org.trc.service.impl.order;

import org.springframework.stereotype.Service;
import org.trc.domain.order.OrderLocusDO;
import org.trc.service.impl.BaseService;
import org.trc.service.order.IOrderLocusService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
@Service(value = "orderLocusService")
public class OrderLocusService extends BaseService<OrderLocusDO,Long> implements IOrderLocusService {
}

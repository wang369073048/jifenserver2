package org.trc.service.impl.order;

import org.springframework.stereotype.Service;
import org.trc.domain.order.OrdersExtendDO;
import org.trc.service.impl.BaseService;
import org.trc.service.order.IOrdersExtendService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
@Service("ordersExtendService")
public class OrdersExtendService extends BaseService<OrdersExtendDO,Long> implements IOrdersExtendService{
}

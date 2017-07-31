package org.trc.service.impl.goods;

import org.springframework.stereotype.Service;
import org.trc.domain.goods.PurchaseRestrictionsDO;
import org.trc.service.goods.IPurchaseRestrictionsService;
import org.trc.service.impl.BaseService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/31
 */
@Service(value = "purchaseRestrictionsService")
public class PurchaseRestrictionsService extends BaseService<PurchaseRestrictionsDO,Long> implements IPurchaseRestrictionsService{
}

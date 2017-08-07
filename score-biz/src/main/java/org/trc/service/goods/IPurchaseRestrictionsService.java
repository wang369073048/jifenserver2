package org.trc.service.goods;

import org.trc.IBaseService;
import org.trc.domain.goods.PurchaseRestrictionsDO;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/31
 */
public interface IPurchaseRestrictionsService extends IBaseService<PurchaseRestrictionsDO,Long>{

    /**
     * 商品新增或商品限购信息更新时，调用此方法
     * @param item
     * @return
     */
    int deal(PurchaseRestrictionsDO item);
}

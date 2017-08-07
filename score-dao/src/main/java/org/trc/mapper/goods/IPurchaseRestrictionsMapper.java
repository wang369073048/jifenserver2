package org.trc.mapper.goods;

import org.trc.domain.goods.PurchaseRestrictionsDO;
import org.trc.domain.goods.PurchaseRestrictionsHistoryDO;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface IPurchaseRestrictionsMapper extends BaseMapper<PurchaseRestrictionsDO> {

    int insertHistory(PurchaseRestrictionsHistoryDO param);

    int updateByGoodsId(PurchaseRestrictionsDO param);
}

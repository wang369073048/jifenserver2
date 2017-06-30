package org.trc.service.goods;

import org.trc.IBaseService;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CategoryDO;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/28
 */
public interface ICouponsService extends IBaseService<CardCouponsDO,Long>{

    int deleteByBatchNumber(CardCouponsDO cardCouponsDO);
}

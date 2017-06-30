package org.trc.mapper.goods;

import org.trc.domain.goods.CardCouponsDO;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface ICouponsMapper extends BaseMapper<CardCouponsDO> {
    int deleteByBatchNumber(CardCouponsDO cardCouponsDO);
}

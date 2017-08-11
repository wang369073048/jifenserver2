package org.trc.service.impl.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CategoryDO;
import org.trc.mapper.goods.ICatetoryMapper;
import org.trc.mapper.goods.ICouponsMapper;
import org.trc.service.goods.ICouponsService;
import org.trc.service.impl.BaseService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/28
 */
@Service("couponsService")
public class CouponsService extends BaseService<CardCouponsDO,Long> implements ICouponsService{

    @Autowired
    private ICouponsMapper couponsMapper;

    @Override
    public int deleteByBatchNumber(CardCouponsDO cardCouponsDO){
        return couponsMapper.deleteByBatchNumber(cardCouponsDO);
    }

    @Override
    public int updateStockById(CardCouponsDO cardCoupons) {
        return couponsMapper.updateStockById(cardCoupons);
    }
}

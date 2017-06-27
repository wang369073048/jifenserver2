package org.trc.biz.impl.goods;

import org.springframework.stereotype.Service;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CardItemDO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Service("couponsBiz")
public class CouponsBiz implements ICouponsBiz{
    @Override
    public int deleteByBatchNumber(CardCouponsDO cardCoupon) {
        return 0;
    }

    @Override
    public int deleteItemById(CardItemDO cardItem) {
        return 0;
    }

    @Override
    public int insert(CardCouponsDO cardCoupons) {
        return 0;
    }

    @Override
    public int importCardItem(String batchNumber, Long shopId, List<CardItemDO> cardItemList) {
        return 0;
    }

    @Override
    public Pagenation<CardCouponsDO> queryCouponsForPage(CardCouponsDO cardCouponsDO, Pagenation<CardCouponsDO> pageRequest) {
        return null;
    }

    @Override
    public List<CardItemDO> releaseCardCoupons(CardItemDO cardItem) {
        return null;
    }

    @Override
    public CardCouponsDO selectByBatchNumer(Long shopId, String batchNumber) {
        return null;
    }

    @Override
    public CardItemDO selectItemByCode(Long shopId, String batchNumber, String code) {
        return null;
    }

    @Override
    public int updateById(CardCouponsDO cardCoupons) {
        return 0;
    }

    @Override
    public List<CardItemDO> selectItemByOrderNum(String orderNum) {
        return null;
    }
}

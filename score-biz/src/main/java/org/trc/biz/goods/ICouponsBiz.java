package org.trc.biz.goods;

import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CardItemDO;
import org.trc.form.goods.CardCouponsForm;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * Created by hzwzhen on 2017/6/22.
 */
public interface ICouponsBiz {
    int deleteByBatchNumber(CardCouponsDO cardCoupon);

    int deleteItemById(CardItemDO cardItem);

    int insert(CardCouponsDO cardCoupons);

    int importCardItem(String batchNumber, Long shopId, List<CardItemDO> cardItemList);

    Pagenation<CardCouponsDO> queryCouponsForPage(CardCouponsForm cardCouponsForm, Pagenation<CardCouponsDO> pageRequest);

    List<CardItemDO> releaseCardCoupons(CardItemDO cardItem);

    CardCouponsDO selectByBatchNumer(Long shopId, String batchNumber);

    CardItemDO selectItemByCode(Long shopId, String batchNumber, String code);

    int updateById(CardCouponsDO cardCoupons);

    List<CardItemDO> selectItemByOrderNum(String orderNum);
}

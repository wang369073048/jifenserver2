package org.trc.service.goods;

import org.trc.IBaseService;
import org.trc.domain.goods.CardItemDO;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface ICardItemService extends IBaseService<CardItemDO,Long>{

    int deleteByBatchNumber(CardItemDO cardItem);

    CardItemDO selectByParams(CardItemDO cardItem);

    int deleteById(CardItemDO cardItem);
}

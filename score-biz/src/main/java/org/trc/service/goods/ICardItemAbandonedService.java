package org.trc.service.goods;

import org.trc.IBaseService;
import org.trc.domain.goods.CardItemAbandonedDO;
import org.trc.domain.goods.CardItemDO;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface ICardItemAbandonedService extends IBaseService<CardItemAbandonedDO,Long>{

    int selectIntoFromCardItem(CardItemDO cardItem);
}

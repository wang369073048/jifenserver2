package org.trc.mapper.goods;

import org.trc.domain.goods.CardItemAbandonedDO;
import org.trc.domain.goods.CardItemDO;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface ICardItemAbandonedMapper extends BaseMapper<CardItemAbandonedDO>{

    int selectIntoFromCardItem(CardItemDO cardItem);
}

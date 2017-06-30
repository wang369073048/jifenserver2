package org.trc.service.impl.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.CardItemAbandonedDO;
import org.trc.domain.goods.CardItemDO;
import org.trc.mapper.goods.ICardItemAbandonedMapper;
import org.trc.service.goods.ICardItemAbandonedService;
import org.trc.service.impl.BaseService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
@Service("cardItemAbandonedService")
public class CardItemAbandonedService extends BaseService<CardItemAbandonedDO,Long> implements ICardItemAbandonedService{

    @Autowired
    private ICardItemAbandonedMapper cardItemAbandonedMapper;

    @Override
    public int selectIntoFromCardItem(CardItemDO cardItem) {
        return cardItemAbandonedMapper.selectIntoFromCardItem(cardItem);
    }
}

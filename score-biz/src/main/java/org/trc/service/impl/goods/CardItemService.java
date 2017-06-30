package org.trc.service.impl.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.CardItemDO;
import org.trc.mapper.goods.ICardItemMapper;
import org.trc.service.goods.ICardItemService;
import org.trc.service.impl.BaseService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
@Service("cardItemService")
public class CardItemService extends BaseService<CardItemDO,Long> implements ICardItemService{

    @Autowired
    private ICardItemMapper cardItemMapper;
    @Override
    public int deleteByBatchNumber(CardItemDO cardItem) {
        return cardItemMapper.deleteByBatchNumber(cardItem);
    }

    @Override
    public CardItemDO selectByParams(CardItemDO cardItem) {
        return cardItemMapper.selectByParams(cardItem);
    }

    @Override
    public int deleteById(CardItemDO cardItem) {
        return cardItemMapper.deleteById(cardItem);
    }
}

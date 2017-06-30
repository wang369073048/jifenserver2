package org.trc.service.impl.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.GoodsDO;
import org.trc.mapper.goods.IGoodsMapper;
import org.trc.service.goods.IGoodsService;
import org.trc.service.impl.BaseService;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Service("goodsService")
public class GoodsService extends BaseService<GoodsDO,Long> implements IGoodsService{

    @Autowired
    private IGoodsMapper goodsMapper;

    @Override
    public int updateById(GoodsDO goodsDO) {
        return goodsMapper.updateById(goodsDO);
    }
}

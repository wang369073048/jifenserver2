package org.trc.service.impl.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.GoodsClassificationRelationshipDO;
import org.trc.mapper.goods.IGoodsClassificationRelationshipMapper;
import org.trc.service.goods.IGoodsClassificationRelationshipService;
import org.trc.service.impl.BaseService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/1
 */
@Service("goodsClassificationRelationshipService")
public class GoodsClassificationRelationshipService extends BaseService<GoodsClassificationRelationshipDO,Long> implements IGoodsClassificationRelationshipService{
    @Autowired
    private IGoodsClassificationRelationshipMapper goodsClassificationRelationshipMapper;
    @Override
    public int delete(GoodsClassificationRelationshipDO param) {
        return goodsClassificationRelationshipMapper.delete(param);
    }
}

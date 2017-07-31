package org.trc.mapper.goods;

import org.trc.domain.goods.GoodsClassificationRelationshipDO;
import org.trc.domain.goods.ShopClassificationDO;
import org.trc.util.BaseMapper;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/28
 */
public interface IShopClassificationMapper extends BaseMapper<ShopClassificationDO>{

    List<ShopClassificationDO> queryEntity(ShopClassificationDO param);

    List<ShopClassificationDO> listEntityByParam(GoodsClassificationRelationshipDO param);
}

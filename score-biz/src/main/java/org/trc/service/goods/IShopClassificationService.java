package org.trc.service.goods;

import org.trc.IBaseService;
import org.trc.domain.goods.GoodsClassificationRelationshipDO;
import org.trc.domain.goods.ShopClassificationDO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/28
 */
public interface IShopClassificationService extends IBaseService<ShopClassificationDO,Long> {

    Pagenation<ShopClassificationDO> queryEntityByPage(ShopClassificationDO param, Pagenation<ShopClassificationDO> pageRequest);

    List<ShopClassificationDO> listEntity(ShopClassificationDO param);

    List<ShopClassificationDO> listEntityByParam(GoodsClassificationRelationshipDO param);

    int delete(ShopClassificationDO shopClassification);
}

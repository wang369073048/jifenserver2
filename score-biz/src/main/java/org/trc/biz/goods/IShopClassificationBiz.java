package org.trc.biz.goods;

import org.trc.domain.goods.ShopClassificationDO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/28
 */
public interface IShopClassificationBiz{

    int insert(ShopClassificationDO shopClassification);

    int delete(ShopClassificationDO shopClassification);

    int update(ShopClassificationDO shopClassification);

    ShopClassificationDO getEntityByParam(ShopClassificationDO param);

    Pagenation<ShopClassificationDO> queryEntity(ShopClassificationDO param, Pagenation<ShopClassificationDO> pageRequest);

    List<ShopClassificationDO> listEntity(ShopClassificationDO param);
}

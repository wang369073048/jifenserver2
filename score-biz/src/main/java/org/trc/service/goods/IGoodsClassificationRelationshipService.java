package org.trc.service.goods;

import org.trc.IBaseService;
import org.trc.domain.goods.GoodsClassificationRelationshipDO;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/1
 */
public interface IGoodsClassificationRelationshipService extends IBaseService<GoodsClassificationRelationshipDO,Long> {

    int delete(GoodsClassificationRelationshipDO param);
}

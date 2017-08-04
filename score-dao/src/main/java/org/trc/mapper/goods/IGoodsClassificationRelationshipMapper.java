package org.trc.mapper.goods;

import org.trc.domain.goods.GoodsClassificationRelationshipDO;
import org.trc.util.BaseMapper;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/1
 */
public interface IGoodsClassificationRelationshipMapper extends BaseMapper<GoodsClassificationRelationshipDO> {

    int batchInsert(List<GoodsClassificationRelationshipDO> list);
}

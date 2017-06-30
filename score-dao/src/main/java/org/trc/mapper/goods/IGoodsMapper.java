package org.trc.mapper.goods;

import org.trc.domain.goods.GoodsDO;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface IGoodsMapper extends BaseMapper<GoodsDO> {

    int updateById(GoodsDO goodsDO);
}

package org.trc.mapper.goods;

import org.trc.domain.goods.GoodsDO;
import org.trc.util.BaseMapper;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface IGoodsMapper extends BaseMapper<GoodsDO> {

    int updateById(GoodsDO goodsDO);

    /**
     * 根据ID删除信息
     * @param goodsDO GoodsDO
     * @return int
     */
    int deleteByParam(GoodsDO goodsDO);

    /**
     * 查询没有推荐的商品（分页）
     * @param goodsDO 查询对象
     * @return 查询到的没有推荐的商品
     */
    List<GoodsDO> selectListExceptRecommendByPage(GoodsDO goodsDO);
}

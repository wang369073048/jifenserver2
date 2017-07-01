package org.trc.mapper.goods;

import org.trc.domain.goods.GoodsRecommendDO;
import org.trc.domain.goods.GoodsRecommendDTO;
import org.trc.util.BaseMapper;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/30
 */
public interface IGoodsRecommendMapper extends BaseMapper<GoodsRecommendDO>{
    /**
     * 查询推荐商品列表（分页）
     * @param query 查询对象
     * @return 查询到的推荐列表
     */
    List<GoodsRecommendDTO> selectGoodsRecommendsByPage(GoodsRecommendDTO query);
}

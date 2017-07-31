package org.trc.mapper.goods;

import org.trc.domain.goods.GoodsDO;
import org.trc.domain.query.GoodsQuery;
import org.trc.util.BaseMapper;

import java.util.List;
import java.util.Map;

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

    /**
     * 判定对应shopId下的所属商品个数
     * @param params
     * @return
     */
    int isOwnerOf(Map<String, Object> params);

    /**
     * 多条件查询表信息(分页)
     * @param goodsDO GoodsDO
     * @return List<GoodsDO>
     */
    List<GoodsDO> selectListByParams(GoodsDO goodsDO);

    /**
     * 多条件查询表信息(分页)
     * @param goodsQuery GoodsQuery
     * @return List<GoodsDO>
     */
    List<GoodsDO> selectListByClassification(GoodsQuery goodsQuery);
}

package org.trc.service.goods;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.goods.GoodsDO;
import org.trc.domain.query.GoodsQuery;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * Created by hzwzhen on 2017/6/22.
 */
public interface IGoodsService extends IBaseService<GoodsDO,Long>{

    /**
     * 根据ID更新信息
     * @param goodsDO GoodsDO
     * @return int
     */
    int updateById(GoodsDO goodsDO);

    /**
     * 根据ID删除信息
     * @param goodsDO GoodsDO
     * @return int
     */
    int deleteByParam(GoodsDO goodsDO);

    /**
     * 根据ID上下架
     * @param goodsDO GoodsDO
     * @return int
     */
    int upOrDownById(GoodsDO goodsDO);

    /**
     * 多条件查询表信息(分页)
     * @param goodsDO GoodsDO
     * @param pageRequest PageRequest<GoodsDO>
     * @return List<GoodsDO>
     */
    Pagenation<GoodsDO> selectListByParams(GoodsDO goodsDO, Pagenation<GoodsDO> pageRequest);

    /**
     * 查询没有推荐的商品（分页）
     * @param query 查询对象
     * @param page 分页参数
     * @return 查询到的没有推荐的商品
     */
    Pagenation<GoodsDO> queryGoodsDOListExceptRecommendForPage(GoodsDO query, Pagenation<GoodsDO> page);

    /**
     * 判定对应shopId下的所属商品个数
     * @param params
     * @return
     */
    int isOwnerOf(Map<String, Object> params);

    /**
     * 多条件查询表信息(分页)
     * @param goodsQuery GoodsQuery
     * @param pageRequest PageRequest<GoodsDO>
     * @return List<GoodsDO>
     */
    Pagenation<GoodsDO> selectListByClassification(GoodsQuery goodsQuery, Pagenation<GoodsDO> pageRequest);

    int selectCountByParams(GoodsDO goodsDO);
}

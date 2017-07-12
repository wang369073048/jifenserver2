package org.trc.service.goods;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.goods.GoodsRecommendDO;
import org.trc.domain.dto.GoodsRecommendDTO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/30
 */
public interface IGoodsRecommendService extends IBaseService<GoodsRecommendDO,Long>{

    /**
     * 根据ID查询表数据
     * @param id Long
     * @return GoodsRecommendDO
     */
    GoodsRecommendDO selectById(Long id);

    /**
     * 多条件查询表信息(分页)
     * @param goodsRecommendDO GoodsRecommendDO
     * @param pageRequest PageRequest<GoodsRecommendDO>
     * @return List<GoodsRecommendDO>
     */
    List<GoodsRecommendDO> selectListByParams(GoodsRecommendDO goodsRecommendDO, PageRequest<GoodsRecommendDO> pageRequest);
    /**
     * 查询推荐商品列表（分页）
     * @param query 查询对象
     * @param pageRequest 分页参数
     * @return 查询到的推荐列表
     */
    Pagenation<GoodsRecommendDTO> selectGoodsRecommendsByPage(GoodsRecommendDTO query, Pagenation<GoodsRecommendDTO> pageRequest);

    int selectCountByParams(GoodsRecommendDO goodsRecommendDO);

    /**
     * 根据ID更新信息
     * @param goodsRecommendDO GoodsRecommendDO
     * @return int
     */
    int updateById(GoodsRecommendDO goodsRecommendDO);

    /**
     * 根据ID删除信息
     * @param id Long
     * @return int
     */
    int deleteById(Long id);

    /**
     * 查找当前最大的序号
     * @return
     */
    int getNextSort();

    /**
     * 查找商品是否被推荐
     * @param goodsId
     * @return
     */
    int selectCountByGoodsId(Long goodsId);

}

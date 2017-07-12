package org.trc.biz.goods;

import org.trc.domain.goods.GoodsRecommendDO;
import org.trc.domain.dto.GoodsRecommendDTO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface IGoodsRecommendBiz {



    /**
     * 多条件查询推荐列表，shopId必填，商品名称选填
     * @param query 查询对象
     * @param pageRequest 分页参数
     * @return PageRequest<GoodsRecommendDTO>
     */
    Pagenation<GoodsRecommendDTO> queryGoodsRecommondsForPage(GoodsRecommendDTO query, Pagenation<GoodsRecommendDTO> pageRequest);

    /**
     * 根据用户ID查询
     * @param id Long
     * @return GoodsRecommendDO
     */
    GoodsRecommendDO getGoodsRecommendDOById(Long id);



    /**
     * 批量新增推荐商品
     * @param goodsRecommendDOs 要新增的推荐商品列表
     * @return 新增结果
     */
    int batchAddRecommends(List<GoodsRecommendDO> goodsRecommendDOs);


    /**
     * 上下移
     * @param goodsIdRecommendA
     * @param goodsIdRecommendB
     * @param shopId
     * @param operatorUserId
     * @return
     */
    int upOrDown(Long goodsIdRecommendA, Long goodsIdRecommendB, Long shopId, String operatorUserId);

    /**
     * 根据id删除热门推荐
     * @param id
     * @return int
     */
    int deleteById(Long id);



    /**
     * 查找商品是否被推荐
     * @param goodsId
     * @return
     */
    int selectCountByGoodsId(Long goodsId);

}

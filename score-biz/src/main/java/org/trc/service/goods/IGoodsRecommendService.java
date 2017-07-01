package org.trc.service.goods;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.goods.GoodsRecommendDO;
import org.trc.domain.goods.GoodsRecommendDTO;
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
     * 查询推荐商品列表（分页）
     * @param query 查询对象
     * @param pageRequest 分页参数
     * @return 查询到的推荐列表
     */
    Pagenation<GoodsRecommendDTO> selectGoodsRecommendsByPage(GoodsRecommendDTO query, Pagenation<GoodsRecommendDTO> pageRequest);

    /**
     * 根据ID删除信息
     * @param id Long
     * @return int
     */
    int deleteById(Long id);

}

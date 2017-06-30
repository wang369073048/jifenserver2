package org.trc.biz.impl.goods;

import com.txframework.core.jdbc.PageRequest;
import org.springframework.stereotype.Service;
import org.trc.biz.goods.IGoodsRecommendBiz;
import org.trc.domain.goods.GoodsRecommendDO;
import org.trc.domain.goods.GoodsRecommendDTO;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/30
 */
@Service("goodsRecommendBiz")
public class GoodsRecommendBiz implements IGoodsRecommendBiz{
    @Override
    public PageRequest<GoodsRecommendDO> queryGoodsRecommendDOListForPage(GoodsRecommendDO goodsRecommendDO, PageRequest<GoodsRecommendDO> pageRequest) {
        return null;
    }

    @Override
    public PageRequest<GoodsRecommendDTO> queryGoodsRecommondsForPage(GoodsRecommendDTO query, PageRequest<GoodsRecommendDTO> pageRequest) {
        return null;
    }

    @Override
    public GoodsRecommendDO getGoodsRecommendDOById(Long id) {
        return null;
    }

    @Override
    public int addGoodsRecommendDO(GoodsRecommendDO goodsRecommendDO) {
        return 0;
    }

    @Override
    public int batchAddRecommends(List<GoodsRecommendDO> goodsRecommendDOs) {
        return 0;
    }

    @Override
    public int modifyGoodsRecommendDO(GoodsRecommendDO goodsRecommendDO) {
        return 0;
    }

    @Override
    public int upOrDown(Long goodsIdRecommendA, Long goodsIdRecommendB, Long shopId, String operatorUserId) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int getNextSort() {
        return 0;
    }

    @Override
    public int selectCountByGoodsId(Long goodsId) {
        return 0;
    }
}

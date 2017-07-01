package org.trc.biz.impl.goods;

import com.txframework.core.jdbc.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.biz.goods.IGoodsRecommendBiz;
import org.trc.domain.goods.GoodsRecommendDO;
import org.trc.domain.goods.GoodsRecommendDTO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.GoodsRecommendException;
import org.trc.service.goods.IGoodsRecommendService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/6/30
 */
@Service("goodsRecommendBiz")
public class GoodsRecommendBiz implements IGoodsRecommendBiz{

    Logger logger = LoggerFactory.getLogger(GoodsRecommendBiz.class);

    @Autowired
    private IGoodsRecommendService goodsRecommendService;
    @Override
    public PageRequest<GoodsRecommendDO> queryGoodsRecommendDOListForPage(GoodsRecommendDO goodsRecommendDO, PageRequest<GoodsRecommendDO> pageRequest) {
        return null;
    }

    @Override //TODO 分页测试
    public Pagenation<GoodsRecommendDTO> queryGoodsRecommondsForPage(GoodsRecommendDTO query, Pagenation<GoodsRecommendDTO> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(query, "传入参数不能为空");
            Assert.isTrue(query.getShopId() != null, "传入ShopId不能为空");
            return goodsRecommendService.selectGoodsRecommendsByPage(query,pageRequest);
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询GoodsRecommendDTO校验参数异常!", e);
            throw new GoodsRecommendException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "多条件查询GoodsRecommendDTO校验参数异常!");
        } catch (Exception e) {
            logger.error("多条件查询GoodsRecommendDTO信息异常!", e);
            throw new GoodsRecommendException(ExceptionEnum.GOODSRECOMMEND_QUERY_EXCEPTION, "多条件查询GoodsRecommendDTO信息异常!");
        }
    }

    @Override
    public GoodsRecommendDO getGoodsRecommendDOById(Long id) {
        try {
            Assert.isTrue(id != null, "查询Id不能为空!");
            GoodsRecommendDO goodsRecommendDO = new GoodsRecommendDO();
            goodsRecommendDO.setId(id);
            goodsRecommendDO.setIsDeleted(false);
            goodsRecommendDO = goodsRecommendService.selectOne(goodsRecommendDO);
            return goodsRecommendDO;
        } catch (IllegalArgumentException e) {
            logger.error("查询GoodsRecommendDO传入Id为空!", e);
            throw new GoodsRecommendException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "查询GoodsRecommendDO传入Id为空!");
        } catch (Exception e) {
            logger.error("根据ID=>[" + id + "]查询GoodsRecommendDO信息异常!", e);
            throw new GoodsRecommendException(ExceptionEnum.GOODS_QUERY_EXCEPTION, "根据ID=>[" + id + "]查询GoodsRecommendDO信息异常!");
        }
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

    /**
     * 根据id删除热门推荐
     *
     * @param id
     * @return
     */
    @Override
    public int deleteById(Long id) {
        try {
            Assert.notNull(id, "推荐商品A的Id不能为null");
            int result = goodsRecommendService.deleteById(id);
            if (result < 1) {
                throw new GoodsRecommendException(ExceptionEnum.GOODS_UPDATE_EXCEPTION, "删除ID=>[" + id + "]的GoodsRecommendDO信息异常!");
            }
            logger.info("删除ID=>[" + id + "]的GoodsRecommendDO成功!");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("删除GoodsRecommendDO校验参数异常!", e);
            throw new GoodsRecommendException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "删除GoodsRecommendDO校验参数异常!");
        } catch (Exception e) {
            logger.error("删除ID=>[" + id + "]的GoodsRecommendDO信息异常", e);
            throw new GoodsRecommendException(ExceptionEnum.GOODS_UPDATE_EXCEPTION, "删除信息异常!");
        }
    }

    @Override
    public int getNextSort() {
        return 0;
    }

    @Override
    public int selectCountByGoodsId(Long goodsId) {
        GoodsRecommendDO goodsRecommendDO = new GoodsRecommendDO();
        goodsRecommendDO.setGoodsId(goodsId);
        goodsRecommendDO.setIsDeleted(false);
        return goodsRecommendService.selectCount(goodsRecommendDO);
    }
}

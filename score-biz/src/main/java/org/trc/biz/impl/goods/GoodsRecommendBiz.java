package org.trc.biz.impl.goods;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;
import org.trc.biz.goods.IGoodsRecommendBiz;
import org.trc.domain.goods.GoodsRecommendDO;
import org.trc.domain.dto.GoodsRecommendDTO;
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
    @Transactional(rollbackFor = Exception.class)
    public int batchAddRecommends(List<GoodsRecommendDO> goodsRecommendDOs) {
        try {
            for (GoodsRecommendDO goodsRecommendDO : goodsRecommendDOs) {
                validateForAdd(goodsRecommendDO);
                goodsRecommendDO.setSort(goodsRecommendService.getNextSort());
                int result = goodsRecommendService.insertSelective(goodsRecommendDO);
                if (result < 1) {
                    throw new GoodsRecommendException(ExceptionEnum.GOODS_SAVE_EXCEPTION, String.format("新增ID=>[%s]的GoodsRecommendDO信息异常!",goodsRecommendDO.getId()));
                }
            }
            for (GoodsRecommendDO goodsRecommendDO : goodsRecommendDOs) {
                logger.info(String.format("新增推荐商品id==>[%s]的GoodsRecommendDO成功",goodsRecommendDO.getId()));
            }
            return goodsRecommendDOs.size();
        } catch (IllegalArgumentException e) {
            logger.error("新增GoodsRecommendDO校验参数异常!", e);
            throw new GoodsRecommendException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "新增GoodsRecommendDO校验参数异常!");
        } catch (Exception e) {
            for (GoodsRecommendDO goodsRecommendDO : goodsRecommendDOs) {
                logger.error(String.format("新增ID=>[%s]的GoodsRecommendDO信息异常!",goodsRecommendDO.getId()), e);
            }
            throw new GoodsRecommendException(ExceptionEnum.GOODS_SAVE_EXCEPTION, "新增信息异常!");
        }
    }

    /**
     * Validate Add
     *
     * @param goodsRecommendDO GoodsRecommendDO
     */
    private void validateForAdd(GoodsRecommendDO goodsRecommendDO) {
        Assert.isTrue(goodsRecommendDO != null, "goodsRecommendDO不能为空!");
        Assert.notNull(goodsRecommendDO.getGoodsId(), "goodsRecommendDO推荐的goodsId不能为空！");
        Assert.notNull(goodsRecommendDO.getShopId(), "goodsRecommendDO推荐的shopId不能为空！");
        Assert.notNull(goodsRecommendDO.getOperatorUserId(), "goodsRecommendDO推荐的operatorUserId不能为空!");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upOrDown(Long goodsIdRecommendA, Long goodsIdRecommendB, Long shopId, String operatorUserId) {
        try {
            //参数校验
            Assert.notNull(goodsIdRecommendA, "推荐商品A的Id不能为null");
            Assert.notNull(goodsIdRecommendB, "推荐商品B的Id不能为null");
            Assert.isTrue(StringUtils.isNotBlank(operatorUserId), "操作用户的id不能为null");

            GoodsRecommendDO goodsRecommendDOA = goodsRecommendService.selectById(goodsIdRecommendA);
            Assert.notNull(goodsRecommendDOA, "推荐商品A为null,id:" + goodsIdRecommendA);
            GoodsRecommendDO goodsRecommendDOB = goodsRecommendService.selectById(goodsIdRecommendB);
            Assert.notNull(goodsRecommendDOB, "推荐商品B为null,id:" + goodsIdRecommendB);
            if(null != shopId && (goodsRecommendDOA.getShopId() != shopId || goodsRecommendDOB.getShopId() != shopId) ){
                throw new GoodsRecommendException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"推荐商品上下移操作不合法!");
            }
            //交换排序
            Integer sorta = goodsRecommendDOA.getSort();
            Integer sortb = goodsRecommendDOB.getSort();
            goodsRecommendDOA.setSort(sortb);
            goodsRecommendDOB.setSort(sorta);

            //更新排序
            int result1 = goodsRecommendService.updateById(goodsRecommendDOA);
            int result2 = goodsRecommendService.updateById(goodsRecommendDOB);

            if (result1 != 1 || result2 != 1) {
                throw new GoodsRecommendException(ExceptionEnum.GOODSRECOMMEND_UPDATE_EXCEPTION,"推荐商品上下移操作失败!");
            }
            logger.info("修改ID=>[" + goodsRecommendDOA.getId() + "]的GoodsRecommendDO成功!");
            logger.info("修改ID=>[" + goodsRecommendDOB.getId() + "]的GoodsRecommendDO成功!");
            return 1;
        } catch (IllegalArgumentException e) {
            logger.error("修改GoodsRecommendDO校验参数异常!", e);
            throw new GoodsRecommendException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "修改GoodsRecommendDO校验参数异常!");
        } catch (Exception e) {
            logger.error("修改ID=>[" + goodsIdRecommendA + "]的GoodsRecommendDO信息异常", e);
            logger.error("修改ID=>[" + goodsIdRecommendB + "]的GoodsRecommendDO信息异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new GoodsRecommendException(ExceptionEnum.GOODSRECOMMEND_UPDATE_EXCEPTION, "修改信息异常!");
        }
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
    public int selectCountByGoodsId(Long goodsId) {
        GoodsRecommendDO goodsRecommendDO = new GoodsRecommendDO();
        goodsRecommendDO.setGoodsId(goodsId);
        goodsRecommendDO.setIsDeleted(false);
        return goodsRecommendService.selectCount(goodsRecommendDO);
    }
}

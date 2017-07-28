package org.trc.biz.impl.goods;

import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.TrCouponOperation;
import com.trc.mall.externalservice.dto.CouponDto;
import com.trc.mall.util.GuidUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.annotation.cache.CacheEvit;
import org.trc.biz.goods.ICategoryBiz;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.biz.goods.IGoodsBiz;
import org.trc.biz.goods.IGoodsRecommendBiz;
import org.trc.constants.Category;
import org.trc.constants.ExternalserviceResultCodeConstants;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CategoryDO;
import org.trc.domain.goods.GoodsDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.CardCouponException;
import org.trc.exception.CouponException;
import org.trc.exception.GoodsException;
import org.trc.service.goods.IGoodsService;
import org.trc.util.Pagenation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.trc.enums.ExceptionEnum.COUPON_QUERY_EXCEPTION;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Service("goodsBiz")
public class GoodsBiz implements IGoodsBiz{

    Logger logger = LoggerFactory.getLogger(GoodsBiz.class);

    @Autowired
    private ICategoryBiz categoryBiz;

    @Autowired
    private ICouponsBiz couponsBiz;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IGoodsRecommendBiz goodsRecommendBiz;

    @Autowired
    private TrCouponOperation trCouponOperation;

    @Override
    @CacheEvit(key="#goodsDO.id")
    public int saveGoodsDO(GoodsDO goodsDO) {
        try {
            goodsDO.setGoodsSn(GuidUtil.getNextUid("pro"));
            validateForAdd(goodsDO);
            validateGoods(goodsDO);
            int result = goodsService.insert(goodsDO);
            logger.info("新增ID=>[" + goodsDO.getId() + "]的GoodsDO成功");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("新增GoodsDO校验参数异常!",e);
            throw new GoodsException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (GoodsException e) {
            throw e;
        } catch (DuplicateKeyException e){
            logger.error("新增GoodsDO校验索引异常!",e);
            throw new GoodsException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "请检查商品条码是否已存在!");
        } catch (Exception e) {
            logger.error("添加商品异常!",e);
            throw new GoodsException(ExceptionEnum.GOODS_SAVE_EXCEPTION, "添加商品异常!");
        }
    }

    @Override
    @CacheEvit(key="#goodsDO.id")
    public int updateGoodsDO(GoodsDO goodsDO) {
        try {
            validateForUpdate(goodsDO);
            int result = goodsRecommendBiz.selectCountByGoodsId(goodsDO.getId());
            if(result > 0){
                throw new GoodsException(ExceptionEnum.GOODS_CAN_NOT_BE_DOWNED,"热门推荐了该商品,当前不允许编辑或下架！如需下架请先删除热门推荐!");
            }
            validateGoods(goodsDO);
            result = goodsService.updateById(goodsDO);
            if(result != 1){
                logger.error("修改GoodsDO异常!请求参数为:"+goodsDO);
                throw new GoodsException(ExceptionEnum.GOODS_UPDATE_EXCEPTION, "修改GoodsDO异常!");
            }
            logger.info("修改ID=>[" + goodsDO.getId() + "]的GoodsDO成功!");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("修改GoodsDO校验参数异常!",e);
            throw new GoodsException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (CardCouponException e) {
            throw e;
        } catch (GoodsException e) {
            throw e;
        } catch (Exception e) {
            logger.error("修改ID=>[" + goodsDO.getId() + "]的GoodsDO信息异常",e);
            throw new GoodsException(ExceptionEnum.GOODS_UPDATE_EXCEPTION, "修改信息异常!");
        }
    }

    @Override
    public int deleteGoodsDO(GoodsDO goodsDO) {
        Assert.isTrue(null != goodsDO,"goodsDO不能为空!");
        Assert.isTrue(null != goodsDO.getId(),"待删除商品id不能为空!");
        Assert.isTrue(null != goodsDO.getShopId(),"待删除商品所属店铺id不能为空!");
        _checkDependencies(goodsDO.getId());
        return goodsService.deleteByParam(goodsDO);
    }
    private boolean _checkDependencies(Long id){
        int result = goodsRecommendBiz.selectCountByGoodsId(id);
        if(result > 0){
            throw new GoodsException(ExceptionEnum.GOODS_CAN_NOT_BE_DOWNED, "热门推荐了该商品,当前操作不允许！请先删除热门推荐!");
        }
        return true;
    }

    @Override
    public Pagenation<GoodsDO> queryGoodsDOListForPage(GoodsDO goodsDO, Pagenation<GoodsDO> page) {
        try {
            Assert.notNull(page, "分页参数不能为空");
            Assert.notNull(goodsDO, "传入参数不能为空");
            return goodsService.selectListByParams(goodsDO, page);
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询GoodsDO校验参数异常!",e);
            throw new GoodsException(ExceptionEnum.GOODS_QUERY_EXCEPTION,e.getMessage());
        } catch (Exception e) {
            logger.error("多条件查询GoodsDO信息异常!", e);
            throw new GoodsException(ExceptionEnum.GOODS_QUERY_EXCEPTION,"多条件查询GoodsDO信息异常!");
        }
    }

    @Override
    public Pagenation<GoodsDO> queryGoodsDOListForUser(GoodsDO goodsDO, Pagenation<GoodsDO> page) {
        return null;
    }

    @Override
    public GoodsDO getGoodsDOById(Long id, Integer isUp) {
        try {
            Assert.isTrue(id != null,"查询Id不能为空!");
            GoodsDO param = new GoodsDO();
            if(isUp != null){
                param.setIsUp(isUp);
            }
            param.setId(id);
            GoodsDO goodsDO = goodsService.selectOne(param);
            if (null == goodsDO) {
                logger.warn("查询结果为空!");
                throw new GoodsException(ExceptionEnum.GOODS_ID_NOT_EXIST, "查询结果为空");
            } else {
                return goodsDO;
            }
        } catch (IllegalArgumentException e) {
            logger.error("查询GoodsDO传入Id为空!",e);
            throw new GoodsException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (GoodsException e) {
            throw e;
        } catch (Exception e) {
            logger.error("根据ID=>[" + id + "]查询GoodsDO信息异常!",e);
            throw new GoodsException(ExceptionEnum.GOODS_QUERY_EXCEPTION, "根据ID=>[" + id + "]查询GoodsDO信息异常!");
        }
    }

    @Override
    public GoodsDO getEffectiveGoodsById(Long id) {
        return null;
    }

    @Override
    @CacheEvit(key="#id")
    public int upById(Long id) {
        try {
            Assert.notNull(id,"商品id不能为空!");
            GoodsDO goodsDO= new GoodsDO();
            goodsDO.setId(id);
            goodsDO.setIsUp(1);
            goodsDO.setUpTime(Calendar.getInstance().getTime());
            int result = goodsService.upOrDownById(goodsDO);
            if(result != 1){
                logger.error("上架商品ID=>[" + goodsDO.getId() + "]的GoodsDO异常!请求参数为:"+goodsDO);
                throw new GoodsException(ExceptionEnum.GOODS_UPDATE_EXCEPTION, "上架商品ID=>[" + goodsDO.getId() + "]的GoodsDO异常!");
            }
            logger.info("上架商品ID=>[" + goodsDO.getId() + "]的GoodsDO成功!");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("上架商品校验参数异常!",e);
            throw new GoodsException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (GoodsException e) {
            throw e;
        } catch (Exception e) {
            logger.error("上架商品ID=>[" + id + "]的GoodsDO信息异常",e);
            throw new GoodsException(ExceptionEnum.GOODS_UPDATE_EXCEPTION, "上架商品异常!");
        }
    }

    @Override
    @CacheEvit(key="#id")
    public int downById(Long id) {
        try {
            Assert.notNull(id,"商品id不能为空!");
            GoodsDO goodsDO= new GoodsDO();
            goodsDO.setId(id);
            goodsDO.setIsUp(0);
            goodsDO.setUpTime(Calendar.getInstance().getTime());
            int result = goodsService.upOrDownById(goodsDO);
            if(result != 1){
                logger.error("下架商品ID=>[" + goodsDO.getId() + "]的GoodsDO异常!请求参数为:"+goodsDO);
                throw new GoodsException(ExceptionEnum.GOODS_UPDATE_EXCEPTION, "下架商品ID=>[" + goodsDO.getId() + "]的GoodsDO异常!");
            }
            logger.info("下架商品ID=>[" + goodsDO.getId() + "]的GoodsDO成功!");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("下架商品校验参数异常!",e);
            throw new GoodsException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (GoodsException e) {
            throw e;
        } catch (Exception e) {
            logger.error("下架商品ID=>[" + id + "]的GoodsDO信息异常",e);
            throw new GoodsException(ExceptionEnum.GOODS_UPDATE_EXCEPTION, "下架商品异常!");
        }
    }

    @Override
    public int orderAssociationProcessing(Long goodsId, Integer quantity, int version) {
        return 0;
    }

    @Override
    public List<GoodsDO> getHotExchangeList(Long shopId, Long limit) {
        return null;
    }

    @Override
    public Pagenation<GoodsDO> queryGoodsDOListExceptRecommendForPage(GoodsDO queryModel, Pagenation<GoodsDO> page) {
        try {
            Assert.notNull(page, "分页参数不能为空");
            Assert.notNull(queryModel, "传入参数不能为空");
            //执行查询
            return  goodsService.queryGoodsDOListExceptRecommendForPage(queryModel,page);
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询goodsDO校验参数异常!", e);
            throw new GoodsException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (Exception e) {
            logger.error("多条件查询goodsDO信息异常!", e);
            throw new GoodsException(ExceptionEnum.GOODS_QUERY_EXCEPTION, "多条件查询goodsDO信息异常!");
        }
    }

    @Override
    public List<GoodsDO> getValueExchangeList(Long shopId) {
        return null;
    }

    @Override
    public boolean isOwnerOf(String[] goodsIdArray, Long shopId) {
        boolean result = false;
        List<Long> goodsId = new ArrayList<Long>();
        Map<String,Object> params = new HashMap<String,Object>();
        try {
            for(String id : goodsIdArray){
                goodsId.add(Long.valueOf(id));
            }
            params.put("shopId", shopId);
            params.put("idList", goodsId);
            if(goodsId != null && goodsId.size() > 0){
                int goodsSize = goodsService.isOwnerOf(params);
                if(goodsSize == goodsId.size()){
                    result = true;
                }
            }
        } catch (Exception e) {
            logger.error("验证商品权限异常!", e);
        }
        return result;
    }

    @Override
    public CouponDto checkEid(String eid) throws IOException, URISyntaxException {
        HttpBaseAck<CouponDto> resultAck = trCouponOperation.checkEid2(eid);
        if (resultAck.isSuccess() && null != resultAck.getData() && CouponDto.SUCCESS_CODE.equals(resultAck.getCode())) {
            return resultAck.getData();
        } else {
            throw new CouponException("41000", "虚拟卡券对应的批次号不存在!");
        }
    }

    private void validateForAdd(GoodsDO goodsDO) {
        Assert.isTrue(null!=goodsDO,"goodsDO不能为空!");
        Assert.hasText(goodsDO.getGoodsName(),"goodsDO对应的商品名称不能为空！");
        Assert.isTrue(null!=goodsDO.getShopId(),"goodsDO对应的shopId不能为空！");
        Assert.isTrue(null!=goodsDO.getCategory(),"goodsDO对应的category不能为空！");
        Assert.isTrue(null!=goodsDO.getPriceScore()&&goodsDO.getPriceScore()>0&&goodsDO.getPriceScore()<10000000l,"goodsDO对应的兑换价不能为空并且最大不超过99999999！");
        Assert.isTrue(null!=goodsDO.getStock()&&goodsDO.getStock()>0&&goodsDO.getStock()<100000000,"goodsDO对应的库存不能为空并且最大不超过99999999！");
        Assert.isTrue(null!=goodsDO.getStockWarn()&&goodsDO.getStockWarn()>0&&goodsDO.getStockWarn()<100000000,"goodsDO对应的库存预警不能为空并且最大不超过99999999！");
        Assert.hasText(goodsDO.getGoodsSn(),"goodsDO对应的商品编号不能为空！");
        Assert.hasText(goodsDO.getMediumImg(),"goodsDO对应的商品组图不能为空！");
        Assert.hasText(goodsDO.getContent(),"goodsDO对应的商品描述不能为空！");
    }

    private void validateForUpdate(GoodsDO goodsDO) {
        Assert.isTrue(null!=goodsDO,"goodsDO不能为空!");
        Assert.isTrue(null!=goodsDO.getId(),"goodsDO对应的商品编号不能为空!");
        Assert.hasText(goodsDO.getGoodsName(),"goodsDO对应的商品名称不能为空！");
        Assert.isTrue(null!=goodsDO.getShopId(),"goodsDO对应的shopId不能为空！");
        Assert.isTrue(null!=goodsDO.getCategory(),"goodsDO对应的category不能为空！");
        Assert.isTrue(null!=goodsDO.getPriceScore()&&goodsDO.getPriceScore()>0&&goodsDO.getPriceScore()<10000000l,"goodsDO对应的兑换价不能为空并且最大不超过99999999！");
        Assert.isTrue(null!=goodsDO.getStock()&&goodsDO.getStock()>0&&goodsDO.getStock()<100000000,"goodsDO对应的库存不能为空并且最大不超过99999999！");
        Assert.isTrue(null!=goodsDO.getStockWarn()&&goodsDO.getStockWarn()>0&&goodsDO.getStockWarn()<100000000,"goodsDO对应的库存预警不能为空并且最大不超过99999999！");
        Assert.hasText(goodsDO.getMediumImg(),"goodsDO对应的商品组图不能为空！");
        Assert.hasText(goodsDO.getContent(),"goodsDO对应的商品描述不能为空！");

    }

    private void validateGoods(GoodsDO goodsDO) {
        CategoryDO category = categoryBiz.getCategoryDOById(goodsDO.getCategory());
        if(Category.FINANCIAL_CARD.equals(category.getCode()) && StringUtils.isNotBlank(goodsDO.getBatchNumber())){
            try {
                CouponDto couponDto =checkEid(goodsDO.getBatchNumber());
                if (!ExternalserviceResultCodeConstants.TrCoupon.SUCCESS.equals(couponDto.getCode())) {
                    throw new CardCouponException(COUPON_QUERY_EXCEPTION,"批次号:"+ goodsDO.getBatchNumber() +"虚拟卡券不存在!");
                }
                if (DateUtils.isSameDay(goodsDO.getValidStartTime(), couponDto.getData().getStartTime()) && DateUtils.isSameDay(goodsDO.getValidEndTime(), couponDto.getData().getEndTime())) {
                    logger.info("虚拟卡券领取有效期检查通过");
                }else{
                    throw new CardCouponException(COUPON_QUERY_EXCEPTION,"虚拟卡券领取有效期检查不通过!");
                }
                if(!goodsDO.getAutoUpTime().before(goodsDO.getValidStartTime()) && !goodsDO.getValidEndTime().before(goodsDO.getAutoDownTime()) && goodsDO.getAutoUpTime().before(goodsDO.getAutoDownTime())){
                    logger.info("虚拟卡券启用有效期检查通过");
                }else{
                    throw new CardCouponException(COUPON_QUERY_EXCEPTION,"虚拟卡券启用有效期检查不通过!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else if(Category.EXTERNAL_CARD.equals(category.getCode()) && StringUtils.isNotBlank(goodsDO.getBatchNumber())){
            //判断虚拟批次号对应的卡券是否存在！
            CardCouponsDO cardCoupon = couponsBiz.selectByBatchNumer(goodsDO.getShopId(), goodsDO.getBatchNumber());
            if(null == cardCoupon){
                throw new CardCouponException(COUPON_QUERY_EXCEPTION,"批次号:" + goodsDO.getBatchNumber() + "对应的外部卡券不存在!");
            }
            //判断相对库存是否充足
            if(goodsDO.getStock() > cardCoupon.getStock()){
                throw new CardCouponException(COUPON_QUERY_EXCEPTION,"批次号:" + goodsDO.getBatchNumber() + "对应的外部卡券库存不充足!");
            }
            //TODO 判断绝对库存是否充足
        }
    }


}

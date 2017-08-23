package org.trc.biz.impl.goods;

import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.TrCouponOperation;
import com.trc.mall.externalservice.dto.CouponDto;
import com.trc.mall.util.GuidUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.trc.annotation.cache.CacheEvit;
import org.trc.biz.goods.ICategoryBiz;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.biz.goods.IGoodsBiz;
import org.trc.biz.goods.IGoodsRecommendBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.Category;
import org.trc.constants.ExternalserviceResultCodeConstants;
import org.trc.domain.goods.*;
import org.trc.domain.query.GoodsQuery;
import org.trc.domain.shop.ShopDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.exception.CardCouponException;
import org.trc.exception.CouponException;
import org.trc.exception.GoodsException;
import org.trc.service.goods.*;
import org.trc.task.SMSTask;
import org.trc.util.Pagenation;
import org.trc.util.ThreadPoolUtil;

import javax.annotation.Resource;
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

    @Resource
    private IGoodsSnapshotService goodsSnapshotService;

    @Autowired
    private TrCouponOperation trCouponOperation;
    @Autowired
    private IShopClassificationService shopClassificationService;
    @Autowired
    private IPurchaseRestrictionsService purchaseRestrictionsService;
    @Autowired
    private IGoodsClassificationRelationshipService goodsClassificationRelationshipService;
    @Autowired
    private IShopBiz shopBiz;

    @Override
    @CacheEvit(key="#goodsDO.id")
    public int saveGoodsDO(GoodsDO goodsDO) {
        try {
            goodsDO.setGoodsSn(GuidUtil.getNextUid("pro"));
            validateForAdd(goodsDO);
            validateGoods(goodsDO);
            int result = goodsService.insertSelective(goodsDO);
            logger.info("新增ID=>[" + goodsDO.getId() + "]的GoodsDO成功");
            List<GoodsClassificationRelationshipDO> list = goodsDO.getGoodsClassificationRelationshipList();
            if(CollectionUtils.isNotEmpty(list)) {
                for (GoodsClassificationRelationshipDO item : list) {
                    item.setGoodsId(goodsDO.getId());
                }
                goodsClassificationRelationshipService.batchInsert(list);
            }
            PurchaseRestrictionsDO item = new PurchaseRestrictionsDO();
            item.setGoodsId(goodsDO.getId());
            item.setLimitQuantity(goodsDO.getLimitQuantity());
            purchaseRestrictionsService.deal(item);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("新增GoodsDO校验参数异常!",e);
            throw new GoodsException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (CardCouponException e) {
            throw e;
        }catch (GoodsException e) {
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
    @Transactional
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
            GoodsClassificationRelationshipDO param = new GoodsClassificationRelationshipDO();
            param.setGoodsId(goodsDO.getId());
            goodsClassificationRelationshipService.delete(param);
            List<GoodsClassificationRelationshipDO> list = goodsDO.getGoodsClassificationRelationshipList();
            if(CollectionUtils.isNotEmpty(list)) {
                goodsClassificationRelationshipService.batchInsert(list);
            }
            logger.info("修改ID=>[" + goodsDO.getId() + "]的GoodsDO成功!");
            PurchaseRestrictionsDO item = new PurchaseRestrictionsDO();
            item.setGoodsId(goodsDO.getId());
            item.setLimitQuantity(goodsDO.getLimitQuantity());
            purchaseRestrictionsService.deal(item);
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
    public Pagenation<GoodsDO> queryGoodsDOListForClassification(GoodsQuery goodsQuery, Pagenation<GoodsDO> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(goodsQuery, "传入参数不能为空");
            Pagenation<GoodsDO> page= goodsService.selectListByClassification(goodsQuery, pageRequest);
            if(page != null && page.getInfos() != null && page.getInfos().size() > 0) {
                for(GoodsDO item : page.getInfos()){
                    List<ShopClassificationDO> shopClassifications = _listShopClassificationByGoodsId(item.getId());
                    item.setShopClassificationList(shopClassifications);
                    PurchaseRestrictionsDO param = new PurchaseRestrictionsDO();
                    param.setGoodsId(item.getId());
                    PurchaseRestrictionsDO result = purchaseRestrictionsService.selectOne(param);
                    item.setLimitQuantity(null != result ? result.getLimitQuantity() : -1);
                }
            }
            return page;
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询GoodsDO校验参数异常!",e);
            throw new GoodsException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (Exception e) {
            logger.error("多条件查询GoodsDO信息异常!", e);
            throw new GoodsException(ExceptionEnum.QUERY_LIST_EXCEPTION,"多条件查询GoodsDO信息异常!");
        }
    }

    private List<ShopClassificationDO> _listShopClassificationByGoodsId(Long goodsId){
        GoodsClassificationRelationshipDO param = new GoodsClassificationRelationshipDO();
        param.setGoodsId(goodsId);
        return shopClassificationService.listEntityByParam(param);
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
    public GoodsDO getGoodsDOByParam(GoodsDO goodsDO) {
        return goodsService.selectOne(goodsDO);
    }

    @Override
    public GoodsDO getGoodsDOById(Long id, Integer whetherPrizes, Integer isUp) {
        try {
            Assert.isTrue(id != null,"查询Id不能为空!");
            GoodsDO param = new GoodsDO();
            param.setIsUp(isUp);
            param.setWhetherPrizes(whetherPrizes);
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
            throw new CouponException(ExceptionEnum.COUPON_QUERY_EXCEPTION, "虚拟卡券对应的批次号不存在!");
        }
    }

    @Override
    public void setClassification(List<Long> goodsList, List<GoodsClassificationRelationshipDO> gcrList) {
        try {
            goodsClassificationRelationshipService.batchDeleteByGoodsIds(goodsList);
            goodsClassificationRelationshipService.batchInsert(gcrList);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new BusinessException(ExceptionEnum.UPDATE_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public int prizeOrderAssociationProcessing(Long goodsId, Integer quantity, int version) {
        try {
            //修改奖品库存数与兑换数
            GoodsDO goodsDO = new GoodsDO();
            goodsDO.setId(goodsId);
            goodsDO.setExchangeQuantity(quantity);
            goodsDO.setVersionLock(version);
            //判断是否需要生成快照
            GoodsDO originalGoodsDO = new GoodsDO();
            originalGoodsDO.setId(goodsId);
            originalGoodsDO.setWhetherPrizes(1);
            originalGoodsDO = goodsService.selectOne(originalGoodsDO);
            if(null != originalGoodsDO && null != originalGoodsDO.getUpdateTime() && originalGoodsDO.getUpdateTime().after(originalGoodsDO.getSnapshotTime())){
                GoodsSnapshotDO goodsSnapshotDO = _convertToGoodsSnapshotDO(originalGoodsDO);
                goodsSnapshotService.insertSelective(goodsSnapshotDO);
                goodsDO.setSnapshotTime(new Date());
            }
            //1.判断奖品库存
            int remaining = originalGoodsDO.getStock() - goodsDO.getExchangeQuantity();
            //2.操作库存
            int result = goodsService.orderAssociationProcessing(goodsDO);
            //3.到预警值
            if(originalGoodsDO.getStockWarn() >= remaining)	{
                ShopDO shopDo = shopBiz.getShopDOById(originalGoodsDO.getShopId());
                ThreadPoolUtil.execute(new SMSTask("商品(" + originalGoodsDO.getGoodsName() + ")达到库存预警",shopDo.getWarnPhone()));
            }
            if(remaining < 0){
                logger.info("奖品:"+originalGoodsDO.getGoodsName()+"库存不充足，请补足!");
                throw new BusinessException(ExceptionEnum.GOODS_INVENTORY_SHORTAGE, "商品库存不足，请补足!");
            }
            if(result != 1){
                logger.error("奖品订单相关操作ID=>[" + goodsId + "]的GoodsDO异常!");
                throw new BusinessException(ExceptionEnum.UPDATE_EXCEPTION, "奖品订单相关操作ID=>[" + goodsId + "]的GoodsDO异常!");
            }
            logger.info("奖品订单相关操作ID=>[" + goodsId + "]的GoodsDO成功!");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("奖品订单相关操作校验参数异常!",e);
            throw new BusinessException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (Exception e) {
            logger.error("奖品订单相关操作ID=>[" + goodsId + "]的GoodsDO信息异常",e);
            throw new BusinessException(ExceptionEnum.UPDATE_EXCEPTION, "奖品订单相关操作ID=>[" + goodsId + "]的GoodsDO信息异常");
        }
    }

    private GoodsSnapshotDO _convertToGoodsSnapshotDO(GoodsDO goodsDO) {
        GoodsSnapshotDO goodsSnapshotDO = new GoodsSnapshotDO();
        goodsSnapshotDO.setGoodsId(goodsDO.getId());
        goodsSnapshotDO.setShopId(goodsDO.getShopId());
        goodsSnapshotDO.setCategory(goodsDO.getCategory());
        goodsSnapshotDO.setVersion(goodsDO.getVersionLock());
        goodsSnapshotDO.setBrandName(goodsDO.getBrandName());
        goodsSnapshotDO.setGoodsName(goodsDO.getGoodsName());
        goodsSnapshotDO.setBatchNumber(goodsDO.getBatchNumber());
        goodsSnapshotDO.setGoodsSn(goodsDO.getGoodsSn());
        goodsSnapshotDO.setMainImg(goodsDO.getMainImg());
        goodsSnapshotDO.setMediumImg(goodsDO.getMediumImg());
        goodsSnapshotDO.setPriceMarket(goodsDO.getPriceMarket());
        goodsSnapshotDO.setPriceScore(goodsDO.getPriceScore());
        goodsSnapshotDO.setTargetUrl(goodsDO.getTargetUrl());
        goodsSnapshotDO.setContent(goodsDO.getContent());
        goodsSnapshotDO.setValidStartTime(goodsDO.getValidStartTime());
        goodsSnapshotDO.setValidEndTime(goodsDO.getValidEndTime());
        goodsSnapshotDO.setCreateTime(new Date());
        return goodsSnapshotDO;
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
//        Assert.hasText(goodsDO.getContent(),"goodsDO对应的商品描述不能为空！");
    }

    private void validateForUpdate(GoodsDO goodsDO) {
        Assert.isTrue(null!=goodsDO,"goodsDO不能为空!");
        Assert.isTrue(null!=goodsDO.getId(),"goodsDO对应的商品编号不能为空!");
        Assert.hasText(goodsDO.getGoodsName(),"goodsDO对应的商品名称不能为空！");
        Assert.isTrue(null!=goodsDO.getShopId(),"goodsDO对应的shopId不能为空！");
        Assert.isTrue(null!=goodsDO.getCategory(),"goodsDO对应的category不能为空！");
        Assert.isTrue(null!=goodsDO.getPriceScore()&&goodsDO.getPriceScore()>0&&goodsDO.getPriceScore()<10000000l,"goodsDO对应的兑换价必须大于0并且最大不超过99999999！");
        Assert.isTrue(null!=goodsDO.getStock()&&goodsDO.getStock()>0&&goodsDO.getStock()<100000000,"goodsDO对应的库存必须大于0并且最大不超过99999999！");
        Assert.isTrue(null!=goodsDO.getStockWarn()&&goodsDO.getStockWarn()>0&&goodsDO.getStockWarn()<100000000,"goodsDO对应的库存预警必须大于0并且最大不超过99999999！");
        Assert.hasText(goodsDO.getMediumImg(),"goodsDO对应的商品组图不能为空！");
//        Assert.hasText(goodsDO.getContent(),"goodsDO对应的商品描述不能为空！");

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
                logger.error("查询服务不可用!");
                throw new CardCouponException(COUPON_QUERY_EXCEPTION,"查询服务不可用!");
            } catch (URISyntaxException e) {
                e.printStackTrace();
                logger.error("查询服务不可用!");
                throw new CardCouponException(COUPON_QUERY_EXCEPTION,"查询服务不可用!");
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

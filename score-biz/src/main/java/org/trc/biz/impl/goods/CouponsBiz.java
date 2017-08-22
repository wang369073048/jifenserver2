package org.trc.biz.impl.goods;

import com.txframework.util.Assert;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.constants.Category;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CardItemDO;
import org.trc.domain.goods.CategoryDO;
import org.trc.domain.goods.GoodsDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.exception.CardCouponException;
import org.trc.form.goods.CardCouponsForm;
import org.trc.service.goods.*;
import org.trc.util.DateUtils;
import org.trc.util.Pagenation;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Service("couponsBiz")
public class CouponsBiz implements ICouponsBiz{
    private Logger logger = LoggerFactory.getLogger(CouponsBiz.class);
    @Autowired
    private ICouponsService couponsService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ICardItemAbandonedService cardItemAbandonedService;
    @Autowired
    private ICardItemService cardItemService;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public int deleteByBatchNumber(CardCouponsDO cardCoupon) {
        try{
            Assert.notNull(cardCoupon, "查询参数不能为空！");
            Assert.notNull(cardCoupon.getBatchNumber(), "批次号不能为空！");
            //1、上架商品使用此卡券时，无法删除
            GoodsDO param = new GoodsDO();
            param.setBatchNumber(cardCoupon.getBatchNumber());
            CategoryDO category = new CategoryDO();
            category.setCode(Category.EXTERNAL_CARD);
            category.setIsDeleted(false);
            category = categoryService.selectOne(category);
            param.setCategory(category.getId());
            param.setIsUp(1);
            int count = goodsService.selectCount(param);
            if(count > 0){
                throw new CardCouponException(ExceptionEnum.COUPON_DELETE_EXCEPTION,"该批次卡券无法删除，请先下架对应的外部优惠券商品!");
            }
            //2、将卡券状态设置为删除状态
            couponsService.deleteByBatchNumber(cardCoupon);
            CardItemDO cardItem = new CardItemDO();
            cardItem.setBatchNumber(cardCoupon.getBatchNumber());
            cardItem.setShopId(cardCoupon.getShopId());
            //3、将未发放的卡券明细移动到废弃明细表
            int cardItemAbandonedCount = cardItemAbandonedService.selectIntoFromCardItem(cardItem);
            //4、同步删除卡券明细(物理删除)
            int cardItemDeletedCount = cardItemService.deleteByBatchNumber(cardItem);
            if (cardItemAbandonedCount != cardItemDeletedCount) {
                throw new CardCouponException(ExceptionEnum.COUPON_DELETE_EXCEPTION, "删除卡券出现异常，废弃条数与物理删除条数不匹配!");
            }
            return cardItemAbandonedCount;
        } catch (CardCouponException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("删除卡券校验参数异常!", e);
            throw new CardCouponException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        }catch (Exception e) {
            logger.error("删除卡券异常!请求参数为:"+ cardCoupon, e);
            throw new CardCouponException(ExceptionEnum.COUPON_DELETE_EXCEPTION, "删除卡券异常!");
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public int deleteItemById(CardItemDO cardItem) {
        try{
            //1、将未发放的卡券明细移动到废弃明细表
            int cardItemAbandonedCount = cardItemAbandonedService.selectIntoFromCardItem(cardItem);
            //2、同步删除卡券明细(物理删除)
            int cardItemDeletedCount = cardItemService.deleteById(cardItem);
            if(cardItemAbandonedCount != cardItemDeletedCount){
                throw new CardCouponException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "删除卡券出现异常，废弃条数与物理删除条数不匹配!");
            }
            return cardItemAbandonedCount;
        } catch (CardCouponException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除卡券明细异常!请求参数为:"+ cardItem, e);
            throw new CardCouponException(ExceptionEnum.COUPON_DELETE_EXCEPTION, "删除卡券明细异常!");
        }

    }

    @Override
    public int insert(CardCouponsDO cardCoupons) {
        try{
            if(null != cardCoupons && StringUtils.isBlank(cardCoupons.getBatchNumber())) {
                cardCoupons.setBatchNumber(RandomStringUtils.randomAlphanumeric(16));
            }
            cardCoupons.setStock(0);//库存默认0
            cardCoupons.setVersion(1L);//版本号默认1
            int result = couponsService.insertSelective(cardCoupons);
            if (result < 0 ){
                logger.error("新增卡券异常!请求参数为 :" + cardCoupons);
                throw new CardCouponException(ExceptionEnum.COUPON_SAVE_EXCEPTION, "新增卡券异常!");
            }
            logger.info("新增ID=>[{}]的CardCouponsDO成功",cardCoupons.getId());
            return result;
        } catch (CardCouponException e) {
            throw e;
        }catch (Exception e) {
            logger.error("新增卡券异常!请求参数为 :" + cardCoupons + ",异常信息为" + e.getMessage());
            throw new CardCouponException(ExceptionEnum.COUPON_SAVE_EXCEPTION, "新增卡券异常!");
        }

    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public int importCardItem(String batchNumber, Long shopId, List<CardItemDO> cardItemList) {
    	validateCardItems(cardItemList);//add by xab 2017-08-08
        CardCouponsDO cardCoupons = new CardCouponsDO();
        cardCoupons.setBatchNumber(batchNumber);
        cardCoupons.setIsDeleted(0);
        CardCouponsDO cardCoupon = couponsService.selectOne(cardCoupons);
        if(null == cardCoupon){
            throw new CardCouponException(ExceptionEnum.COUPON_QUERY_EXCEPTION, "批次号:" + batchNumber + "对应的卡券不存在!");
        }
        
        int addQuantity = 0;
        try {
            //批量插入卡券
            addQuantity = cardItemService.batchInsert(cardItemList);
        }catch (DataAccessException e){
            logger.error("批量插入发生异常!");
            throw new CardCouponException(ExceptionEnum.COUPON_SAVE_EXCEPTION, "批量导入异常:"+e.getMessage());
        }
        //更新卡券明细
        cardCoupon.setStock(cardCoupon.getStock()+addQuantity);
        couponsService.updateStockById(cardCoupon);
        return addQuantity;
    }
    
    private void validateCardItems(List<CardItemDO> cardItemList) {
    	if(null == cardItemList || cardItemList.size() <= 0){
            throw new CardCouponException(ExceptionEnum.COUPON_CODE_IMPORT_EXCEPTION, "导入文件数据为空，请检查文件!");
        }
    	if(cardItemList.size() > 10000 ){
            throw new CardCouponException(ExceptionEnum.COUPON_CODE_IMPORT_EXCEPTION, "系统一次导入量最大为10000条，请检查数据重新导入!");
        }
    	List<CardItemDO> duplicateCardItem = cardItemService.checkCardItem(cardItemList);
        StringBuilder duplicateCode = new StringBuilder("");
        for(CardItemDO cardItem : duplicateCardItem){
            duplicateCode.append(cardItem.getCode()).append("       ");
        }
        throw new CardCouponException(ExceptionEnum.COUPON_CODE_IMPORT_EXCEPTION, "批量导入卡券失败!券码重复:"+duplicateCode.toString());
    }

    @Override
    public Pagenation<CardCouponsDO> queryCouponsForPage(CardCouponsForm queryModel, Pagenation<CardCouponsDO> page) {
        try {
            Assert.notNull(page, "分页参数不能为空");
            Assert.notNull(queryModel, "查询参数不能为空");
            Example example = new Example(CardCouponsDO.class);
            Example.Criteria criteria = example.createCriteria();
            if (null != queryModel.getShopId()){ //商铺ID
                criteria.andEqualTo("shopId",queryModel.getShopId());
            }
            if (StringUtils.isNotBlank(queryModel.getCouponName())){ //banner名称
                criteria.andEqualTo("couponName",queryModel.getCouponName());
            }
            if (StringUtils.isNotBlank(queryModel.getBatchNumber())){ //banner名称
                criteria.andEqualTo("batchNumber",queryModel.getBatchNumber());
            }
            if (queryModel.getStartTime() != null) {//开始日期
                criteria.andGreaterThanOrEqualTo("createTime", new Date(queryModel.getStartTime()));
            }
            if (queryModel.getEndTime() != null) {//截止日期
                Date endTime = new Date(queryModel.getEndTime());
                criteria.andLessThan("createTime", DateUtils.addDays(endTime, 1));
            }
            criteria.andEqualTo("isDeleted",0);
            example.orderBy("createTime").asc();
            page = couponsService.pagination(example,page,queryModel);
            return page;
        } catch (IllegalArgumentException e){
            logger.error("查询卡券列表校验参数异常!", e);
            throw new CardCouponException(ExceptionEnum.PARAM_ERROR_ILLEGAL, "查询卡券列表校验参数异常!");
        } catch (Exception e) {
            logger.error("查询卡券列表失败!", e);
            throw new CardCouponException(ExceptionEnum.COUPON_QUERY_EXCEPTION, "查询卡券列表失败!");
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public List<CardItemDO> releaseCardCoupons(CardItemDO cardItem) {
        cardItemService.releaseCardCoupons(cardItem);
        //更新卡券库存
        CardCouponsDO param = new CardCouponsDO();
        param.setBatchNumber(cardItem.getBatchNumber());
        CardCouponsDO old = couponsService.selectOne(param);
        Integer stock = old.getStock() - cardItem.getQuantity();
        if(stock < 0){
            throw new BusinessException(ExceptionEnum.COUPON_UNDER_STOCK_EXCEPTION, "卡券库存不足!");
        }
        param.setId(old.getId());
        param.setStock(stock);
        param.setVersion(old.getVersion());
        param.setUpdateTime(cardItem.getReleaseTime());
        couponsService.updateStockById(param);
        return cardItemService.select(cardItem);
    }

    @Override
    public CardCouponsDO selectByBatchNumer(Long shopId, String batchNumber) {
        CardCouponsDO cardCouponsDO = new CardCouponsDO();
        cardCouponsDO.setShopId(shopId);
        cardCouponsDO.setBatchNumber(batchNumber);
        cardCouponsDO.setIsDeleted(0);
        return couponsService.selectOne(cardCouponsDO);
    }

    @Override
    public CardItemDO selectItemByCode(Long shopId, String batchNumber, String code) {
        CardItemDO cardItem = new CardItemDO();
        cardItem.setBatchNumber(batchNumber);
        cardItem.setCode(code);
        cardItem.setShopId(shopId);
        cardItem = cardItemService.selectByParams(cardItem);
        if(cardItem == null) {
            throw new CardCouponException(ExceptionEnum.COUPON_QUERY_EXCEPTION,code + "对应的卡券不存在");
        }
        return cardItemService.selectByParams(cardItem);
    }

    @Override
    public int updateById(CardCouponsDO cardCoupons) {
        return 0;
    }

    @Override
    public List<CardItemDO> selectItemByOrderNum(String orderNum) {
        CardItemDO cardItemDO = new CardItemDO();
        cardItemDO.setOrderCode(orderNum);
        return cardItemService.select(cardItemDO);
    }
}

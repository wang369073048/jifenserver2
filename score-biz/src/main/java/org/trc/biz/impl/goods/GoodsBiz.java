package org.trc.biz.impl.goods;

import com.trc.mall.externalservice.TrCouponAck;
import com.trc.mall.externalservice.TrCouponOperation;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.biz.goods.ICategoryBiz;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.biz.goods.IGoodsBiz;
import org.trc.constants.Category;
import org.trc.constants.ExternalserviceResultCodeConstants;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CategoryDO;
import org.trc.domain.goods.GoodsDO;
import org.trc.exception.CouponException;
import org.trc.util.Pagenation;

import java.util.List;

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

    //@Autowired
    private TrCouponOperation trCouponOperation;

    @Override
    public int saveGoodsDO(GoodsDO goodsDO) {
        return 0;
    }

    @Override
    public int updateGoodsDO(GoodsDO goodsDO) {
        return 0;
    }

    @Override
    public int deleteGoodsDO(GoodsDO goodsDO) {
        return 0;
    }

    @Override
    public Pagenation<GoodsDO> queryGoodsDOListForPage(GoodsDO goodsDO, Pagenation<GoodsDO> page) {
        return null;
    }

    @Override
    public Pagenation<GoodsDO> queryGoodsDOListForUser(GoodsDO goodsDO, Pagenation<GoodsDO> page) {
        return null;
    }

    @Override
    public GoodsDO getGoodsDOById(Long id, Integer isUp) {
        return null;
    }

    @Override
    public GoodsDO getEffectiveGoodsById(Long id) {
        return null;
    }

    @Override
    public int upById(Long id) {
        return 0;
    }

    @Override
    public int downById(Long id) {
        return 0;
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
    public Pagenation<GoodsDO> queryGoodsDOListExceptRecommendForPage(GoodsDO query, Pagenation<GoodsDO> page) {
        return null;
    }

    @Override
    public List<GoodsDO> getValueExchangeList(Long shopId) {
        return null;
    }

    @Override
    public boolean isOwnerOf(String[] goodsIdArray, Long shopId) {
        return false;
    }

    @Override
    public TrCouponAck checkEid(String eid) {
        return null;
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

    private void validateGoods(GoodsDO goodsDO) {
        CategoryDO category = categoryBiz.getCategoryDOById(goodsDO.getCategory());
        if(Category.FINANCIAL_CARD.equals(category.getCode()) && StringUtils.isNotBlank(goodsDO.getBatchNumber())){
            TrCouponAck trCouponAck = trCouponOperation.checkEid2(goodsDO.getBatchNumber());
            if(!ExternalserviceResultCodeConstants.TrCoupon.SUCCESS.equals(trCouponAck.getCode())){
                throw new CouponException(COUPON_QUERY_EXCEPTION,"批次号:"+ goodsDO.getBatchNumber() +"虚拟卡券不存在!");
            }
            if(DateUtils.isSameDay(goodsDO.getValidStartTime(),trCouponAck.getStartTime()) && DateUtils.isSameDay(goodsDO.getValidEndTime(),trCouponAck.getEndTime())){
                logger.info("虚拟卡券领取有效期检查通过");
            }else{
                throw new CouponException(COUPON_QUERY_EXCEPTION,"虚拟卡券领取有效期检查不通过!");
            }
            if(!goodsDO.getAutoUpTime().before(goodsDO.getValidStartTime()) && !goodsDO.getValidEndTime().before(goodsDO.getAutoDownTime()) && goodsDO.getAutoUpTime().before(goodsDO.getAutoDownTime())){
                logger.info("虚拟卡券启用有效期检查通过");
            }else{
                throw new CouponException(COUPON_QUERY_EXCEPTION,"虚拟卡券启用有效期检查不通过!");
            }
        }else if(Category.EXTERNAL_CARD.equals(category.getCode()) && StringUtils.isNotBlank(goodsDO.getBatchNumber())){
            //判断虚拟批次号对应的卡券是否存在！
            CardCouponsDO cardCoupon = couponsBiz.selectByBatchNumer(goodsDO.getShopId(), goodsDO.getBatchNumber());
            if(null == cardCoupon){
                throw new CouponException(COUPON_QUERY_EXCEPTION,"批次号:" + goodsDO.getBatchNumber() + "对应的外部卡券不存在!");
            }
            //判断相对库存是否充足
            if(goodsDO.getStock() > cardCoupon.getStock()){
                throw new CouponException(COUPON_QUERY_EXCEPTION,"批次号:" + goodsDO.getBatchNumber() + "对应的外部卡券库存不充足!");
            }
            //TODO 判断绝对库存是否充足
        }
    }


}

package org.trc.biz.impl.goods;

import com.txframework.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CardItemDO;
import org.trc.domain.pagehome.Banner;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.CardCouponException;
import org.trc.form.goods.CardCouponsForm;
import org.trc.service.goods.ICouponsService;
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
    @Override
    public int deleteByBatchNumber(CardCouponsDO cardCoupon) {
        return 0;
    }

    @Override
    public int deleteItemById(CardItemDO cardItem) {
        return 0;
    }

    @Override
    public int insert(CardCouponsDO cardCoupons) {
        return 0;
    }

    @Override
    public int importCardItem(String batchNumber, Long shopId, List<CardItemDO> cardItemList) {
        return 0;
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
            if (StringUtil.isNotEmpty(queryModel.getStartDate())) {//开始日期
                criteria.andGreaterThanOrEqualTo("createTime", DateUtils.parseDate(queryModel.getStartDate()));
            }
            if (StringUtil.isNotEmpty(queryModel.getEndDate())) {//截止日期
                Date endDate = DateUtils.parseDate(queryModel.getEndDate());
                criteria.andLessThan("createTime", DateUtils.addDays(endDate, 1));
            }
            example.orderBy("createTime").desc();
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
    public List<CardItemDO> releaseCardCoupons(CardItemDO cardItem) {
        return null;
    }

    @Override
    public CardCouponsDO selectByBatchNumer(Long shopId, String batchNumber) {
        return null;
    }

    @Override
    public CardItemDO selectItemByCode(Long shopId, String batchNumber, String code) {
        return null;
    }

    @Override
    public int updateById(CardCouponsDO cardCoupons) {
        return 0;
    }

    @Override
    public List<CardItemDO> selectItemByOrderNum(String orderNum) {
        return null;
    }
}

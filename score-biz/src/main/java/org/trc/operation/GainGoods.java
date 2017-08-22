package org.trc.operation;

import com.alibaba.fastjson.JSONObject;
import com.txframework.util.Assert;
import com.txframework.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.admin.IRequestFlowBiz;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.biz.goods.IGoodsBiz;
import org.trc.constants.*;
import org.trc.domain.admin.RequestFlow;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CardItemDO;
import org.trc.domain.goods.CategoryDO;
import org.trc.domain.goods.GoodsDO;
import org.trc.domain.luckydraw.WinningRecordDO;
import org.trc.domain.order.OrdersDO;
import org.trc.exception.BusinessException;
import org.trc.framework.core.spring.SpringContextHolder;
import org.trc.service.goods.ICategoryService;
import org.trc.service.goods.ICouponsService;
import org.trc.service.goods.IGoodsService;
import org.trc.service.luckydraw.IWinningRecordService;
import org.trc.service.order.IOrderService;
import org.trc.task.SMSTask;
import org.trc.util.ThreadPoolUtil;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by george on 2017/7/19.
 */
public class GainGoods implements Runnable{

    private Logger logger = LoggerFactory.getLogger(GainGoods.class);

    private static IGoodsBiz goodsBiz;

    private static ICategoryService categoryService;

    private static ICouponsBiz couponsBiz;

    private static IOrderService ordersService;

    private static IWinningRecordService winningRecordService;

    private static GainFinancialCard gainFinancialCard;

    private static IRequestFlowBiz requestFlowBiz;

    static {
        goodsBiz = (IGoodsBiz) SpringContextHolder.getBean("goodsBiz");
        categoryService = (ICategoryService) SpringContextHolder.getBean("categoryService");
        couponsBiz = (ICouponsBiz) SpringContextHolder.getBean("couponsBiz");
        ordersService = (IOrderService) SpringContextHolder.getBean("ordersService");
        winningRecordService = (IWinningRecordService) SpringContextHolder.getBean("winningRecordService");
        gainFinancialCard = (GainFinancialCard) SpringContextHolder.getBean("gainFinancialCard");
        requestFlowBiz = (IRequestFlowBiz)SpringContextHolder.getBean("requestFlowBiz");
    }

    private WinningRecordDO winningRecord;

    public GainGoods(WinningRecordDO winningRecord){
        this.winningRecord = winningRecord;
    }

    private boolean _validate(WinningRecordDO winningRecord){
        try {
            Assert.isTrue(null != winningRecord, "中奖记录不能为空!");
            Assert.isTrue(null != winningRecord.getState(), "中奖状态不能为空!");
            Assert.isTrue(null != winningRecord.getGoodsId(), "中奖商品id不能为空!");
            Assert.isTrue(null != winningRecord.getExpenditure(), "消耗积分不能为空!");
            Assert.isTrue(null != winningRecord.getNumberOfPrizes(), "中奖商品数量不能为空!");
            Assert.isTrue(StringUtils.isNotBlank(winningRecord.getUserId()), "中奖用户不能为空!");
            Assert.isTrue(StringUtils.isNotBlank(winningRecord.getLotteryPhone()), "中奖手机号不能为空!");
            return true;
        } catch (Exception e){
            logger.error("中奖记录参数校验未通过!winningRecord:" + winningRecord.toString());
            return false;
        }
    }

    public void run() {
        if(!_validate(winningRecord)){
            return ;
        }
        if(LuckyDraw.WinningState.ALREADY_ISSUED == winningRecord.getState()){
            logger.info("奖品发放已完成,不能再次发放!");
            return;
        }
        //生成订单
        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setId(winningRecord.getGoodsId());
        goodsDO.setWhetherPrizes(1);
        GoodsDO goods = goodsBiz.getGoodsDOByParam(goodsDO);
        CategoryDO category = categoryService.getCategoryDOById(goods.getCategory());
        Date createTime = Calendar.getInstance().getTime();
        OrdersDO ordersDO;
        try {
            if (winningRecord.getState().equals(LuckyDraw.WinningState.UNISSUED) || winningRecord.getState().equals(LuckyDraw.WinningState.PRIZE_ORDER_IS_NOT_GENERATED)) {
                ordersDO = _createOrder(winningRecord, goods, category, createTime);
            } else if (winningRecord.getState().equals(LuckyDraw.WinningState.PAYMENT_FAILURE)) {
                OrdersDO param = new OrdersDO();
                param.setOrderNum(winningRecord.getOrderNum());
                ordersDO = ordersService.selectOne(param);
            } else {
                logger.error("中奖记录状态暂不需要处理!" + winningRecord.toString());
                return;
            }
        } catch (BusinessException e){
            logger.error("奖品库存不足，订单生成失败!");
            winningRecord.setState(LuckyDraw.WinningState.PRIZE_ORDER_IS_NOT_GENERATED);
            winningRecordService.updateState(winningRecord);
            return;
        } catch (Exception e){
            logger.error("抽奖订单生成失败，发生未知!");
            winningRecord.setState(LuckyDraw.WinningState.PRIZE_ORDER_UNKNOWN_ERROR);
            winningRecordService.updateState(winningRecord);
            return;
        }
        if(0 == category.getIsVirtual()){
            return;
        }
        switch (category.getCode()) {
            case Category.EXTERNAL_CARD:
                //TODO 暂时不做补偿
                //外部卡券兑换
                CardItemDO ciParam = new CardItemDO();
                try {
                    ciParam.setShopId(ordersDO.getShopId());
                    ciParam.setState(1);
                    ciParam.setOrderCode(winningRecord.getRequestNo());
                    ciParam.setUserId(ordersDO.getUserId());
                    ciParam.setBatchNumber(goods.getBatchNumber());
                    ciParam.setQuantity(ordersDO.getGoodsCount());
                    ciParam.setReleaseTime(createTime);
                    //发券
                    List<CardItemDO> cardItemList = couponsBiz.releaseCardCoupons(ciParam);
                    StringBuilder couponCode = new StringBuilder("");
                    for (CardItemDO cardItem : cardItemList) {
                        couponCode.append(cardItem.getCode()).append("  ");
                    }
                    //调用短信发送
                    CardCouponsDO cardCouponsDO = couponsBiz.selectByBatchNumer(ordersDO.getShopId(), goods.getBatchNumber());
                    String smsContentPattern = "亲爱的{0}，您已成功兑换{1}共{2}份，兑换码为{3}，有效期至{4}，请于有效期内尽快使用。使用方法及订单详情，请前往泰然城积分商城查看，泰然城服务热线0571-96056。请勿轻易转发，谨防诈骗。。";
                    String smsContent = MessageFormat.format(smsContentPattern, ordersDO.getUsername(), goods.getGoodsName(), cardItemList.size(), couponCode.toString(),
                            DateUtils.format(cardCouponsDO.getValidEndTime(), DateUtils.TIME_PATTERN));
                    ThreadPoolUtil.execute(new SMSTask(smsContent, ordersDO.getUsername()));
                    OrdersDO param = new OrdersDO();
                    param.setId(ordersDO.getId());
                    param.setOrderState(OrderStatus.TRANSACTION_SUCCESS);
                    ordersService.updateByPrimaryKeySelective(param);
                    winningRecord.setState(LuckyDraw.WinningState.ALREADY_ISSUED);
                    winningRecordService.updateState(winningRecord);
                    logger.debug("抽奖中外部卡券发券成功!");
                } catch (Exception e){
                    logger.error("外部卡券发券失败!" + ciParam.toString());
                    OrdersDO param = new OrdersDO();
                    param.setId(ordersDO.getId());
                    param.setOrderState(OrderStatus.WAITING_FOR_DELIVERY);
                    ordersService.updateByPrimaryKeySelective(param);
                    winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                    winningRecordService.updateState(winningRecord);
                }
                break;
            case Category.FINANCIAL_CARD:
                ordersDO.setOrderType(OrderType.FINANCIAL_CARD_ORDER);
                if(winningRecord.getState().equals(LuckyDraw.WinningState.PAYMENT_FAILURE) || winningRecord.getState().equals(LuckyDraw.WinningState.PRIZE_ORDER_IS_NOT_GENERATED)){
                    RequestFlow param = new RequestFlow();
                    param.setRequestNum(winningRecord.getRequestNo());
                    RequestFlow requestFlow = requestFlowBiz.getEntity(param);
                    _deal(requestFlow, ordersDO);
                } else if(winningRecord.getState().equals(LuckyDraw.WinningState.UNISSUED)){
                    JSONObject requestParam = new JSONObject();
                    requestParam.put("userId", winningRecord.getUserId());
                    requestParam.put("eid", goods.getBatchNumber());
                    requestParam.put("requestNo", winningRecord.getRequestNo());
                    RequestFlow requestFlow = _inserRequestFlow(BusinessSide.SCORE, BusinessSide.TAIRAN_FINANCIAL, BusinessType.LUCKY_DRAW_GAIN_FINANCIAL_COUPONS, winningRecord.getRequestNo(), RequestFlow.Status.INITIAL, requestParam.toJSONString(), null,createTime, null);
                    _deal(requestFlow, ordersDO);
                }
                break;
            default:
                break;
        }
    }

    private void _deal(RequestFlow requestFlow, OrdersDO ordersDO){
        boolean result = gainFinancialCard.execute(requestFlow);
        if(result){
            OrdersDO oParam = new OrdersDO();
            oParam.setId(ordersDO.getId());
            oParam.setOrderState(OrderStatus.TRANSACTION_SUCCESS);
            ordersService.updateByPrimaryKeySelective(oParam);
        } else{
            OrdersDO oParam = new OrdersDO();
            oParam.setId(ordersDO.getId());
            oParam.setOrderState(OrderStatus.WAITING_FOR_DELIVERY);
            ordersService.updateByPrimaryKeySelective(oParam);
        }
    }

    private OrdersDO _createOrder(WinningRecordDO winningRecord, GoodsDO goods, CategoryDO category, Date createTime) {
        OrdersDO ordersDO = new OrdersDO();
        ordersDO.setSource(2);
        ordersDO.setPrice(goods.getPriceScore());
        ordersDO.setGoodsCount(winningRecord.getNumberOfPrizes());
        ordersDO.setPayment(winningRecord.getExpenditure());
        ordersDO.setGoodsId(goods.getId());
        ordersDO.setBarcode(goods.getBarcode());
        ordersDO.setGoodsNo(goods.getGoodsNo());
        ordersDO.setGoodsName(goods.getGoodsName());
        ordersDO.setGoodsVersion(goods.getVersionLock());
        ordersDO.setOrderNum(winningRecord.getRequestNo());
        ordersDO.setMinImg(goods.getMainImg());
        ordersDO.setShopId(goods.getShopId());
        ordersDO.setUserId(winningRecord.getUserId());
        ordersDO.setUsername(winningRecord.getLotteryPhone());
        ordersDO.setIsDeleted(false);
        ordersDO.setCreateTime(createTime);
        ordersDO.setUpdateTime(createTime);
        if (category.getIsVirtual() == 0) {
            ordersDO.setOrderType(OrderType.MATERIAL_ORDER);
        } else if(Category.FINANCIAL_CARD.equals(category.getCode())){
            ordersDO.setOrderType(OrderType.FINANCIAL_CARD_ORDER);
        } else if(Category.EXTERNAL_CARD.equals(category.getCode())){
            ordersDO.setOrderType(OrderType.EXTERNAL_CARD_ORDER);
        }
        //生成奖品订单后，商品相应操作
        goodsBiz.prizeOrderAssociationProcessing(ordersDO.getGoodsId(), ordersDO.getGoodsCount(), ordersDO.getGoodsVersion());
        ordersDO.setOrderState(OrderStatus.WAITING_FOR_DELIVERY);
        ordersMapper.insert(ordersDO);
        return ordersDO;
    }

    @Transactional
    private RequestFlow _inserRequestFlow(String requester, String responder, String type, String requestNum, String status, String requestParam, String responseResult, Date requestTime, String remark){
        RequestFlow requestFlow = new RequestFlow(requester, responder, type, requestNum, null==status?RequestFlow.Status.INITIAL:status, requestParam, responseResult,requestTime, remark);
        return requestFlowService.insert(requestFlow);
    }

}

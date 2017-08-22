package org.trc.biz.impl.order;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.biz.order.IAreaBiz;
import org.trc.biz.order.INewOrderBiz;
import org.trc.biz.score.IScoreBiz;
import org.trc.constants.OrderStatus;
import org.trc.constants.OrderType;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.goods.CardItemDO;
import org.trc.domain.luckydraw.WinningRecordDO;
import org.trc.domain.order.LogisticsCodeDO;
import org.trc.domain.order.LogisticsDO;
import org.trc.domain.order.OrderAddressDO;
import org.trc.domain.order.OrderLocusDO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.order.OrdersExtendDO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.settlement.SettlementDO;
import org.trc.domain.shop.ShopDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.exception.GoodsException;
import org.trc.exception.OrderException;
import org.trc.service.luckydraw.IWinningRecordService;
import org.trc.service.order.ILogisticsService;
import org.trc.service.order.IOrderAddressService;
import org.trc.service.order.IOrderLocusService;
import org.trc.service.order.IOrderService;
import org.trc.service.order.IOrdersExtendService;
import org.trc.service.shop.IShopService;
import org.trc.util.Pagenation;

import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.LogisticAck;
import com.trc.mall.externalservice.LogisticTrace;
import com.trc.mall.externalservice.TrcExpress100;
import com.trc.mall.externalservice.dto.TrcExpressDto;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
@Service("newOrderBiz")
public class NewOrderBiz implements INewOrderBiz {
    Logger logger = LoggerFactory.getLogger(NewOrderBiz.class);

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IShopService shopService;
    @Autowired
    private ILogisticsService logisticsService;
    @Resource
    private TrcExpress100 trcExpress100;
    @Autowired
    private ICouponsBiz couponsBiz;
    @Autowired
    private IOrderLocusService orderLocusService;
    @Resource
    private LogisticTrace logisticTrace;
    @Autowired
    private IOrderAddressService orderAddressService;
    @Autowired
    private IAreaBiz areaBiz;
    @Autowired
    private IWinningRecordService winningRecordService;
    @Autowired
    private IOrdersExtendService ordersExtendService;
    @Autowired
    private IScoreBiz scoreBiz;
    @Override
    public OrderAddressDO getOrderAddressByOrderId(Long orderId) {
        OrderAddressDO orderAddress = new OrderAddressDO();
        orderAddress.setOrderId(orderId);
        OrderAddressDO orderAddressDO = orderAddressService.selectOne(orderAddress);
        handleOrderAddressDO(orderAddressDO);
        return orderAddressDO;
    }

    @Override
    public OrderAddressDO getOrderAddressByOrderNum(String orderNum) {
        OrderAddressDO orderAddressDO = orderAddressService.getOrderAddressDOByOrderNum(orderNum);
        handleOrderAddressDO(orderAddressDO);
        return orderAddressDO;
    }
    private void handleOrderAddressDO(OrderAddressDO orderAddressDO){
        if(null!=orderAddressDO){
            orderAddressDO.setProvince(areaBiz.getAreaByCode(orderAddressDO.getProvinceCode()).getProvince());
            orderAddressDO.setCity(areaBiz.getAreaByCode(orderAddressDO.getCityCode()).getCity());
            if(StringUtils.isNotBlank(orderAddressDO.getAreaCode())){
                orderAddressDO.setArea(areaBiz.getAreaByCode(orderAddressDO.getAreaCode()).getDistrict());
            }
        }
    }

    @Override
    public LogisticAck logisticsTracking(String shipperCode, String logisticCode) {
        return logisticTrace.pull(shipperCode, logisticCode);
    }

    @Override
    public HttpBaseAck<TrcExpressDto> pull(String shipperCode, String logisticCode) throws IOException {
        return trcExpress100.pull(shipperCode, logisticCode);
    }

    /**
     * 多条件查询(分页)
     *
     * @param ordersDO
     * @param page
     * @return
     */
    @Override
    public Pagenation<OrdersDO> queryOrdersDOListForPage(OrderDTO ordersDO, Pagenation<OrdersDO> page) {
        try {
            Assert.notNull(page, "分页参数不能为空");
            Assert.notNull(ordersDO, "传入参数不能为空");
            page = orderService.selectListByParams(ordersDO, page);
            if (page != null && page.getInfos() != null) {
                for (OrdersDO item : page.getInfos()) {
                    ShopDO shop = shopService.selectById(item.getShopId());
                    item.setShopName(shop.getShopName());
                    OrderAddressDO orderAddressDO = this.getOrderAddressByOrderId(item.getId());
                    item.setOrderAddressDO(orderAddressDO);
                }
            }
            return page;
        } catch (IllegalArgumentException e) {
            logger.error("<== " + e.getMessage(), e);
            throw new OrderException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "方法:queryOrdersDOListForPage:参数检查异常!");
        } catch (Exception e) {
            logger.error("分页查询异常", e);
            throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "方法:queryOrdersDOListForPage:分页查询失败!");
        }
    }

    @Override
    public List<OrdersDO> queryOrdersDOList(OrderDTO ordersDO) {
        return null;
    }

    /**
     * 查询订单
     *
     * @param ordersDO
     * @return
     */
    @Override
    public OrdersDO selectByParams(OrdersDO ordersDO) {
        OrdersDO result = orderService.selectOne(ordersDO);
        if (null != result) {
            ShopDO shop = shopService.selectByPrimaryKey(result.getShopId());
            result.setShopName(shop.getShopName());
            OrderAddressDO orderAddressDO = this.getOrderAddressByOrderId(result.getId());
            result.setOrderAddressDO(orderAddressDO);
            LogisticsDO logistics = new LogisticsDO();
            logistics.setOrderId(result.getId());
            LogisticsDO logisticsDO = logisticsService.selectOne(logistics);
            if (null != logisticsDO && StringUtils.isNotBlank(logisticsDO.getShipperCode())) {
                LogisticsCodeDO logisticsCodeDO = logisticsService.getLogisticsCodeDOByCode(logisticsDO.getShipperCode());
                if (null != logisticsCodeDO) {
                    logisticsDO.setCompanyName(logisticsService.getLogisticsCodeDOByCode(logisticsDO.getShipperCode()).getCompanyName());
                }
            }
            if (OrderType.EXTERNAL_CARD_ORDER == result.getOrderType().intValue()) {
                List<CardItemDO> cardItemList = couponsBiz.selectItemByOrderNum(result.getOrderNum());
                StringBuilder couponCode = new StringBuilder("");
                for (CardItemDO ci : cardItemList) {
                    couponCode.append(ci.getCode()).append(" ");
                }
                result.setCouponCode(couponCode.toString());
            }
            result.setLogisticsDO(logisticsDO);
        } else {
            throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "查询的订单不存在!");
        }
        return result;
    }

    @Override
    public OrdersDO createOrder(OrderDTO orderDTO) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrdersDO modifyOrderState(OrdersDO ordersDO) {
        try {
            Assert.notNull(ordersDO, "ordersDO不能为空!");
            Assert.notNull(ordersDO.getId(), "id不能为空!");
            Assert.notNull(ordersDO.getOrderState(), "orderState不能为空!");
            OrdersDO param = new OrdersDO();
            param.setId(ordersDO.getId());
            OrdersDO oldOrder = orderService.selectByParams(param);
            if (null == oldOrder) {
                throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "对应的订单不存在!");
            }
            Date confirmTime = Calendar.getInstance().getTime();
            if (oldOrder.getOrderState().equals(OrderStatus.WAITING_FOR_RECEIVING) && ordersDO.getOrderState().equals(OrderStatus.TRANSACTION_SUCCESS)) {
                ordersDO.setConfirmTime(confirmTime);
                int result = orderService.updateById(ordersDO);
                if (result < 1) {
                    throw new OrderException(ExceptionEnum.ORDER_UPDATE_EXCEPTION, "订单状态修改失败！");
                } else {
                    OrderLocusDO locus = new OrderLocusDO(ordersDO.getId(), OrderStatus.WAITING_FOR_RECEIVING, OrderStatus.TRANSACTION_SUCCESS, confirmTime);
                    if (orderLocusService.insertSelective(locus) < 1) {
                        logger.error("订单状态跟踪记录失败!orderId=" + ordersDO.getId());
                        throw new OrderException(ExceptionEnum.ORDER_SAVE_EXCEPTION, "订单状态跟踪记录失败!orderId=" + ordersDO.getId());
                    }
                    return ordersDO;
                }
            } else {
                throw new OrderException(ExceptionEnum.ORDER_ERROR_ILLEGAL_OPERATION, "订单状态修改不合法！");
            }
        } catch (IllegalArgumentException e) {
            logger.error("修改订单状态校验参数异常!", e);
            throw new OrderException(ExceptionEnum.PARAM_CHECK_EXCEPTION, e.getMessage());
        } catch (OrderException e) {
            throw e;
        } catch (Exception e) {
            logger.error("修改订单状态异常!", e);
            throw new GoodsException(ExceptionEnum.GOODS_SAVE_EXCEPTION, "修改订单状态异常!");
        }

    }
    /**
     * 发货->发货后订单交易成功
     *
     * @param logistics
     * @return LogisticsDO
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public LogisticsDO shipOrder(LogisticsDO logistics) {
        try {
            // Validate
            validateLogistics(logistics);
            OrdersDO order = new OrdersDO();
            order.setId(logistics.getOrderId());
            order = orderService.selectOne(order);
            if (null == order) {
                logger.error("不存在该订单!orderId=" + logistics.getOrderId());
                throw new OrderException(ExceptionEnum.ORDER_ENTITY_NOT_EXIST, String.format("不存在该订单!orderId=%s",logistics.getOrderId()));
            }
            if (!order.getOrderState().equals(OrderStatus.WAITING_FOR_DELIVERY)) {
                logger.error("当前订单状态:" + order.getOrderState() + "请勿非法操作!");
                throw new OrderException(ExceptionEnum.ORDER_ERROR_ILLEGAL_OPERATION, "当前订单状态:" + order.getOrderState() + "请勿非法操作!");
            }
            Date deliveryTime = Calendar.getInstance().getTime();
            order.setFreight(logistics.getFreight());
            order.setDeliveryTime(deliveryTime);
            order.setOrderState(OrderStatus.WAITING_FOR_RECEIVING);
            order.setUpdateTime(Calendar.getInstance().getTime());
            int result = orderService.updateByPrimaryKey(order);
            if (result < 1) {
                throw new OrderException(ExceptionEnum.ORDER_UPDATE_EXCEPTION, "更新订单状态失败！");
            }
            logistics.setCreateTime(Calendar.getInstance().getTime());
            result = logisticsService.insertSelective(logistics);
            if (result < 1) {
                logger.error("logistics没有插入数据!");
                throw new OrderException(ExceptionEnum.ORDER_SAVE_EXCEPTION, "插入物流信息失败!");
            } else {
                OrderLocusDO locus = new OrderLocusDO(order.getId(), OrderStatus.WAITING_FOR_DELIVERY, OrderStatus.WAITING_FOR_RECEIVING, deliveryTime);
                if (orderLocusService.insertSelective(locus) < 1) {
                    logger.error("订单状态跟踪记录失败!orderId=" + order.getId());
                    throw new OrderException(ExceptionEnum.ORDER_SAVE_EXCEPTION, "订单状态跟踪记录失败!orderId=" + order.getId());
                }
                return logistics;
            }
        } catch (Exception e) {
            return null;
        }
    }
    private void validateLogistics(LogisticsDO logistics) {
        Assert.notNull(logistics, "logistics不能为空!");
        Assert.isTrue(logistics.getOrderId() != null, "orderId不能为空");
        //Assert.isTrue(StringUtils.isNotBlank(logistics.getCompanyName()), "companyName不能为空");
        Assert.isTrue(StringUtils.isNotBlank(logistics.getLogisticsNum()), "logisticsNum不能为空");
        Assert.isTrue(logistics.getOperatorUserId() != null, "operatorUserId不能为空");
    }
    /**
     * 奖品发放
     * @param orderNum
     * @param remark
     * @param logistics
     * @return LogisticsDO
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public LogisticsDO shipPrizes(String orderNum, String remark, LogisticsDO logistics) {
        WinningRecordDO param = new WinningRecordDO();
        param.setOrderNum(orderNum);
        WinningRecordDO oldEntity = winningRecordService.selectOneForUpdate(param);
        param.setState(1);
        param.setId(oldEntity.getId());
        param.setVersion(oldEntity.getVersion());
        int result = winningRecordService.updateState(param);
        if(0 == result){
            throw new BusinessException(ExceptionEnum.UPDATE_EXCEPTION, "奖品发放失败!");
        }
        if(StringUtils.isNotBlank(remark)) {
            Date time = Calendar.getInstance().getTime();
            OrdersDO orders = new OrdersDO();
            orders.setOrderNum(orderNum);
            OrdersDO originalOrder = orderService.selectOne(orders);
            OrdersExtendDO ordersExtend = new OrdersExtendDO();
            ordersExtend.setOrderId(originalOrder.getId());
            ordersExtend.setOrderNum(originalOrder.getOrderNum());
            ordersExtend.setRemark(remark);
            ordersExtend.setReturnTime(time);
            ordersExtend.setCreateTime(time);
            ordersExtend.setUpdateTime(time);
            ordersExtendService.insertSelective(ordersExtend);
        }
        return shipOrder(logistics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LogisticsDO modifyOrderLogistic(LogisticsDO logistics) {
        try {
            Assert.notNull(logistics, "logistics不能为空!");
            Assert.isTrue(logistics.getOrderId() != null, "orderId不能为空");
            boolean param = (StringUtils.isNotBlank(logistics.getLogisticsNum()) || StringUtils.isNotBlank(logistics.getCompanyName()));
            Assert.isTrue(param, "缺少logisticsNum或companyName参数!");
            OrdersDO order = new OrdersDO();
            order.setId(logistics.getOrderId());
            order = orderService.selectOne(order);
            if (null == order) {
                logger.error("不存在该订单!orderId=" + logistics.getOrderId());
                throw new OrderException(ExceptionEnum.ORDER_ENTITY_NOT_EXIST, "不存在该订单!orderId=" + logistics.getOrderId());
            }
            if (!order.getOrderState().equals(OrderStatus.WAITING_FOR_RECEIVING)) {
                logger.error("当前订单状态:" + order.getOrderState() + "请勿非法操作!");
                throw new OrderException(ExceptionEnum.ORDER_ERROR_ILLEGAL_OPERATION, "当前订单状态:" + order.getOrderState() + "请勿非法操作!");
            }
            if (order.getFreight().intValue() != logistics.getFreight().intValue()) {
                order.setFreight(logistics.getFreight());
                order.setUpdateTime(Calendar.getInstance().getTime());
                int result = orderService.updateByPrimaryKey(order);
                if (result < 1) {
                    throw new OrderException(ExceptionEnum.ORDER_UPDATE_EXCEPTION, "更新订单物流费用失败！");
                }
            }
            // Update
            int result = logisticsService.updateById(logistics);
            if (result < 1) {
                logger.error("修改数据行数为0!orderId=" + logistics.getOrderId());
                throw new OrderException(ExceptionEnum.ORDER_UPDATE_EXCEPTION, "更新订单物流信息失败！");
            } else {
                return logistics;
            }
        } catch (IllegalArgumentException e) {
            logger.error("更新订单物流信息校验参数异常!", e);
            throw new OrderException(ExceptionEnum.PARAM_CHECK_EXCEPTION, e.getMessage());
        } catch (OrderException e) {
            throw e;
        } catch (Exception e) {
            logger.error("更新订单物流信息异常!", e);
            throw new OrderException(ExceptionEnum.ORDER_UPDATE_EXCEPTION, "更新订单物流信息异常!");
        }
    }

    @Override
    public LogisticsDO getOrderLogistic(LogisticsDO logistics) {
        return logisticsService.selectOne(logistics);
    }

    @Override
    public Pagenation<OrdersDO> queryOrdersByParams(SettlementQuery settlementQuery, Pagenation<OrdersDO> pageRequest) {
        Assert.notNull(pageRequest, "分页参数不能为空");
        Assert.notNull(settlementQuery, "传入参数不能为空");
        return orderService.selectOrdersByParams(settlementQuery,pageRequest);
    }

    @Override
    public Pagenation<OrderDTO> queryRefundOrdersByParams(SettlementQuery settlementQuery, Pagenation<OrderDTO> pageRequest) {
        Assert.notNull(pageRequest, "分页参数不能为空");
        Assert.notNull(settlementQuery, "传入参数不能为空");
        return orderService.selectRefundOrdersByParams(settlementQuery,pageRequest);
    }

    @Override
    public List<OrderDTO> queryRefundOrdersByParamsForExport(SettlementQuery settlementQuery) {
        Assert.notNull(settlementQuery, "传入参数不能为空");
        return orderService.queryRefundOrdersByParamsForExport(settlementQuery);
    }

    @Override
    public SettlementDO getSettlementByParams(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public List<ExportOrderDTO> queryOrdersForExport(SettlementQuery settlementQuery) {
        return orderService.queryOrdersForExport(settlementQuery);
    }

    @Override
    public List<ExportOrderDTO> queryOrderAndAddressForExport(SettlementQuery settlementQuery) {
        List<ExportOrderDTO> result = orderService.queryOrderAndAddressForExport(settlementQuery);
        if(result != null){
            for(ExportOrderDTO exportOrderDTO : result){
                OrderAddressDO orderAddress = new OrderAddressDO();
                orderAddress.setOrderId(exportOrderDTO.getOrderId());
                OrderAddressDO orderAddressDO = orderAddressService.selectOne(orderAddress);
                if(null != orderAddressDO) {
                    exportOrderDTO.setReceiverName(orderAddressDO.getReceiverName());
                    exportOrderDTO.setPhone(orderAddressDO.getPhone());
                    exportOrderDTO.setProvince(areaBiz.getAreaByCode(orderAddressDO.getProvinceCode()).getProvince());
                    exportOrderDTO.setCity(areaBiz.getAreaByCode(orderAddressDO.getCityCode()).getCity());
                    if(StringUtils.isNotBlank(orderAddressDO.getAreaCode())) {
                        exportOrderDTO.setArea(areaBiz.getAreaByCode(orderAddressDO.getAreaCode()).getDistrict());
                    }
                    exportOrderDTO.setAddress(orderAddressDO.getAddress());
                }
                if(exportOrderDTO.getOrderState()==2 || exportOrderDTO.getOrderState()==3){
                    LogisticsDO logistics = new LogisticsDO();
                    logistics.setOrderId(exportOrderDTO.getOrderId());
                    LogisticsDO logisticsDO = logisticsService.selectByParams(logistics);
                    if(exportOrderDTO.getOrderId()==48){
                    	System.out.println("---------");
                    }
                    if(null != logisticsDO){
                        exportOrderDTO.setLogisticsNum(logisticsDO.getLogisticsNum());
                        exportOrderDTO.setCompanyName(logisticsService.getLogisticsCodeDOByCode(logisticsDO.getShipperCode()).getCompanyName());
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int confirmOrder(Integer confirmState, Integer originalState, Date time) {
        return 0;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)//TODO
    public void returnGoods(String orderNum, String userId, String remark) {
        //查找对应的订单
        OrdersDO orders = new OrdersDO();
        orders.setOrderNum(orderNum);
        orders.setUserId(userId);
        OrdersDO originalOrder = orderService.selectOne(orders);
        if (null == originalOrder) {
            throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "订单:" + orderNum + "不存在!");
        } else if (OrderStatus.RETURN_GOODS == originalOrder.getOrderState().intValue()) {
            throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "订单:" + orderNum + "已退回!");
        }/*else if(OrderStatus.TRANSACTION_SUCCESS == originalOrder.getOrderState().intValue() || OrderStatus.SYSTEM_CONFIRM_SUCCESS == originalOrder.getOrderState().intValue()){
            throw new OrderException(OrderException.OPERATION_NOT_VALID, "订单:"+orderNum+"已完结，不允许退回!");
        }*/ else {
            //1、退积分
            Date time = Calendar.getInstance().getTime();
            scoreBiz.returnScore(orderNum, time);
            //2、更新订单状态，
            OrdersDO order = new OrdersDO();
            order.setId(originalOrder.getId());
            order.setOrderState(OrderStatus.RETURN_GOODS);
            orderService.updateByPrimaryKeySelective(order);
            //3、更新退货时间，备注信息
            OrdersExtendDO ordersExtend = new OrdersExtendDO();
            ordersExtend.setOrderId(originalOrder.getId());
            ordersExtend.setOrderNum(originalOrder.getOrderNum());
            ordersExtend.setRemark(remark);
            ordersExtend.setReturnTime(time);
            ordersExtend.setCreateTime(time);
            ordersExtend.setUpdateTime(time);
            ordersExtendService.insertSelective(ordersExtend);
            //4、TODO 库存更新 待明确
        }
    }

    @Override
    public int quantityToBeDelivered(String userId, Long shopId) {
        return 0;
    }

    @Override
    public int quantityToBeReceived(String userId, Long shopId) {
        return 0;
    }

    @Override
    public OrdersExtendDO getOrdersExtendByOrderNum(OrdersExtendDO ordersExtendDO) {
        return ordersExtendService.selectOne(ordersExtendDO);
    }
}

package org.trc.biz.impl.order;

import com.sun.javafx.binding.StringFormatter;
import com.trc.mall.externalservice.LogisticAck;
import com.trc.mall.externalservice.LogisticTrace;
import com.trc.mall.externalservice.TrcExpress100;
import com.trc.mall.externalservice.TrcExpressAck;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.biz.order.INewOrderBiz;
import org.trc.constants.OrderStatus;
import org.trc.constants.OrderType;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.goods.CardItemDO;
import org.trc.domain.order.*;
import org.trc.domain.shop.ShopDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.GoodsException;
import org.trc.exception.OrderException;
import org.trc.service.order.ILogisticsService;
import org.trc.service.order.IOrderLocusService;
import org.trc.service.order.IOrderService;
import org.trc.service.shop.IShopService;
import org.trc.util.Pagenation;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
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
    @Resource//TODO 注入
    private LogisticTrace logisticTrace;
    @Override
    public OrderAddressDO getOrderAddressByOrderId(Long orderId) {
        return null;
    }

    @Override
    public OrderAddressDO getOrderAddressByOrderNum(String orderNum) {
        return null;
    }

    @Override
    public LogisticAck logisticsTracking(String shipperCode, String logisticCode) {
        return logisticTrace.pull(shipperCode, logisticCode);
    }

    @Override
    public TrcExpressAck pull(String shipperCode, String logisticCode) {
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
    public Pagenation<OrdersDO> queryOrdersDOListForPage(OrdersDO ordersDO, Pagenation<OrdersDO> page) {
        try {
            Assert.notNull(page, "分页参数不能为空");
            Assert.notNull(ordersDO, "传入参数不能为空");
            page = orderService.selectListByParams(ordersDO, page);
            if (page != null && page.getResult() != null) {
                for (OrdersDO item : page.getResult()) {
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
    @Override
    public LogisticsDO shipPrizes(String orderNum, String remark, LogisticsDO logistics) {
        return null;
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
        return null;
    }

    @Override
    public SettlementDO getSettlementByParams(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public List<ExportOrderDTO> queryOrdersForExport(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public List<ExportOrderDTO> queryOrderAndAddressForExport(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public int confirmOrder(Integer confirmState, Integer originalState, Date time) {
        return 0;
    }

    @Override
    public void returnGoods(String orderNum, String userId, String remark) {

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
        return null;
    }
}

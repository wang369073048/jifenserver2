package org.trc.biz.order;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.order.LogisticsDO;
import org.trc.domain.order.OrderAddressDO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.order.OrdersExtendDO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.settlement.SettlementDO;
import org.trc.util.Pagenation;

import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.LogisticAck;
import com.trc.mall.externalservice.dto.TrcExpressDto;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
public interface INewOrderBiz {

    OrderAddressDO getOrderAddressByOrderId(Long orderId);

    OrderAddressDO getOrderAddressByOrderNum(String orderNum);

    LogisticAck logisticsTracking(String shipperCode, String logisticCode );

    HttpBaseAck<TrcExpressDto> pull(String shipperCode, String logisticCode)throws IOException;;

    /**
     * 多条件查询(分页)
     *
     * @param ordersDO
     * @param page
     * @return PageRequest<OrdersDO>
     */
    Pagenation<OrdersDO> queryOrdersDOListForPage(OrderDTO ordersDO, Pagenation<OrdersDO> page);

    /**
     * 多条件查询(不分页)
     *
     * @param ordersDO
     */
    List<OrdersDO> queryOrdersDOList(OrderDTO ordersDO);

    /**
     * 查询订单
     *
     * @param ordersDO
     * @return OrdersDO
     */
    OrdersDO selectByParams(OrdersDO ordersDO);

    /**
     * 创建订单
     *
     * @param orderDTO
     * @return OrdersDO
     */
    OrdersDO createOrder(OrderDTO orderDTO);

    /**
     * 修改订单状态
     *
     * @param ordersDO
     * @return
     */
    OrdersDO modifyOrderState(OrdersDO ordersDO);

    /**
     * 发货->发货后订单交易成功
     *
     * @param logistics
     * @return LogisticsDO
     */
    LogisticsDO shipOrder(LogisticsDO logistics);

    /**
     * 奖品发放
     *
     * @param orderNum
     * @param remark
     * @param logistics
     * @return LogisticsDO
     */
    LogisticsDO shipPrizes(String orderNum, String remark, LogisticsDO logistics);

    /**
     * 修改订单物流
     *
     * @param logistics
     * @return LogisticsDO
     */
    LogisticsDO modifyOrderLogistic(LogisticsDO logistics);

    /**
     * 获取订单物流
     *
     * @param logistics
     * @return ResultModel<LogisticsDO>
     */
    LogisticsDO getOrderLogistic(LogisticsDO logistics);

    /**
     * 多条件查询表信息(分页)
     * @param settlementQuery SettlementQuery
     * @param pageRequest PageRequest<OrdersDO>
     * @return PageRequest<OrdersDO>
     */
    Pagenation<OrdersDO> queryOrdersByParams(SettlementQuery settlementQuery, Pagenation<OrdersDO> pageRequest);

    /**
     * 多条件查询退款结算信息(分页)
     * @param settlementQuery SettlementQuery
     * @param pageRequest PageRequest<OrdersDO>
     * @return PageRequest<OrdersDO>
     */
    Pagenation<OrderDTO> queryRefundOrdersByParams(SettlementQuery settlementQuery, Pagenation<OrderDTO> pageRequest);

    /**
     * 多条件查询退款结算信息(不分页)
     * @param settlementQuery
     * @return
     */
    List<OrderDTO> queryRefundOrdersByParamsForExport(SettlementQuery settlementQuery);

    /**
     * 查询待结算信息
     * @param settlementQuery
     * @return PageRequest<SettlementDTO>
     */
    SettlementDO getSettlementByParams(SettlementQuery settlementQuery);

    List<ExportOrderDTO> queryOrdersForExport(SettlementQuery settlementQuery);

    List<ExportOrderDTO> queryOrderAndAddressForExport(SettlementQuery settlementQuery);

    int confirmOrder(Integer confirmState, Integer originalState, Date time);

    void returnGoods(String orderNum, String userId, String remark);

    int quantityToBeDelivered(String userId, Long shopId);

    int quantityToBeReceived(String userId, Long shopId);

    OrdersExtendDO getOrdersExtendByOrderNum(OrdersExtendDO ordersExtendDO);
}

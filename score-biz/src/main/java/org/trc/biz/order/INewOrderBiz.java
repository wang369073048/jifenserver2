package org.trc.biz.order;

import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.LogisticAck;
import com.trc.mall.externalservice.dto.TrcExpressDto;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.order.*;
import org.trc.util.Pagenation;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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

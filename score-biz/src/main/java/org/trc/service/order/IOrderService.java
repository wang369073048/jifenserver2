package org.trc.service.order;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.order.SettlementDO;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
public interface IOrderService extends IBaseService<OrdersDO,Long>{

    /**
     * 根据ID查询表数据
     * @return OrdersDO
     */
    OrdersDO selectByParams(OrdersDO ordersDO);

    /**
     * 多条件查询表信息(分页)
     * @param ordersDO OrdersDO
     * @param pageRequest PageRequest<OrdersDO>
     * @return List<OrdersDO>
     */
    Pagenation<OrdersDO> selectListByParams(OrdersDO ordersDO, Pagenation<OrdersDO> pageRequest);

    /**
     * 多条件查询表信息(分页)
     * @param settlementQuery SettlementQuery
     * @param pageRequest PageRequest<OrdersDO>
     * @return List<OrdersDO>
     */
    Pagenation<OrdersDO> selectOrdersByParams(SettlementQuery settlementQuery, Pagenation<OrdersDO> pageRequest);

    int selectCountByParams(OrdersDO ordersDO);

    /**
     * 插入信息
     * @param ordersDO OrdersDO
     * @return int
     */
    int insert(OrdersDO ordersDO);

    /**
     * 根据ID更新信息
     * @param ordersDO OrdersDO
     * @return int
     */
    int updateById(OrdersDO ordersDO);

    /**
     * 根据ID删除信息
     * @param id Long
     * @return int
     */
    int deleteById(Long id);

    /**
     * 查询待结算信息
     * @param settlementQuery
     * @return
     */
    SettlementDO getSettlementByParams(SettlementQuery settlementQuery);

    List<ExportOrderDTO> queryOrdersForExport(SettlementQuery settlementQuery);

    List<ExportOrderDTO> queryOrderAndAddressForExport(SettlementQuery settlementQuery);

    int confirmOrder(Map<String,Object> params);

    int quantityToBeDelivered(OrdersDO order);

    int quantityToBeReceived(OrdersDO order);
}

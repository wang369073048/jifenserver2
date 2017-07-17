package org.trc.mapper.order;

import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.OrdersDO;
import org.trc.util.BaseMapper;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/4
 */
public interface IOrderMapper extends BaseMapper<OrdersDO>{

    /**
     * 多条件查询表信息(分页)
     * @param ordersDO OrdersDO
     * @param pageRequest PageRequest<OrdersDO>
     * @return List<OrdersDO>
     */
    List<OrdersDO> selectListByParams(OrdersDO ordersDO, Pagenation<OrdersDO> pageRequest);

    /**
     * 多条件查询表信息(分页)
     * @param settlementQuery SettlementQuery
     * @param pageRequest PageRequest<OrdersDO>
     * @return List<OrdersDO>
     */
    List<OrdersDO> selectOrdersByParams(SettlementQuery settlementQuery, Pagenation<OrdersDO> pageRequest);
}

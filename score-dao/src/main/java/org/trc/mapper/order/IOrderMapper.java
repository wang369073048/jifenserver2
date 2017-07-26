package org.trc.mapper.order;

import org.apache.ibatis.annotations.Param;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.OrdersDO;
import org.trc.util.BaseMapper;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/4
 */
public interface IOrderMapper extends BaseMapper<OrdersDO>{

    /**
     * 多条件查询表信息(分页)
     * @return List<OrdersDO>
     */
    List<OrdersDO> selectListByParams(OrderDTO orderDTO);

    /**
     * 多条件查询表信息(分页)
     * @param settlementQuery SettlementQuery
     * @param pageRequest PageRequest<OrdersDO>
     * @return List<OrdersDO>
     */
    List<OrdersDO> selectOrdersByParams(SettlementQuery settlementQuery);

    List<ExportOrderDTO> queryOrderAndAddressForExport(SettlementQuery settlementQuery);
}

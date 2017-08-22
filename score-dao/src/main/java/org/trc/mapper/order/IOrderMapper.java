package org.trc.mapper.order;

import java.util.List;

import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.settlement.SettlementDO;
import org.trc.util.BaseMapper;
import org.trc.util.Pagenation;

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
     * @return List<OrdersDO>
     */
    List<OrdersDO> selectOrdersByParams(SettlementQuery settlementQuery);
    
    /**
	 * 查询待结算信息
	 * @param settlementQuery
	 * @return
	 */
	SettlementDO getSettlementByParams(SettlementQuery settlementQuery);

    List<ExportOrderDTO> queryOrderAndAddressForExport(SettlementQuery settlementQuery);

    List<ExportOrderDTO> queryOrdersForExport(SettlementQuery settlementQuery);

    List<OrderDTO> selectRefundOrdersByParams(SettlementQuery settlementQuery);
}

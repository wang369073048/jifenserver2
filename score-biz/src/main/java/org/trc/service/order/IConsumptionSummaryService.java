package org.trc.service.order;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.dto.ConsumptionSummaryStatisticalDataDTO;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.ConsumptionSummaryDO;
import org.trc.util.Pagenation;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/26
 */
public interface IConsumptionSummaryService extends IBaseService<ConsumptionSummaryDO,Long>{

    ConsumptionSummaryDO getLastConsumptionSummary();

    int insertConsumptionSummary(ConsumptionSummaryDO consumptionSummaryDO);

    int updateConsumptionSummary(ConsumptionSummaryDO consumptionSummaryDO);

    ConsumptionSummaryDO getConsumptionSummaryByParams(ConsumptionSummaryDO consumptionSummaryDO);

    List<ConsumptionSummaryDO> generateConsumptionSummaryForExchangeIn(Map<String ,Date> timeInterval);

    List<ConsumptionSummaryDO> generateConsumptionSummaryForConsume(Map<String ,Date> timeInterval);

    Pagenation<ConsumptionSummaryDO> selectListByParams(SettlementQuery SettlementQuery, Pagenation<ConsumptionSummaryDO> pageRequest );

    ConsumptionSummaryStatisticalDataDTO generateConsumptionSummarySDForExchangeIn(Map timeInterval);

    ConsumptionSummaryStatisticalDataDTO generateConsumptionSummarySDForConsume(Map timeInterval);

    List<ConsumptionSummaryDO> queryConsumptionSummaryForExport(SettlementQuery settlementQuery);

    Pagenation<ConsumptionSummaryDO> queryMonthConsumptionSummary(SettlementQuery settlementQuery, Pagenation<ConsumptionSummaryDO> pageRequest);

    List<ConsumptionSummaryDO> queryMonthConsumptionSummaryForExport(SettlementQuery settlementQuery);

    SettlementIntervalDTO getSettlementInterval(SettlementQuery settlementQuery);
}

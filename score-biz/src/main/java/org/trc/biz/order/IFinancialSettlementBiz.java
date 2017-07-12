package org.trc.biz.order;

import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.ConsumptionSummaryDO;
import org.trc.domain.dto.ConsumptionSummaryStatisticalDataDTO;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/7
 */
public interface IFinancialSettlementBiz {

    Pagenation<ConsumptionSummaryDO> queryConsumptionSummary(SettlementQuery settlementQuery, Pagenation<ConsumptionSummaryDO> pageRequest);

    ConsumptionSummaryStatisticalDataDTO statisticsConsumptionSummary(SettlementQuery settlementQuery);

    Pagenation<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetails(SettlementQuery settlementQuery, Pagenation<MembershipScoreDailyDetailsDO> pageRequest);

    List<ConsumptionSummaryDO> queryConsumptionSummaryForExport(SettlementQuery settlementQuery);

    List<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetailForExport(SettlementQuery settlementQuery);

    Pagenation<ConsumptionSummaryDO> queryMonthConsumptionSummary(SettlementQuery settlementQuery, Pagenation<ConsumptionSummaryDO> pageRequest);

    List<ConsumptionSummaryDO> queryMonthConsumptionSummaryForExport(SettlementQuery settlementQuery);

    SettlementIntervalDTO getSettlementInterval(SettlementQuery settlementQuery);

    SettlementIntervalDTO getSettlementIntervalForMembershipScoreDailyDetail(SettlementQuery settlementQuery);
}

package org.trc.biz.impl.order;

import org.springframework.stereotype.Service;
import org.trc.biz.order.IFinancialSettlementBiz;
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
 * since Date： 2017/7/10
 */
@Service("financialSettlementBiz")
public class FinancialSettlementBiz implements IFinancialSettlementBiz{
    @Override
    public Pagenation<ConsumptionSummaryDO> queryConsumptionSummary(SettlementQuery settlementQuery, Pagenation<ConsumptionSummaryDO> pageRequest) {
        return null;
    }

    @Override
    public ConsumptionSummaryStatisticalDataDTO statisticsConsumptionSummary(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public Pagenation<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetails(SettlementQuery settlementQuery, Pagenation<MembershipScoreDailyDetailsDO> pageRequest) {
        return null;
    }

    @Override
    public List<ConsumptionSummaryDO> queryConsumptionSummaryForExport(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public List<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetailForExport(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public Pagenation<ConsumptionSummaryDO> queryMonthConsumptionSummary(SettlementQuery settlementQuery, Pagenation<ConsumptionSummaryDO> pageRequest) {
        return null;
    }

    @Override
    public List<ConsumptionSummaryDO> queryMonthConsumptionSummaryForExport(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public SettlementIntervalDTO getSettlementInterval(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public SettlementIntervalDTO getSettlementIntervalForMembershipScoreDailyDetail(SettlementQuery settlementQuery) {
        return null;
    }
}

package org.trc.mapper.settlement;

import org.trc.domain.dto.ConsumptionSummaryStatisticalDataDTO;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.order.ConsumptionSummaryDO;
import org.trc.util.BaseMapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/26
 */
public interface IConsumptionSummaryMapper extends BaseMapper<ConsumptionSummaryDO>{

    ConsumptionSummaryDO getLastConsumptionSummary();

    int insertConsumptionSummary(ConsumptionSummaryDO consumptionSummaryDO);

    int updateConsumptionSummary(ConsumptionSummaryDO consumptionSummaryDO);

    ConsumptionSummaryDO getConsumptionSummaryByParams(ConsumptionSummaryDO consumptionSummaryDO);

    List<ConsumptionSummaryDO> generateConsumptionSummaryForExchangeIn(Map<String ,Date> timeInterval);

    List<ConsumptionSummaryDO> generateConsumptionSummaryForConsume(Map<String ,Date> timeInterval);
    /**
     * 
     * @Description add by xab 获取抽奖消费汇总
     * @param timeInterval
     * @return
     */
    List<ConsumptionSummaryDO> generateConsumptionSummaryForLotteryConsume(Map<String ,Date> timeInterval);
    /**
     * 
     * @Description add by xab 获取消费冲正[也就是退积分]汇总
     * @param timeInterval
     * @return
     */
    List<ConsumptionSummaryDO> generateConsumptionSummaryForConsumeCorrect(Map<String ,Date> timeInterval);

    List<ConsumptionSummaryDO> selectListByParams(SettlementQuery SettlementQuery);

    ConsumptionSummaryStatisticalDataDTO generateConsumptionSummarySDForExchangeIn(Map timeInterval);

    ConsumptionSummaryStatisticalDataDTO generateConsumptionSummarySDForConsume(Map timeInterval);

    List<ConsumptionSummaryDO> queryConsumptionSummaryForExport(SettlementQuery settlementQuery);

    List<ConsumptionSummaryDO> queryMonthConsumptionSummary(SettlementQuery settlementQuery);

    List<ConsumptionSummaryDO> queryMonthConsumptionSummaryForExport(SettlementQuery settlementQuery);

    SettlementIntervalDTO getSettlementInterval(SettlementQuery settlementQuery);
}

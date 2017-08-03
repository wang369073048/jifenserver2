package org.trc.service.impl.order;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.dto.ConsumptionSummaryStatisticalDataDTO;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.order.ConsumptionSummaryDO;
import org.trc.mapper.order.IConsumptionSummaryMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.order.IConsumptionSummaryService;
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
@Service("consumptionSummaryService")
public class ConsumptionSummaryService extends BaseService<ConsumptionSummaryDO,Long> implements IConsumptionSummaryService{

    @Autowired
    private IConsumptionSummaryMapper consumptionSummaryMapper;
    @Override
    public ConsumptionSummaryDO getLastConsumptionSummary() {
        return null;
    }

    @Override
    public int insertConsumptionSummary(ConsumptionSummaryDO consumptionSummaryDO) {
        return 0;
    }

    @Override
    public int updateConsumptionSummary(ConsumptionSummaryDO consumptionSummaryDO) {
        return 0;
    }

    @Override
    public ConsumptionSummaryDO getConsumptionSummaryByParams(ConsumptionSummaryDO consumptionSummaryDO) {
        return null;
    }

    @Override
    public List<ConsumptionSummaryDO> generateConsumptionSummaryForExchangeIn(Map<String, Date> timeInterval) {
        return null;
    }

    @Override
    public List<ConsumptionSummaryDO> generateConsumptionSummaryForConsume(Map<String, Date> timeInterval) {
        return null;
    }

    @Override
    public Pagenation<ConsumptionSummaryDO> selectListByParams(SettlementQuery SettlementQuery, Pagenation<ConsumptionSummaryDO> pageRequest) {
        return null;
    }

    @Override
    public ConsumptionSummaryStatisticalDataDTO generateConsumptionSummarySDForExchangeIn(Map timeInterval) {
        return consumptionSummaryMapper.generateConsumptionSummarySDForExchangeIn(timeInterval);
    }

    @Override
    public ConsumptionSummaryStatisticalDataDTO generateConsumptionSummarySDForConsume(Map timeInterval) {
        return consumptionSummaryMapper.generateConsumptionSummarySDForConsume(timeInterval);
    }

    @Override
    public List<ConsumptionSummaryDO> queryConsumptionSummaryForExport(SettlementQuery settlementQuery) {
        return consumptionSummaryMapper.queryConsumptionSummaryForExport(settlementQuery);
    }

    @Override
    public Pagenation<ConsumptionSummaryDO> queryMonthConsumptionSummary(SettlementQuery settlementQuery, Pagenation<ConsumptionSummaryDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<ConsumptionSummaryDO> list = consumptionSummaryMapper.queryMonthConsumptionSummary(settlementQuery);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }

    @Override
    public List<ConsumptionSummaryDO> queryMonthConsumptionSummaryForExport(SettlementQuery settlementQuery) {
        return consumptionSummaryMapper.queryMonthConsumptionSummaryForExport(settlementQuery);
    }

    @Override
    public SettlementIntervalDTO getSettlementInterval(SettlementQuery settlementQuery) {
        return null;
    }
}

package org.trc.biz.impl.settlement;

import com.txframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.biz.settlement.IFinancialSettlementBiz;
import org.trc.constants.TemporaryContext;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.order.ConsumptionSummaryDO;
import org.trc.domain.dto.ConsumptionSummaryStatisticalDataDTO;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.service.settlement.IConsumptionSummaryService;
import org.trc.service.settlement.IMembershipScoreDailyDetailsService;
import org.trc.util.Pagenation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/10
 */
@Service("financialSettlementBiz")
public class FinancialSettlementBiz implements IFinancialSettlementBiz{
    Logger logger = LoggerFactory.getLogger(FinancialSettlementBiz.class);
    @Autowired
    private IConsumptionSummaryService consumptionSummaryService;
    @Autowired
    private IMembershipScoreDailyDetailsService membershipScoreDailyDetailsService;
    @Override
    public Pagenation<ConsumptionSummaryDO> queryConsumptionSummary(SettlementQuery settlementQuery, Pagenation<ConsumptionSummaryDO> pageRequest) {
        return null;
    }

    @Override
    public ConsumptionSummaryStatisticalDataDTO statisticsConsumptionSummary(SettlementQuery settlementQuery) {
        Map params = new HashMap();
        params.put("phone",settlementQuery.getPhone());
        params.put("shopId",settlementQuery.getShopId());
        if(null != settlementQuery.getShopId()) {
            params.put("exchangeCurrency", TemporaryContext.getExchangeCurrencyById(settlementQuery.getShopId()));
        }
        params.put("userId",settlementQuery.getUserId());
        params.put("startTime",settlementQuery.getStartTime());
        params.put("endTime",settlementQuery.getEndTime());
        ConsumptionSummaryStatisticalDataDTO result1 = consumptionSummaryService.generateConsumptionSummarySDForExchangeIn(params);
        ConsumptionSummaryStatisticalDataDTO result2 = consumptionSummaryService.generateConsumptionSummarySDForConsume(params);
        if(null != result1 && null != result2){
            result1.setTotalConsumptionCount(result2.getTotalConsumptionCount());
            result1.setConsumptionNum(result2.getConsumptionNum());
            return result1;
        }else if(null != result1 && null == result2){
            return result1;
        }else if(null == result1 && null != result2){
            return result2;
        }
        return null;
    }

    @Override
    public Pagenation<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetails(SettlementQuery settlementQuery, Pagenation<MembershipScoreDailyDetailsDO> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(settlementQuery, "传入参数不能为空");
            return membershipScoreDailyDetailsService.selectListByParams(settlementQuery, pageRequest);
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询MembershipDcoreDailyDetailsDO校验参数异常!",e);
            throw new BusinessException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (Exception e) {
            logger.error("多条件查询MembershipDcoreDailyDetailsDO信息异常!", e);
            throw new BusinessException(ExceptionEnum.QUERY_LIST_EXCEPTION,"多条件查询MembershipDcoreDailyDetailsDO信息异常!");
        }
    }

    @Override
    public List<ConsumptionSummaryDO> queryConsumptionSummaryForExport(SettlementQuery settlementQuery) {
        return consumptionSummaryService.queryConsumptionSummaryForExport(settlementQuery);
    }

    @Override
    public List<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetailForExport(SettlementQuery settlementQuery) {
        return membershipScoreDailyDetailsService.queryMembershipScoreDailyDetailForExport(settlementQuery);
    }

    @Override
    public Pagenation<ConsumptionSummaryDO> queryMonthConsumptionSummary(SettlementQuery settlementQuery, Pagenation<ConsumptionSummaryDO> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(settlementQuery, "传入参数不能为空");
            return consumptionSummaryService.queryMonthConsumptionSummary(settlementQuery, pageRequest);
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询ConsumptionSummaryDO校验参数异常!",e);
            throw new BusinessException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (Exception e) {
            logger.error("多条件查询ConsumptionSummaryDO信息异常!", e);
            throw new BusinessException(ExceptionEnum.QUERY_LIST_EXCEPTION,"多条件查询ConsumptionSummaryDO信息异常!");
        }
    }

    @Override
    public List<ConsumptionSummaryDO> queryMonthConsumptionSummaryForExport(SettlementQuery settlementQuery) {
        return consumptionSummaryService.queryMonthConsumptionSummaryForExport(settlementQuery);
    }

    @Override
    public SettlementIntervalDTO getSettlementInterval(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public SettlementIntervalDTO getSettlementIntervalForMembershipScoreDailyDetail(SettlementQuery settlementQuery) {
    	return membershipScoreDailyDetailsService.getSettlementIntervalForMembershipScoreDailyDetail(settlementQuery);
    }
}

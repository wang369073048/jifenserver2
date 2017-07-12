package org.trc.resource.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.txframework.util.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.trc.biz.order.IFinancialSettlementBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.ConsumptionSummaryDO;
import org.trc.domain.dto.ConsumptionSummaryStatisticalDataDTO;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.util.AppResult;
import org.trc.util.JSONUtil;
import org.trc.util.Pagenation;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/7
 */
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Financial.ROOT)
public class FinancialResoure {

    @Autowired
    private IFinancialSettlementBiz financialSettlementBiz;

    @GET
    @Path(ScoreAdminConstants.Route.Financial.CONSUMPTION_SUMMARY)
    //@Admin
    public AppResult queryConsumptionSummary(@QueryParam("shopId") Long shopId,
                                             @QueryParam("phone") String phone,
                                             @NotNull @QueryParam("startTime") Long startTime,
                                             @NotNull @QueryParam("endTime") Long endTime,
                                             @BeanParam Pagenation<ConsumptionSummaryDO> page) {

        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setShopId(shopId);
        settlementQuery.setPhone(phone);
        settlementQuery.setStartTime(new Date(startTime));
        settlementQuery.setEndTime(new Date(endTime));

        Pagenation<ConsumptionSummaryDO> result = financialSettlementBiz.queryConsumptionSummary(settlementQuery, page);
        List<ConsumptionSummaryDO> consumptionSummarys = result.getResult();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (ListUtils.isNotEmpty(consumptionSummarys)) {
            for (ConsumptionSummaryDO consumptionSummaryDO : consumptionSummarys) {
                JSONObject json = new JSONObject();
                json.put("userId", consumptionSummaryDO.getUserId());
                json.put("phone", consumptionSummaryDO.getPhone());
                json.put("accountDay", consumptionSummaryDO.getAccountDay());
                json.put("shopName", consumptionSummaryDO.getShopName());
                json.put("exchangeInNum", consumptionSummaryDO.getExchangeInNum());
                json.put("consumeNum", consumptionSummaryDO.getConsumeNum());
                json.put("createTime", consumptionSummaryDO.getCreateTime().getTime());
                jsonArray.add(json);
            }
        }
        //获取统计结转时间区间
        SettlementIntervalDTO settlementIntervalDTO = financialSettlementBiz.getSettlementInterval(settlementQuery);
        if (null != settlementIntervalDTO) {
            if (null == settlementIntervalDTO.getEndTime()) {
                Date tmpTime = new Date();
                settlementQuery.setStartTime(tmpTime);
                settlementQuery.setEndTime(tmpTime);
            } else {
                settlementQuery.setEndTime(settlementIntervalDTO.getEndTime());
            }
        }
        JSONUtil.putParam(jsonArray, result, jsonObject);
        //添加汇总信息
        ConsumptionSummaryStatisticalDataDTO resultSD = financialSettlementBiz.statisticsConsumptionSummary(settlementQuery);
        if (null != resultSD) {
            jsonObject.put("totalExchangeCount", resultSD.getTotalExchangeCount());
            jsonObject.put("exchangeNum", null != resultSD.getExchangeNum() ? resultSD.getExchangeNum() : 0);
            jsonObject.put("totalConsumptionCount", resultSD.getTotalConsumptionCount());
            jsonObject.put("consumptionNum", null != resultSD.getConsumptionNum() ? resultSD.getConsumptionNum() : 0);
        }
        return createSucssAppResult("查询成功!", jsonObject);
    }

    //TODO 导出 @Path(ScoreAdminConstants.Route.Financial.CONSUMPTION_SUMMARY_EXPORT)

    @GET
    @Path(ScoreAdminConstants.Route.Financial.MONTH_CONSUMPTION_SUMMARY)
    //@Admin
    public AppResult queryMonthlyConsumptionSummary(@QueryParam("shopId") Long shopId,
                                                    @QueryParam("phone") String phone,
                                                    @NotNull @QueryParam("startTime") Long startTime,
                                                    @NotNull @QueryParam("endTime") Long endTime,
                                                    @BeanParam Pagenation<ConsumptionSummaryDO> page) {

        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setShopId(shopId);
        settlementQuery.setPhone(phone);
        settlementQuery.setStartTime(new Date(startTime));
        settlementQuery.setEndTime(new Date(endTime));

        Pagenation<ConsumptionSummaryDO> result = financialSettlementBiz.queryMonthConsumptionSummary(settlementQuery, page);
        List<ConsumptionSummaryDO> consumptionSummarys = result.getResult();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (ListUtils.isNotEmpty(consumptionSummarys)) {
            for (ConsumptionSummaryDO consumptionSummaryDO : consumptionSummarys) {
                JSONObject json = new JSONObject();
                json.put("userId", consumptionSummaryDO.getUserId());
                json.put("phone", consumptionSummaryDO.getPhone());
                json.put("accountDay", consumptionSummaryDO.getAccountDay());
                json.put("shopName", consumptionSummaryDO.getShopName());
                json.put("exchangeInNum", consumptionSummaryDO.getExchangeInNum());
                json.put("consumeNum", consumptionSummaryDO.getConsumeNum());
                jsonArray.add(json);
            }
        }
        //获取统计结转时间区间
        SettlementIntervalDTO settlementIntervalDTO = financialSettlementBiz.getSettlementInterval(settlementQuery);
        if (null != settlementIntervalDTO) {
            if (null == settlementIntervalDTO.getEndTime()) {
                Date tmpTime = new Date();
                settlementQuery.setStartTime(tmpTime);
                settlementQuery.setEndTime(tmpTime);
            } else {
                settlementQuery.setEndTime(settlementIntervalDTO.getEndTime());
            }
        }
        JSONUtil.putParam(jsonArray, result, jsonObject);
        //添加汇总信息
        ConsumptionSummaryStatisticalDataDTO resultSD = financialSettlementBiz.statisticsConsumptionSummary(settlementQuery);
        if (null != resultSD) {
            jsonObject.put("totalExchangeCount", resultSD.getTotalExchangeCount());
            jsonObject.put("exchangeNum", null != resultSD.getExchangeNum() ? resultSD.getExchangeNum() : 0);
            jsonObject.put("totalConsumptionCount", resultSD.getTotalConsumptionCount());
            jsonObject.put("consumptionNum", null != resultSD.getConsumptionNum() ? resultSD.getConsumptionNum() : 0);
        }
        return createSucssAppResult("查询成功!", jsonObject);
    }

    //TODO 导出 @Path(ScoreAdminConstants.Route.Financial.MONTH_CONSUMPTION_SUMMARY_EXPORT)

    @GET
    @Path(ScoreAdminConstants.Route.Financial.MEMBERSHIP_SCORE_DAILY_DETAILS)
    //@Admin
    public AppResult queryMembershipScoreDailyDetail(@QueryParam("userId") String userId,
                                                    @NotNull @QueryParam("startTime") Long startTime,
                                                    @NotNull @QueryParam("endTime") Long endTime,
                                                    @BeanParam Pagenation<MembershipScoreDailyDetailsDO> page) {

        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setUserId(userId);
        settlementQuery.setStartTime(new Date(startTime));
        settlementQuery.setEndTime(new Date(endTime));

        Pagenation<MembershipScoreDailyDetailsDO> result = financialSettlementBiz.queryMembershipScoreDailyDetails(settlementQuery, page);
        List<MembershipScoreDailyDetailsDO> membershipDcoreDailyDetails = result.getResult();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (ListUtils.isNotEmpty(membershipDcoreDailyDetails)) {
            for (MembershipScoreDailyDetailsDO consumptionSummaryDO : membershipDcoreDailyDetails) {
                JSONObject json = new JSONObject();
                json.put("userId", consumptionSummaryDO.getUserId());
                json.put("accountDay", consumptionSummaryDO.getAccountDay());
                json.put("exchangeInNum", consumptionSummaryDO.getExchangeInNum());
                json.put("consumeNum", consumptionSummaryDO.getConsumeNum());
                json.put("balance", consumptionSummaryDO.getBalance());
                json.put("createTime", consumptionSummaryDO.getCreateTime().getTime());
                jsonArray.add(json);
            }
        }
        //获取统计结转时间区间
        SettlementIntervalDTO settlementIntervalDTO = financialSettlementBiz.getSettlementIntervalForMembershipScoreDailyDetail(settlementQuery);
        if (null != settlementIntervalDTO) {
            if (null == settlementIntervalDTO.getEndTime()) {
                Date tmpTime = new Date();
                settlementQuery.setStartTime(tmpTime);
                settlementQuery.setEndTime(tmpTime);
            } else {
                settlementQuery.setEndTime(settlementIntervalDTO.getEndTime());
            }
        }
        JSONUtil.putParam(jsonArray, result, jsonObject);
        //添加汇总信息
        ConsumptionSummaryStatisticalDataDTO resultSD = financialSettlementBiz.statisticsConsumptionSummary(settlementQuery);
        if (null != resultSD) {
            jsonObject.put("totalExchangeCount", resultSD.getTotalExchangeCount());
            jsonObject.put("exchangeNum", null != resultSD.getExchangeNum() ? resultSD.getExchangeNum() : 0);
            jsonObject.put("totalConsumptionCount", resultSD.getTotalConsumptionCount());
            jsonObject.put("consumptionNum", null != resultSD.getConsumptionNum() ? resultSD.getConsumptionNum() : 0);
        }
        return createSucssAppResult("查询成功!", jsonObject);
    }

    //TODO 导出 @Path(ScoreAdminConstants.Route.Financial.MEMBERSHIP_SCORE_DAILY_DETAILS_EXPORT)
}

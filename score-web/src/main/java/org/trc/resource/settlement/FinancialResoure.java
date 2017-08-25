package org.trc.resource.settlement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.txframework.util.DateUtils;
import com.txframework.util.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.settlement.IFinancialSettlementBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.constants.TemporaryContext;
import org.trc.domain.dto.ConsumptionSummaryStatisticalDataDTO;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.order.ConsumptionSummaryDO;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.domain.order.OrdersDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.util.*;
import org.trc.validation.VerifyDate;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/7
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Financial.ROOT)
public class FinancialResoure {

    Logger logger = LoggerFactory.getLogger(FinancialResoure.class);
    @Autowired
    private IFinancialSettlementBiz financialSettlementBiz;
    
    @Resource
    private UserService userService;

    @GET
    @Path(ScoreAdminConstants.Route.Financial.CONSUMPTION_SUMMARY)
    //@Admin
    public Pagenation queryConsumptionSummary(@QueryParam("shopId") Long shopId,
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
        //添加汇总信息
        ConsumptionSummaryStatisticalDataDTO resultSD = financialSettlementBiz.statisticsConsumptionSummary(settlementQuery);
        if (null != resultSD) {
            result.setTotalExchangeCount(resultSD.getTotalExchangeCount());
            result.setExchangeNum(null != resultSD.getExchangeNum() ? resultSD.getExchangeNum() : 0);
            result.setTotalConsumptionCount(resultSD.getTotalConsumptionCount());
            result.setConsumptionNum(null != resultSD.getConsumptionNum() ? resultSD.getConsumptionNum() : 0);
        }
        return result;
    }

    @GET
    @Path(ScoreAdminConstants.Route.Financial.CONSUMPTION_SUMMARY_EXPORT)
    public Response exportConsumptionSummary(@QueryParam("shopId") Long shopId,
                                             @QueryParam("phone") String phone,
                                             @NotNull @QueryParam("startTime") Long startTime,
                                             @NotNull @QueryParam("endTime") Long endTime) throws Exception {
        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setShopId(shopId);
        settlementQuery.setPhone(phone);
        settlementQuery.setStartTime(new Date(startTime));
        settlementQuery.setEndTime(new Date(endTime));
        List<ConsumptionSummaryDO> consumptionSummaryList = financialSettlementBiz.queryConsumptionSummaryForExport(settlementQuery);
        //导出文件
        CellDefinition userId = new CellDefinition("userId", "用户Id", CellDefinition.TEXT, 13000);
        CellDefinition accountDay = new CellDefinition("accountDay", "日期", CellDefinition.TEXT, 4000);
        CellDefinition shopName = new CellDefinition("shopName", "店铺名称", CellDefinition.TEXT, 4000);
        CellDefinition phoneCell = new CellDefinition("phone", "会员手机号", CellDefinition.TEXT, 4000);
        CellDefinition exchangeInNum = new CellDefinition("exchangeInNum", "兑入积分数量", CellDefinition.NUM_0, 4000);
        CellDefinition consumeNum = new CellDefinition("consumeNum", "消费积分数量", CellDefinition.NUM_0, 4000);
        List<CellDefinition> cellDefinitionList = new ArrayList<>();
        cellDefinitionList.add(userId);
        cellDefinitionList.add(accountDay);
        cellDefinitionList.add(shopName);
        cellDefinitionList.add(phoneCell);
        cellDefinitionList.add(exchangeInNum);
        cellDefinitionList.add(consumeNum);
        String sheetName = "用户消费明细";
        String fileName = "用户消费明细-" + DateUtils.formatDate(settlementQuery.getStartTime(), DateUtils.DATE_PATTERN) + "-" + DateUtils.formatDate(settlementQuery.getEndTime(), DateUtils.DATE_PATTERN) + ".xls";
        fileName = URLEncoder.encode(fileName, "UTF-8");
        HSSFWorkbook hssfWorkbook = ExportExcel.generateExcel(consumptionSummaryList, cellDefinitionList, sheetName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        hssfWorkbook.write(stream);
        /**模拟数据结束*/
        return Response.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8'zh_cn'" + fileName).type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Cache-Control", "no-cache").build();
    }

    @GET
    @Path(ScoreAdminConstants.Route.Financial.MONTH_CONSUMPTION_SUMMARY)
    public Pagenation queryMonthlyConsumptionSummary(@QueryParam("shopId") Long shopId,
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
        //添加汇总信息
        ConsumptionSummaryStatisticalDataDTO resultSD = financialSettlementBiz.statisticsConsumptionSummary(settlementQuery);
        if (null != resultSD) {
            result.setTotalExchangeCount(resultSD.getTotalExchangeCount());
            result.setExchangeNum(null != resultSD.getExchangeNum() ? resultSD.getExchangeNum() : 0);
            result.setTotalConsumptionCount(resultSD.getTotalConsumptionCount());
            result.setConsumptionNum(null != resultSD.getConsumptionNum() ? resultSD.getConsumptionNum() : 0);
        }
        return result;
    }

    @GET
    @Path(ScoreAdminConstants.Route.Financial.MONTH_CONSUMPTION_SUMMARY_EXPORT)
    public Response exportMonthConsumptionSummary(@QueryParam("shopId") Long shopId,
                                                  @QueryParam("phone") String phone,
                                                  @NotNull @QueryParam("startTime") Long startTime,
                                                  @NotNull @QueryParam("endTime") Long endTime) throws Exception {
        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setShopId(shopId);
        settlementQuery.setPhone(phone);
        settlementQuery.setStartTime(new Date(startTime));
        settlementQuery.setEndTime(new Date(endTime));
        List<ConsumptionSummaryDO> consumptionSummaryList = financialSettlementBiz.queryMonthConsumptionSummaryForExport(settlementQuery);
        //导出文件
        CellDefinition userId = new CellDefinition("userId", "用户Id", CellDefinition.TEXT, 13000);
        CellDefinition accountDay = new CellDefinition("accountDay", "日期", CellDefinition.TEXT, 4000);
        CellDefinition shopName = new CellDefinition("shopName", "店铺名称", CellDefinition.TEXT, 4000);
        CellDefinition phoneCell = new CellDefinition("phone", "会员手机号", CellDefinition.TEXT, 4000);
        CellDefinition exchangeInNum = new CellDefinition("exchangeInNum", "兑入积分数量", CellDefinition.NUM_0, 4000);
        CellDefinition consumeNum = new CellDefinition("consumeNum", "积分消费数量", CellDefinition.NUM_0, 4000);
        CellDefinition lotteryConsumeNum = new CellDefinition("lotteryConsumeNum", "积分抽奖数量", CellDefinition.NUM_0, 4000);
        CellDefinition consumeCorrectNum = new CellDefinition("consumeCorrectNum", "退积分数量", CellDefinition.NUM_0, 4000);
        List<CellDefinition> cellDefinitionList = new ArrayList<>();
        cellDefinitionList.add(userId);
        cellDefinitionList.add(accountDay);
        cellDefinitionList.add(shopName);
        cellDefinitionList.add(phoneCell);
        cellDefinitionList.add(exchangeInNum);
        cellDefinitionList.add(consumeNum);
        cellDefinitionList.add(lotteryConsumeNum);
        cellDefinitionList.add(consumeCorrectNum);
        String sheetName = "月度消费明细";
        String fileName = "月度消费明细-" + DateUtils.formatDate(settlementQuery.getStartTime(), DateUtils.DATE_PATTERN) + "-" + DateUtils.formatDate(settlementQuery.getEndTime(), DateUtils.DATE_PATTERN) + ".xls";
        fileName = URLEncoder.encode(fileName, "UTF-8");
        HSSFWorkbook hssfWorkbook = ExportExcel.generateExcel(consumptionSummaryList, cellDefinitionList, sheetName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        hssfWorkbook.write(stream);
        /**模拟数据结束*/
        return Response.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8'zh_cn'" + fileName).type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Cache-Control", "no-cache").build();

    }

    @GET
    @Path(ScoreAdminConstants.Route.Financial.MEMBERSHIP_SCORE_DAILY_DETAILS)
    public Pagenation queryMembershipScoreDailyDetail(@QueryParam("userId") String userId,
                                                    @VerifyDate @QueryParam("startTime") Long startTime,
                                                    @VerifyDate @QueryParam("endTime") Long endTime,
                                                    @BeanParam Pagenation<MembershipScoreDailyDetailsDO> page) {

        SettlementQuery settlementQuery = new SettlementQuery();
        if(StringUtils.isNotBlank(userId)){
            settlementQuery.setUserId(userId);
        }
        if(startTime == null || endTime == null) {
            throw new BusinessException(ExceptionEnum.PARAM_CHECK_EXCEPTION,"起止时间不能为空");
        }
        settlementQuery.setStartTime(new Date(startTime));
        settlementQuery.setEndTime(new Date(endTime));

        Pagenation<MembershipScoreDailyDetailsDO> result = financialSettlementBiz.queryMembershipScoreDailyDetails(settlementQuery, page);
        if (result != null && ListUtils.isNotEmpty(result.getInfos()) && result.getInfos().size() > 0) {
        	List<MembershipScoreDailyDetailsDO> membershipScoreDailyDetails = result.getInfos();
            for (int i= 0; i < membershipScoreDailyDetails.size();i++) {
            	MembershipScoreDailyDetailsDO membershipScoreDailyDetail = membershipScoreDailyDetails.get(i);
            	UserDO userDO = userService.getUserDO(QueryType.UserId, membershipScoreDailyDetail.getUserId());
            	membershipScoreDailyDetail.setUserPhone(userDO.getPhone());
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
        //添加汇总信息
        ConsumptionSummaryStatisticalDataDTO resultSD = financialSettlementBiz.statisticsConsumptionSummary(settlementQuery);
        if (null != resultSD) {
            result.setTotalExchangeCount(resultSD.getTotalExchangeCount());
            result.setExchangeNum(null != resultSD.getExchangeNum() ? resultSD.getExchangeNum() : 0);
            result.setTotalConsumptionCount(resultSD.getTotalConsumptionCount());
            result.setConsumptionNum(null != resultSD.getConsumptionNum() ? resultSD.getConsumptionNum() : 0);
        }
        return result;
    }

    @GET
    @Path(ScoreAdminConstants.Route.Financial.MEMBERSHIP_SCORE_DAILY_DETAILS_EXPORT)
    public Response exportMembershipScoreDailyDetail(@QueryParam("userId") String userId,
                                                     @NotNull @QueryParam("startTime") Long startTime,
                                                     @NotNull @QueryParam("endTime") Long endTime) throws Exception{
        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setUserId(userId);
        settlementQuery.setStartTime(new Date(startTime));
        settlementQuery.setEndTime(new Date(endTime));
        List<MembershipScoreDailyDetailsDO> consumptionSummaryList = financialSettlementBiz.queryMembershipScoreDailyDetailForExport(settlementQuery);
        //导出文件
        CellDefinition userIdCell = new CellDefinition("userId", "用户Id", CellDefinition.TEXT, 13000);
        CellDefinition accountDay = new CellDefinition("accountDay", "日期", CellDefinition.TEXT, 4000);
        CellDefinition exchangeInNum = new CellDefinition("exchangeInNum", "兑入积分数量", CellDefinition.NUM_0, 4000);
        CellDefinition consumeNum = new CellDefinition("consumeNum", "积分消费数量", CellDefinition.NUM_0, 4000);
        CellDefinition lotteryConsumeNum = new CellDefinition("lotteryConsumeNum", "积分抽奖数量", CellDefinition.NUM_0, 4000);
        CellDefinition consumeCorrectNum = new CellDefinition("consumeCorrectNum", "退积分数量", CellDefinition.NUM_0, 4000);
        CellDefinition balance = new CellDefinition("balance", "结余积分数量", CellDefinition.NUM_0, 4000);
        List<CellDefinition> cellDefinitionList = new ArrayList<>();
        cellDefinitionList.add(userIdCell);
        cellDefinitionList.add(accountDay);
        cellDefinitionList.add(exchangeInNum);
        cellDefinitionList.add(consumeNum);
        cellDefinitionList.add(lotteryConsumeNum);
        cellDefinitionList.add(consumeCorrectNum);
        cellDefinitionList.add(balance);
        String sheetName = "会员积分日结明细";
        String fileName = "会员积分日结明细-" + DateUtils.formatDate(settlementQuery.getStartTime(), DateUtils.DATE_PATTERN) + "-" + DateUtils.formatDate(settlementQuery.getEndTime(), DateUtils.DATE_PATTERN) + ".xls";
        fileName = URLEncoder.encode(fileName, "UTF-8");
        HSSFWorkbook hssfWorkbook = ExportExcel.generateExcel(consumptionSummaryList, cellDefinitionList, sheetName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        hssfWorkbook.write(stream);
        /**模拟数据结束*/
        return Response.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8'zh_cn'" + fileName).type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Cache-Control", "no-cache").build();

    }
}

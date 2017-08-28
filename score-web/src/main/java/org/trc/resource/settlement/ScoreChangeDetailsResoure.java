package org.trc.resource.settlement;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.score.IScoreChangeRecordBiz;
import org.trc.biz.settlement.IFinancialSettlementBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.dto.ConsumptionSummaryStatisticalDataDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.dto.ScoreChangeRecordsDTO;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.shop.ShopDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.FlowException;
import org.trc.util.CellDefinition;
import org.trc.util.ExportExcel;
import org.trc.util.Pagenation;

import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.txframework.util.DateUtils;
import com.txframework.util.ListUtils;

/**
 * 
 * @Description: 积分流转明细
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月23日 下午4:35:04
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.ScoreChange.ROOT)
public class ScoreChangeDetailsResoure {

    Logger logger = LoggerFactory.getLogger(ScoreChangeDetailsResoure.class);
    @Autowired
    private IScoreChangeRecordBiz scoreChangeRecordBiz;
    @Autowired
    private UserService userService;
    
    @Autowired
    private IFinancialSettlementBiz financialSettlementBiz;
    @Autowired
    private IShopBiz shopBiz;
    @Autowired
    private IAuthBiz authBiz;
    
    @GET
    @Path(ScoreAdminConstants.Route.ScoreChange.DETAILS)
    public Pagenation queryConsumptionSummary(@QueryParam("shopId") Long shopId,
                                             @QueryParam("userPhone") String userPhone,
                                             @QueryParam("businessCode") String businessCode,
                                             @NotNull @QueryParam("startTime") Long startTime,
                                             @NotNull @QueryParam("endTime") Long endTime,
                                             @BeanParam Pagenation<ScoreChangeRecordsDTO> page) {

    	 ScoreChangeRecordQueryDTO queryDto = new ScoreChangeRecordQueryDTO();
         if (StringUtils.isNotBlank(userPhone)) {
             UserDO userDO = userService.getUserDO(QueryType.Phone, userPhone);
             if (null == userDO) {
                 throw new FlowException(ExceptionEnum.ORDER_QUERY_EXCEPTION,"手机号不存在!");
             }
             queryDto.setUserId(userDO.getUserId());
         }
         if (shopId!=null) {
             ShopDO shopDO = shopBiz.getShopDOById(shopId);
             if (null == shopDO) {
                 throw new FlowException(ExceptionEnum.ORDER_QUERY_EXCEPTION,"该店铺不存在!");
             }
             queryDto.setTheOtherUserId(shopDO.getUserId());
         }
         queryDto.setShopId(shopId);
         queryDto.calBusinessCodes(businessCode);

         if (null != startTime) {
             queryDto.setOperateTimeMin(new Date(startTime));
         }
         if (null != endTime) {
             queryDto.setOperateTimeMax(new Date(endTime));
         }
         Pagenation<ScoreChangeRecordsDTO> scoreChangePge = scoreChangeRecordBiz.queryScoreChangeRecordsForUser(queryDto, page);
         List<ScoreChangeRecordsDTO> scoreChanges = scoreChangePge.getInfos();
         if (ListUtils.isNotEmpty(scoreChanges)) {
                 for (int i= 0 ; i < scoreChanges.size();i++) {
                	 ScoreChangeRecordsDTO scoreChangeRecordsDTO = scoreChanges.get(i);
                     UserDO userDO =  userService.getUserDO(QueryType.UserId, scoreChangeRecordsDTO.getUserId());
                     if (null != userDO) {
                    	 scoreChangeRecordsDTO.setUserPhone(userDO.getPhone());
                     }
                 }
         }
         SettlementQuery settlementQuery = new SettlementQuery();
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
        	 scoreChangePge.setTotalExchangeCount(resultSD.getTotalExchangeCount());
        	 scoreChangePge.setExchangeNum(null != resultSD.getExchangeNum() ? resultSD.getExchangeNum() : 0);
        	 scoreChangePge.setTotalConsumptionCount(resultSD.getTotalConsumptionCount());
        	 scoreChangePge.setConsumptionNum(null != resultSD.getConsumptionNum() ? resultSD.getConsumptionNum() : 0);
         }
         return scoreChangePge;
    }

    @GET
    @Path(ScoreAdminConstants.Route.ScoreChange.EXPORT)
    public Response exportConsumptionSummary(@QueryParam("shopId") Long shopId,
                                             @QueryParam("userPhone") String userPhone,
                                             @QueryParam("businessCode") String businessCode,
                                             @NotNull @QueryParam("startTime") Long startTime,
                                             @NotNull @QueryParam("endTime") Long endTime) throws Exception {
    	
    	ScoreChangeRecordQueryDTO queryDto = new ScoreChangeRecordQueryDTO();
        if (StringUtils.isNotBlank(userPhone)) {
            UserDO userDO = userService.getUserDO(QueryType.Phone, userPhone);
            if (null == userDO) {
                throw new FlowException(ExceptionEnum.ORDER_QUERY_EXCEPTION,"手机号不存在!");
            }
            queryDto.setUserId(userDO.getUserId());
        }
        queryDto.setShopId(shopId);
        queryDto.calBusinessCodes(businessCode);

        if (null != startTime) {
            queryDto.setOperateTimeMin(new Date(startTime));
        }
        if (null != endTime) {
            queryDto.setOperateTimeMax(new Date(endTime));
        }
        List<ScoreChangeRecordsDTO> scoreChanges = scoreChangeRecordBiz.queryScoreChangeRecordsForUserExport(queryDto);
        if (ListUtils.isNotEmpty(scoreChanges)) {
            for (int i= 0 ; i < scoreChanges.size();i++) {
           	 ScoreChangeRecordsDTO scoreChangeRecordsDTO = scoreChanges.get(i);
                UserDO userDO =  userService.getUserDO(QueryType.UserId, scoreChangeRecordsDTO.getUserId());
                if (null != userDO) {
               	 scoreChangeRecordsDTO.setUserPhone(userDO.getPhone());
                }
            }
        }
    	 //导出文件
        CellDefinition userId = new CellDefinition("userId", "用户Id", CellDefinition.TEXT, 4000);
        CellDefinition operationTime = new CellDefinition("operationTime", "发生日期", CellDefinition.DATE, 4000);
        CellDefinition userName = new CellDefinition("userName", "会员名称", CellDefinition.TEXT, 4000);
        CellDefinition userPhonec = new CellDefinition("userPhone", "会员手机", CellDefinition.TEXT, 6000);
        CellDefinition shopName = new CellDefinition("shopName", "所属商家", CellDefinition.TEXT, 4000);
        CellDefinition orderCode = new CellDefinition("orderCode", "订单编号", CellDefinition.TEXT, 8000);
        CellDefinition businessName = new CellDefinition("businessCode", "积分行为", CellDefinition.TEXT, 4000);
        CellDefinition score = new CellDefinition("score", "积分数量", CellDefinition.NUM_0, 4000);
        CellDefinition remark = new CellDefinition("remark", "详细说明", CellDefinition.TEXT, 5000);

 
        List<CellDefinition> cellDefinitionList = new ArrayList<>();
        cellDefinitionList.add(userId);
        cellDefinitionList.add(operationTime);
        cellDefinitionList.add(userName);
        cellDefinitionList.add(userPhonec);
        cellDefinitionList.add(shopName);
        cellDefinitionList.add(orderCode);
        cellDefinitionList.add(businessName);
        cellDefinitionList.add(score);
        cellDefinitionList.add(remark);
        String sheetName = "积分流水明细";
        String fileName = "积分流水明细-" + DateUtils.formatDate(queryDto.getOperateTimeMin(), DateUtils.DATE_PATTERN) + "-" + DateUtils.formatDate(queryDto.getOperateTimeMax(), DateUtils.DATE_PATTERN) + ".xls";
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HSSFWorkbook hssfWorkbook = ExportExcel.generateExcel(scoreChanges, cellDefinitionList, sheetName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        hssfWorkbook.write(stream);
        /**模拟数据结束*/
        return Response.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8'zh_cn'" + fileName).type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Cache-Control", "no-cache").build();
    }

}

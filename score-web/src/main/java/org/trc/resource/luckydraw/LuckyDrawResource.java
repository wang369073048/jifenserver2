package org.trc.resource.luckydraw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.txframework.util.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.luckydraw.ILuckyDrawBiz;
import org.trc.biz.luckydraw.IWinningRecordBiz;
import org.trc.biz.order.INewOrderBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.WinningRecordDTO;
import org.trc.domain.luckydraw.ActivityDetailDO;
import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.domain.luckydraw.WinningRecordDO;
import org.trc.domain.order.LogisticsDO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.query.DateQuery;
import org.trc.domain.shop.ManagerDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.OrderException;
import org.trc.util.AppResult;
import org.trc.util.CellDefinition;
import org.trc.util.ExportExcel;
import org.trc.util.Pagenation;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.LuckyDraw.ROOT)
public class LuckyDrawResource {
    private Logger logger = LoggerFactory.getLogger(LuckyDrawResource.class);
    @Autowired
    private IShopBiz shopBiz;
    @Autowired
    private INewOrderBiz newOrderBiz;
    @Autowired
    private IAuthBiz authBiz;
    @Autowired
    private ILuckyDrawBiz luckyDrawBiz;
    @Autowired
    private IWinningRecordBiz winningRecordBiz;

    /**
     * @param orderNum
     * @param companyName
     * @param shipperCode
     * @param logisticsNum
     * @param remark
     * @param requestContext
     * @return
     * @throws Exception
     */
    @POST
    @Path(ScoreAdminConstants.Route.LuckyDraw.SHIP)
    public AppResult shipOrder(@FormParam("orderNum") String orderNum,
                               @FormParam("companyName") String companyName,
                               @FormParam("shipperCode") String shipperCode,
                               @FormParam("logisticsNum") String logisticsNum,
                               @FormParam("remark") String remark,
                               @Context ContainerRequestContext requestContext) throws Exception {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        ManagerDO manager = shopBiz.getManagerByUserId(userId);
        OrdersDO param = new OrdersDO();
        param.setOrderNum(orderNum);
        OrdersDO order = newOrderBiz.selectByParams(param);
        if (!order.getShopId().equals(manager.getShopId())) {
            throw new OrderException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }
        LogisticsDO logistics = new LogisticsDO(order.getId(), companyName, shipperCode, logisticsNum, null, userId, null);
        LogisticsDO logisticsDO = newOrderBiz.shipPrizes(orderNum, remark, logistics);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logisticsId", logisticsDO.getId());
        return createSucssAppResult("操作成功", jsonObject);
    }

    @POST
    public AppResult add(@NotEmpty @FormParam("platform") String platform, @NotEmpty @FormParam("activityName") String activityName,
                         @NotNull @FormParam("startTime") Long startTime, @NotNull @FormParam("endTime") Long endTime,
                         @FormParam("freeLotteryTimes") Integer freeLotteryTimes, @FormParam("freeDrawType") String freeDrawType,
                         @FormParam("expenditure") Integer expenditure, @FormParam("dailyDrawLimit") Integer dailyDrawLimit,
                         @FormParam("appBackground") String appBackground, @FormParam("webBackground") String webBackground,
                         @FormParam("activityRules") String activityRules, @NotEmpty @FormParam("activityPrizes") String activityPrizes,
                         @Context ContainerRequestContext requestContext) {
        LuckyDrawDO luckyDraw = new LuckyDrawDO();
        luckyDraw.setPlatform(platform);
        luckyDraw.setActivityName(activityName);
        if (null != startTime) {
            luckyDraw.setStartTime(new Date(startTime));
        }
        if (null != endTime) {
            luckyDraw.setEndTime(new Date(endTime));
        }
        luckyDraw.setFreeLotteryTimes(freeLotteryTimes);
        luckyDraw.setFreeDrawType(freeDrawType);
        luckyDraw.setExpenditure(expenditure);
        luckyDraw.setDailyDrawLimit(dailyDrawLimit);
        luckyDraw.setAppBackground(appBackground);
        luckyDraw.setWebBackground(webBackground);
        luckyDraw.setIsDeleted(0);
        luckyDraw.setActivityRules(activityRules);
        Date time = Calendar.getInstance().getTime();
        luckyDraw.setCreateTime(time);
        luckyDraw.setUpdateTime(time);
        List<ActivityPrizesDO> activityPrizeList = (List) JSON.parseArray(activityPrizes, ActivityPrizesDO.class);
        luckyDraw.setActivityPrizesList(activityPrizeList);
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        luckyDraw.setShopId(auth.getShopId());
        luckyDrawBiz.insertLuckyDraw(luckyDraw);
        return createSucssAppResult("操作成功", "");
    }

    @PUT
    public AppResult edit(@NotNull @FormParam("id") Long id, @NotEmpty @FormParam("platform") String platform, @NotEmpty @FormParam("activityName") String activityName,
                         @NotNull @FormParam("startTime") Long startTime, @NotNull @FormParam("endTime") Long endTime,
                         @FormParam("freeLotteryTimes") Integer freeLotteryTimes, @FormParam("freeDrawType") String freeDrawType,
                         @FormParam("expenditure") Integer expenditure, @FormParam("dailyDrawLimit") Integer dailyDrawLimit,
                         @FormParam("appBackground") String appBackground, @FormParam("webBackground") String webBackground,
                         @FormParam("activityRules") String activityRules, @NotEmpty @FormParam("activityPrizes") String activityPrizes,
                         @Context ContainerRequestContext requestContext) {
        LuckyDrawDO luckyDraw = new LuckyDrawDO();
        luckyDraw.setId(id);
        luckyDraw.setPlatform(platform);
        luckyDraw.setActivityName(activityName);
        if (null != startTime) {
            luckyDraw.setStartTime(new Date(startTime));
        }
        if (null != endTime) {
            luckyDraw.setEndTime(new Date(endTime));
        }
        luckyDraw.setFreeLotteryTimes(freeLotteryTimes);
        luckyDraw.setFreeDrawType(freeDrawType);
        luckyDraw.setExpenditure(expenditure);
        luckyDraw.setDailyDrawLimit(dailyDrawLimit);
        luckyDraw.setAppBackground(appBackground);
        luckyDraw.setWebBackground(webBackground);
        luckyDraw.setActivityRules(activityRules);
        Date time = Calendar.getInstance().getTime();
        luckyDraw.setUpdateTime(time);
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        luckyDraw.setShopId(auth.getShopId());
        List<ActivityPrizesDO> activityPrizeList = (List) JSON.parseArray(activityPrizes, ActivityPrizesDO.class);
        luckyDraw.setActivityPrizesList(activityPrizeList);
        luckyDrawBiz.updateLuckyDraw(luckyDraw);
        return createSucssAppResult("操作成功", "");
    }

    @DELETE
    @Path("/{id}")
    public AppResult delete(@PathParam("id")Long id, @Context ContainerRequestContext requestContext){
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        LuckyDrawDO luckyDraw = new LuckyDrawDO();
        luckyDraw.setId(id);
        luckyDraw.setShopId(auth.getShopId());
        luckyDrawBiz.deleteEntity(luckyDraw);
        return createSucssAppResult("删除成功", "");
    }

    /**
     * 查询抽奖活动
     *
     * @return
     */
    @GET
    @Path("/{id}")
    public AppResult getEntityById(@PathParam("id") Long id,@Context ContainerRequestContext requestContext) {
        LuckyDrawDO luckyDraw = new LuckyDrawDO();
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        luckyDraw.setId(id);
        luckyDraw.setShopId(auth.getShopId());
        LuckyDrawDO result = luckyDrawBiz.getLuckyDraw(luckyDraw);
        return createSucssAppResult("查询成功", result);
    }

    @GET
    @Path(ScoreAdminConstants.Route.LuckyDraw.LIST)
        public Pagenation list(@QueryParam("activityName") String activityName, @QueryParam("operateTimeMin") Long operateTimeMin,
                         @QueryParam("operateTimeMax") Long operateTimeMax, @QueryParam("state") Integer state,
                         @BeanParam Pagenation<LuckyDrawDO> page,@Context ContainerRequestContext requestContext){
            LuckyDrawDO luckyDraw = new LuckyDrawDO();
            //获取userId
            String userId = (String) requestContext.getProperty("userId");
            Auth auth = authBiz.getAuthByUserId(userId);
            luckyDraw.setShopId(auth.getShopId());
            luckyDraw.setActivityName(activityName);
            if (null != operateTimeMin) {
                luckyDraw.setOperateTimeMin(new Date(operateTimeMin));
            }
            if (null != operateTimeMax) {
                luckyDraw.setOperateTimeMax(new Date(operateTimeMax));
            }
            luckyDraw.setState(state);
            luckyDraw.setIsDeleted(0);
            return luckyDrawBiz.queryLuckyDraw(luckyDraw, page);

    }


    /**
     * 中奖信息
     *
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.LuckyDraw.WINNING_RECORD+"/{id}")
    public AppResult getWinningRecordById(@PathParam("id") Long id,@Context ContainerRequestContext requestContext) {
            WinningRecordDO param = new WinningRecordDO();
            //获取userId
            String userId = (String) requestContext.getProperty("userId");
            ManagerDO manager = shopBiz.getManagerByUserId(userId);
            param.setId(id);
            param.setShopId(manager.getShopId());
            WinningRecordDTO result = winningRecordBiz.getWinningRecord(param);
            return createSucssAppResult("查询成功", result);

    }


    @GET
    @Path(ScoreAdminConstants.Route.LuckyDraw.WINNING_RECORD_LIST)
    public Pagenation listWinningRecord(@QueryParam("luckyDrawId") Long luckyDrawId, @QueryParam("platform") String platform, @QueryParam("operateTimeMin") Long operateTimeMin,
    									@QueryParam("operateTimeMax") Long operateTimeMax, @QueryParam("lotteryPhone") String lotteryPhone,
    									@QueryParam("activityName") String activityName, @QueryParam("state") Integer state,
    									@BeanParam Pagenation<WinningRecordDTO> page){
            WinningRecordDTO param = new WinningRecordDTO();
            param.setLuckyDrawId(luckyDrawId);
            param.setPlatform(platform);
            param.setLotteryPhone(lotteryPhone);
            param.setActivityName(activityName);
            param.setState(state);
            if (null != operateTimeMin) {
                param.setOperateTimeMin(new Date(operateTimeMin));
            }
            if (null != operateTimeMax) {
                param.setOperateTimeMax(new Date(operateTimeMax));
            }
            Pagenation<WinningRecordDTO> result = winningRecordBiz.queryWinningRecord(param, page);
           return result;
    }

    @GET
    @Path(ScoreAdminConstants.Route.LuckyDraw.WINNING_RECORD_EXPORT)
    public Response exportWinningRecord(@QueryParam("platform") String platform, @QueryParam("operateTimeMin") Long operateTimeMin,
                                        @QueryParam("operateTimeMax") Long operateTimeMax, @QueryParam("lotteryPhone") String lotteryPhone,
                                        @QueryParam("activityName") String activityName, @QueryParam("state") Integer state) throws IOException {
        WinningRecordDTO param = new WinningRecordDTO();
        param.setPlatform(platform);
        param.setLotteryPhone(lotteryPhone);
        param.setActivityName(activityName);
        param.setState(state);
        DateQuery dateQuery = new DateQuery();
        if (null != operateTimeMin) {
            dateQuery.setOperateTimeMin(new Date(operateTimeMin));
        }
        if (null != operateTimeMax) {
            dateQuery.setOperateTimeMax(new Date(operateTimeMax));
        }
        List<WinningRecordDTO> result = winningRecordBiz.listWinningRecord(param);
        //导出文件
        CellDefinition activityNameCell = new CellDefinition("activityName", "活动名称", CellDefinition.TEXT, 4000);
        CellDefinition drawTime = new CellDefinition("drawTime", "中奖时间", CellDefinition.DATE_TIME, 8000);
        CellDefinition platformCell = new CellDefinition("platformDesc", "所属平台", CellDefinition.TEXT, 4000);
        CellDefinition orderNum = new CellDefinition("orderNum", "订单编号", CellDefinition.TEXT, 9000);
        CellDefinition prizeName = new CellDefinition("prizeName", "奖品名称", CellDefinition.TEXT, 4000);
        CellDefinition numberOfPrizes = new CellDefinition("numberOfPrizes", "奖品数量", CellDefinition.TEXT, 4000);
        CellDefinition goodsType = new CellDefinition("goodsTypeDesc", "商品类型", CellDefinition.TEXT, 4000);
        CellDefinition goodsNo = new CellDefinition("goodsNo", "商品货号", CellDefinition.TEXT, 4000);
        CellDefinition lotteryPhoneCell = new CellDefinition("lotteryPhone", "手机", CellDefinition.TEXT, 4000);
        CellDefinition province = new CellDefinition("province", "省", CellDefinition.TEXT, 4000);
        CellDefinition city = new CellDefinition("city", "市", CellDefinition.TEXT, 4000);
        CellDefinition area = new CellDefinition("area", "区", CellDefinition.TEXT, 4000);
        CellDefinition address = new CellDefinition("address", "详细地址", CellDefinition.TEXT, 16000);
        CellDefinition receiverName = new CellDefinition("receiverName", "收件人", CellDefinition.TEXT, 4000);
        CellDefinition stateCell = new CellDefinition("stateDesc", "奖品状态", CellDefinition.TEXT, 4000);

        List<CellDefinition> cellDefinitionList = new ArrayList<>();
        cellDefinitionList.add(activityNameCell);
        cellDefinitionList.add(drawTime);
        cellDefinitionList.add(platformCell);
        cellDefinitionList.add(orderNum);
        cellDefinitionList.add(prizeName);
        cellDefinitionList.add(numberOfPrizes);
        cellDefinitionList.add(goodsType);
        cellDefinitionList.add(goodsNo);
        cellDefinitionList.add(lotteryPhoneCell);
        cellDefinitionList.add(province);
        cellDefinitionList.add(city);
        cellDefinitionList.add(area);
        cellDefinitionList.add(address);
        cellDefinitionList.add(receiverName);
        cellDefinitionList.add(stateCell);

        String sheetName = "获奖列表";
        String fileName = "获奖列表" + ((null != param.getOperateTimeMin()) ? "-" + DateUtils.formatDate(param.getOperateTimeMin(), DateUtils.DATE_PATTERN) : "") + (null != param.getOperateTimeMax() ? "-" + DateUtils.formatDate(param.getOperateTimeMax(), DateUtils.DATE_PATTERN) : "") + ".xls";
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HSSFWorkbook hssfWorkbook = ExportExcel.generateExcel(result, cellDefinitionList, sheetName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        hssfWorkbook.write(stream);
        /**模拟数据结束*/
        return Response.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8'zh_cn'" + fileName).type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Cache-Control", "no-cache").build();
    }


    @GET
    @Path(ScoreAdminConstants.Route.LuckyDraw.ACTIVITY_DETAIL_LIST)
    public Pagenation<ActivityDetailDO> listAcitivityDetail(@QueryParam("shopId") Long shopId, @QueryParam("lotteryPhone") String lotteryPhone,
                                                            @QueryParam("operateTimeMin") Long operateTimeMin, @QueryParam("operateTimeMax") Long operateTimeMax,
                                                            @QueryParam("whetherWinning") Integer whetherWinning,
                                                            @BeanParam Pagenation<ActivityDetailDO> page) {
        ActivityDetailDO param = new ActivityDetailDO();
        param.setLotteryPhone(lotteryPhone);
        param.setShopId(shopId);
        param.setWhetherWinning(whetherWinning);
        if (null != operateTimeMin) {
            param.setOperateTimeMin(new Date(operateTimeMin));
        }
        if (null != operateTimeMax) {
            param.setOperateTimeMax(new Date(operateTimeMax));
        }
        return winningRecordBiz.queryActivityDetail(param, page);
    }

    @GET
    @Path(ScoreAdminConstants.Route.LuckyDraw.ACTIVITY_DETAIL_EXPORT)
    public Response exportAcitivityDetail(@QueryParam("shopId") Long shopId, @QueryParam("lotteryPhone") String lotteryPhone,
                                          @QueryParam("operateTimeMin") Long operateTimeMin, @QueryParam("operateTimeMax") Long operateTimeMax,
                                          @QueryParam("whetherWinning") Integer whetherWinning) throws IOException {
        ActivityDetailDO param = new ActivityDetailDO();
        param.setLotteryPhone(lotteryPhone);
        param.setShopId(shopId);
        param.setWhetherWinning(whetherWinning);
        if (null != operateTimeMin) {
            param.setOperateTimeMin(new Date(operateTimeMin));
        }
        if (null != operateTimeMax) {
            param.setOperateTimeMax(new Date(operateTimeMax));
        }
        List<ActivityDetailDO> result = winningRecordBiz.listActivityDetail(param);
        //导出文件
        CellDefinition userId = new CellDefinition("userId", "userid", CellDefinition.TEXT, 9000);
        CellDefinition drawTime = new CellDefinition("drawTime", "中奖时间", CellDefinition.DATE_TIME, 8000);
        CellDefinition shopName = new CellDefinition("shopName", "店铺名称", CellDefinition.TEXT, 4000);
        CellDefinition activityNameCell = new CellDefinition("activityName", "活动名称", CellDefinition.TEXT, 4000);
        CellDefinition lotteryPhoneCell = new CellDefinition("lotteryPhone", "会员手机号", CellDefinition.TEXT, 4000);
        CellDefinition prizeName = new CellDefinition("prizeName", "奖品名称", CellDefinition.TEXT, 4000);
        CellDefinition numberOfPrizes = new CellDefinition("numberOfPrizes", "奖品数量", CellDefinition.TEXT, 4000);
        CellDefinition goodsType = new CellDefinition("goodsTypeDesc", "奖品类型", CellDefinition.TEXT, 4000);
        CellDefinition whetherWinningCell = new CellDefinition("whetherWinningDesc", "是否中奖", CellDefinition.TEXT, 4000);

        List<CellDefinition> cellDefinitionList = new ArrayList<>();
        cellDefinitionList.add(userId);
        cellDefinitionList.add(drawTime);
        cellDefinitionList.add(shopName);
        cellDefinitionList.add(activityNameCell);
        cellDefinitionList.add(prizeName);
        cellDefinitionList.add(numberOfPrizes);
        cellDefinitionList.add(goodsType);
        cellDefinitionList.add(lotteryPhoneCell);
        cellDefinitionList.add(whetherWinningCell);

        String sheetName = "抽奖报表";
        String fileName = "抽奖报表" + ((null != param.getOperateTimeMin()) ? "-" + DateUtils.formatDate(param.getOperateTimeMin(), DateUtils.DATE_PATTERN) : "") + (null != param.getOperateTimeMax() ? "-" + DateUtils.formatDate(param.getOperateTimeMax(), DateUtils.DATE_PATTERN) : "") + ".xls";
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HSSFWorkbook hssfWorkbook = ExportExcel.generateExcel(result, cellDefinitionList, sheetName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        hssfWorkbook.write(stream);
        /**模拟数据结束*/
        return Response.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8'zh_cn'" + fileName).type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Cache-Control", "no-cache").build();

    }
}

package org.trc.resource.luckydraw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.luckydraw.ILuckyDrawBiz;
import org.trc.biz.order.INewOrderBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.domain.order.LogisticsDO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.shop.ManagerDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.OrderException;
import org.trc.util.AppResult;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    private ILuckyDrawBiz luckyDrawBiz;

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
        logger.info("很不友好的版本测试!");
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
}

package org.trc.resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.constants.CommonConstants;
import com.tairanchina.context.TxJerseyRestContextFactory;
import com.tairanchina.engine.jersey.TxJerseyTools;
import com.trc.mall.constants.ScoreConstants;
import com.trc.mall.dto.LotteryLimitDto;
import com.trc.mall.dto.WinningRecordDTO;
import com.trc.mall.exception.BusinessException;
import com.trc.mall.exception.LuckyDrawException;
import com.trc.mall.exception.OrderAddressException;
import com.trc.mall.exception.ScoreException;
import com.trc.mall.interceptor.Logined;
import com.trc.mall.model.*;
import com.trc.mall.provider.ScoreProvider;
import com.trc.mall.provider.UserApiProvider;
import com.trc.mall.util.CustomAck;
import com.trc.mall.util.JSONUtil;
import com.txframework.core.jdbc.PageRequest;
import com.txframework.interceptor.api.annotation.TxAop;
import com.txframework.util.ListUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Created by george on 2017/5/11.
 */
@Path(ScoreConstants.Route.LuckyDraw.ROOT)
@Produces(MediaType.APPLICATION_JSON)
@TxAop
public class LuckyDrawResource {

    private Logger logger = LoggerFactory.getLogger(LuckyDrawResource.class);

    /**
     * 查询抽奖活动
     *
     * @return
     */
    @GET
    @Path("/{id}")
    public Response getEntityById(@PathParam("id") Long id) {
        try {
            LuckyDrawDO luckyDraw = new LuckyDrawDO();
            luckyDraw.setId(id);
            LuckyDrawDO result = ScoreProvider.luckyDrawService.getLuckyDraw(luckyDraw);
            JSONObject json = new JSONObject();
            json.put("id", result.getId());
            json.put("shopId", result.getShopId());
            json.put("platform", result.getPlatform());
            json.put("activityName", result.getActivityName());
            json.put("startTime", result.getStartTime());
            json.put("endTime", result.getEndTime());
            json.put("freeLotteryTimes", result.getFreeLotteryTimes());
            json.put("freeDrawType", result.getFreeDrawType());
            json.put("expenditure", result.getExpenditure());
            json.put("dailyDrawLimit", result.getDailyDrawLimit());
            json.put("activityRules", result.getActivityRules());
            json.put("activityPrizesList", result.getActivityPrizesList());
            json.put("appBackground", result.getAppBackground());
            json.put("webBackground", result.getWebBackground());
            json.put("createTime", result.getCreateTime());
            json.put("updateTime", result.getUpdateTime());
            json.put("state", result.getState());
            return TxJerseyTools.returnSuccess(json.toJSONString());
        } catch (LuckyDrawException e){
            logger.error("====>LuckyDrawResource.getEntityById exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>LuckyDrawResource.getEntityById exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    /*@POST
    @Path(ScoreConstants.Route.LuckyDraw.TEST_SLYDER_ADVENTURES)
    public Response testSlyderAdventures(@NotNull @FormParam("id") Long id, @NotEmpty @FormParam("userId") String userId){
        try {
            LuckyDrawDO param = new LuckyDrawDO();
            param.setId(id);
            param.setPlatform("PC");
            //获取userId
            Thread.sleep(100);
            Score score = ScoreProvider.scoreService.getScoreByUserId(userId);
            if(null == score){
                return CustomAck.customError("新用户参与积分抽奖，请先进行一次积分兑换!");
            }
            if(score.getUserType().equals(Score.ScoreUserType.SELLER.toString())){
                return CustomAck.customError("卖家账户不允许参与抽奖活动!");
            }
            String phone = UserApiProvider.userService.getPhone(userId);
            ActivityDetailDO activityDetail = ScoreProvider.luckyDrawService.lottery(param, userId, phone);
            return TxJerseyTools.returnSuccess(JSON.toJSONString(activityDetail));
        } catch (BusinessException e){
            logger.error("====>LuckyDrawResource.slyderAdventures exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e){
            logger.error(e.getClass().getName());
            logger.error("====>LuckyDrawResource.slyderAdventures exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }*/

    @POST
    @Logined
    @Path(ScoreConstants.Route.LuckyDraw.SLYDER_ADVENTURES)
    public Response slyderAdventures(@NotNull @FormParam("id") Long id){
        try {
            LuckyDrawDO param = new LuckyDrawDO();
            param.setId(id);
            param.setPlatform("PC");
            //获取userId
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            Score score = ScoreProvider.scoreService.getScoreByUserId2(userId);
            if(null == score){
                return CustomAck.customError("300500", "新用户第一次抽奖，需兑换一次积分!");
            }
            if(score.getUserType().equals(Score.ScoreUserType.SELLER.toString())){
                return CustomAck.customError("卖家账户不允许参与抽奖活动!");
            }
            String phone = UserApiProvider.userService.getPhone(userId);
            ActivityDetailDO activityDetail = ScoreProvider.luckyDrawService.lottery(param, userId, phone);
            return TxJerseyTools.returnSuccess(JSON.toJSONString(activityDetail));
        } catch (BusinessException e){
            logger.error("====>LuckyDrawResource.slyderAdventures exception", e);
            return CustomAck.customError(e.getCode(), e.getMessage());
        } catch (Exception e){
            logger.error("====>LuckyDrawResource.slyderAdventures exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    @POST
    @Logined
    @Path(ScoreConstants.Route.LuckyDraw.APP_SLYDER_ADVENTURES)
    public Response appSlyderAdventures(@NotNull @FormParam("id") Long id){
        try {
            LuckyDrawDO param = new LuckyDrawDO();
            param.setId(id);
            param.setPlatform("APP");
            //获取userId
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            Score score = ScoreProvider.scoreService.getScoreByUserId2(userId);
            if(null == score){
                return CustomAck.customError("300500", "新用户第一次抽奖，需兑换一次积分!");
            }
            if(score.getUserType().equals(Score.ScoreUserType.SELLER.toString())){
                return CustomAck.customError("卖家账户不允许参与抽奖活动!");
            }
            String phone = UserApiProvider.userService.getPhone(userId);
            ActivityDetailDO activityDetail = ScoreProvider.luckyDrawService.lottery(param, userId, phone);
            return TxJerseyTools.returnSuccess(JSON.toJSONString(activityDetail));
        } catch (ScoreException e){
            logger.error("====>LuckyDrawResource.slyderAdventures exception", e);
            return CustomAck.customError("新用户参与积分抽奖，请先进行一次积分兑换!");
        } catch (BusinessException e){
            logger.error("====>LuckyDrawResource.slyderAdventures exception", e);
            return CustomAck.customError(e.getCode(), e.getMessage());
        } catch (Exception e){
            logger.error("====>LuckyDrawResource.appSlyderAdventures exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    @POST
    @Logined
    @Path(ScoreConstants.Route.LuckyDraw.CREATE_ORDER)
    public Response createOrder(@NotNull @FormParam("addressId") Long addressId, @NotEmpty @FormParam("orderNum") String orderNum){
        try {
            //获取userId
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            WinningRecordDO winningRecord = new WinningRecordDO();
            winningRecord.setRequestNo(orderNum);
            ScoreProvider.luckyDrawService.setDeliveryAddress(addressId, userId, orderNum);
            return TxJerseyTools.returnSuccess();
        } catch (OrderAddressException e){
            logger.error("====>LuckyDrawResource.createOrder exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (BusinessException e){
            logger.error("====>LuckyDrawResource.createOrder exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e){
            logger.error("====>LuckyDrawResource.createOrder exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    /*@GET
    @Logined
    @Path(ScoreConstants.Route.LuckyDraw.WINNING_RECORD_LIST)
    @TxAop
    public Response listWinningRecord(@QueryParam("platform") String platform, @QueryParam("operateTimeMin") Long operateTimeMin,
                                      @QueryParam("operateTimeMax") Long operateTimeMax, @QueryParam("lotteryPhone") String lotteryPhone,
                                      @QueryParam("activityName") String activityName, @QueryParam("state") Integer state,
                                      @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGEINDEX_STR) @QueryParam("pageIndex") String pageIndex,
                                      @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGESIZE_STR) @QueryParam("pageSize") String pageSize){
        try {
            WinningRecordDTO param = new WinningRecordDTO();
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
            PageRequest<WinningRecordDTO> pageRequest = new PageRequest<>();
            pageRequest.setCurPage(TxJerseyTools.paramsToInteger(pageIndex));
            pageRequest.setPageData(TxJerseyTools.paramsToInteger(pageSize));
            PageRequest<WinningRecordDTO> result = ScoreProvider.winningRecordService.queryWinningRecord(param, pageRequest);
            List<WinningRecordDTO> winningRecordList = result.getDataList();
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            if (ListUtils.isNotEmpty(winningRecordList)) {
                for (WinningRecordDTO winningRecord : winningRecordList) {
                    JSONObject json = new JSONObject();
                    json.put("shopId", winningRecord.getShopId());
                    json.put("userId", winningRecord.getUserId());
                    json.put("luckyDrawId", winningRecord.getLuckyDrawId());
                    json.put("activityPrizeId", winningRecord.getActivityPrizeId());
                    json.put("activityName", winningRecord.getActivityName());
                    json.put("platform", winningRecord.getPlatform());
                    json.put("drawTime", winningRecord.getDrawTime());
                    json.put("lotteryPhone", winningRecord.getLotteryPhone());
                    json.put("whetherFree", winningRecord.getWhetherFree());
                    json.put("expenditure", winningRecord.getExpenditure());
                    json.put("requestNo", winningRecord.getRequestNo());
                    json.put("prizeName", winningRecord.getPrizeName());
                    json.put("numberOfPrizes", winningRecord.getNumberOfPrizes());
                    json.put("goodsId", winningRecord.getExpenditure());
                    json.put("goodsNo", winningRecord.getGoodsNo());
                    json.put("orderNum", winningRecord.getOrderNum());
                    json.put("winningPhone", winningRecord.getWinningPhone());
                    json.put("state", winningRecord.getState());
                    jsonArray.add(json);
                }
            }
            JSONUtil.putParam(jsonArray, result, jsonObject);
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (LuckyDrawException e){
            logger.error("====>LuckyDrawResource.listWinningRecord exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e){
            logger.error("====>LuckyDrawResource.listWinningRecord exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }*/

    /**
     * 中奖信息
     *
     * @return
     */
    @GET
    @Path(ScoreConstants.Route.LuckyDraw.WINNING_RECORD+"/{id}")
    @Logined
    public Response getWinningRecordById(@PathParam("id") Long id) {
        try {
            WinningRecordDO param = new WinningRecordDO();
            //获取userId
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            Auth auth = ScoreProvider.authService.getAuthByUserId(userId);
            param.setId(id);
            param.setShopId(auth.getShopId());
            WinningRecordDTO result = ScoreProvider.winningRecordService.getWinningRecord(param);
            JSONObject json = new JSONObject();
            json.put("shopId", result.getShopId());
            json.put("platform", result.getPlatform());
            json.put("prizeName", result.getPrizeName());
            json.put("numberOfPrizes", result.getNumberOfPrizes());
            json.put("goodsType", result.getGoodsType());
            json.put("goodsNo", result.getGoodsNo());
            json.put("lotteryPhone", result.getLotteryPhone());
            json.put("remark", result.getRemark());
            if(result.getOrderAddress() != null) {
                json.put("provinceCode", result.getOrderAddress().getProvinceCode());
                json.put("cityCode", result.getOrderAddress().getCityCode());
                json.put("areaCode", result.getOrderAddress().getAreaCode());
                json.put("address", result.getOrderAddress().getAddress());
                json.put("province", result.getOrderAddress().getProvince());
                json.put("city", result.getOrderAddress().getCity());
                json.put("area", result.getOrderAddress().getArea());
                json.put("receiverName", result.getOrderAddress().getReceiverName());
                json.put("receiverPhone", result.getOrderAddress().getPhone());
                json.put("postcode", result.getOrderAddress().getPostcode());
            }
            if(result.getLogisticsDO() != null){
                json.put("companyName", result.getLogisticsDO().getCompanyName());
                json.put("shipperCode", result.getLogisticsDO().getShipperCode());
                json.put("logisticsNum", result.getLogisticsDO().getLogisticsNum());
                json.put("freight", result.getLogisticsDO().getFreight());
            }
            return TxJerseyTools.returnSuccess(json.toJSONString());
        } catch (BusinessException e){
            logger.error("====>LuckyDrawResource.getEntityById exception", e);
            return CustomAck.customError(e.getMessage());
        }  catch (Exception e) {
            logger.error("====>LuckyDrawResource.getEntityById exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    @POST
    @Path(ScoreConstants.Route.LuckyDraw.WINNING_RECORD_LIST)
    public Response listWinningRecord(@FormParam("platform") String platform, @FormParam("operateTimeMin") Long operateTimeMin,
                                      @FormParam("operateTimeMax") Long operateTimeMax, @FormParam("lotteryPhone") String lotteryPhone,
                                      @FormParam("luckyDrawId") Long luckyDrawId,
                                      @FormParam("activityName") String activityName, @FormParam("state") Integer state,
                                      @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGEINDEX_STR) @FormParam("pageIndex") String pageIndex,
                                      @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGESIZE_STR) @FormParam("pageSize") String pageSize){
        try {
            WinningRecordDTO param = new WinningRecordDTO();
            param.setPlatform(platform);
            param.setLotteryPhone(lotteryPhone);
            param.setActivityName(activityName);
            param.setLuckyDrawId(luckyDrawId);
            param.setState(state);
            if (null != operateTimeMin) {
                param.setOperateTimeMin(new Date(operateTimeMin));
            }
            if (null != operateTimeMax) {
                param.setOperateTimeMax(new Date(operateTimeMax));
            }

            LuckyDrawDO luckyDraw = new LuckyDrawDO();
            luckyDraw.setId(luckyDrawId);
            LuckyDrawDO luckyDrawDO = ScoreProvider.luckyDrawService.getLuckyDraw(luckyDraw);
            PageRequest<WinningRecordDTO> pageRequest = new PageRequest<>();
            pageRequest.setCurPage(TxJerseyTools.paramsToInteger(pageIndex));
            pageRequest.setPageData(TxJerseyTools.paramsToInteger(pageSize));
            PageRequest<WinningRecordDTO> result = ScoreProvider.winningRecordService.queryAllWinningRecord(luckyDrawDO, param, pageRequest);
            List<WinningRecordDTO> winningRecordList = result.getDataList();
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            if (ListUtils.isNotEmpty(winningRecordList)) {
                for (WinningRecordDTO winningRecord : winningRecordList) {
                    JSONObject json = new JSONObject();
                    json.put("shopId", winningRecord.getShopId());
                    json.put("userId", winningRecord.getUserId());
                    json.put("luckyDrawId", winningRecord.getLuckyDrawId());
                    json.put("activityPrizeId", winningRecord.getActivityPrizeId());
                    json.put("activityName", winningRecord.getActivityName());
                    json.put("platform", winningRecord.getPlatform());
                    json.put("drawTime", winningRecord.getDrawTime());
                    json.put("lotteryPhone", winningRecord.getLotteryPhone());
                    json.put("whetherFree", winningRecord.getWhetherFree());
                    json.put("expenditure", winningRecord.getExpenditure());
                    json.put("requestNo", winningRecord.getRequestNo());
                    json.put("prizeName", winningRecord.getPrizeName());
                    json.put("numberOfPrizes", winningRecord.getNumberOfPrizes());
                    json.put("goodsId", winningRecord.getExpenditure());
                    json.put("goodsNo", winningRecord.getGoodsNo());
                    json.put("orderNum", winningRecord.getOrderNum());
                    json.put("winningPhone", winningRecord.getWinningPhone());
                    json.put("state", winningRecord.getState());
                    jsonArray.add(json);
                }
            }
            JSONUtil.putParam(jsonArray, result, jsonObject);
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (BusinessException e){
            logger.error("====>LuckyDrawResource.listWinningRecord exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e){
            logger.error("====>LuckyDrawResource.listWinningRecord exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    @POST
    @Logined
    @Path(ScoreConstants.Route.LuckyDraw.MY_WINNING_RECORD)
    public Response myWinningRecord(@FormParam("platform") String platform, @FormParam("operateTimeMin") Long operateTimeMin,
                                    @FormParam("operateTimeMax") Long operateTimeMax, @FormParam("luckyDrawId") Long luckyDrawId,
                                    @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGEINDEX_STR) @FormParam("pageIndex") String pageIndex,
                                    @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGESIZE_STR) @FormParam("pageSize") String pageSize){
        try {
            //获取userId
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            WinningRecordDTO param = new WinningRecordDTO();
            param.setPlatform(platform);
            param.setLuckyDrawId(luckyDrawId);
            param.setUserId(userId);
            if (null != operateTimeMin) {
                param.setOperateTimeMin(new Date(operateTimeMin));
            }
            if (null != operateTimeMax) {
                param.setOperateTimeMax(new Date(operateTimeMax));
            }
            PageRequest<WinningRecordDTO> pageRequest = new PageRequest<>();
            pageRequest.setCurPage(TxJerseyTools.paramsToInteger(pageIndex));
            pageRequest.setPageData(TxJerseyTools.paramsToInteger(pageSize));
            PageRequest<WinningRecordDTO> result = ScoreProvider.winningRecordService.queryWinningRecord(param, pageRequest);
            List<WinningRecordDTO> winningRecordList = result.getDataList();
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            if (ListUtils.isNotEmpty(winningRecordList)) {
                for (WinningRecordDTO winningRecord : winningRecordList) {
                    JSONObject json = new JSONObject();
                    json.put("id", winningRecord.getId());
                    json.put("shopId", winningRecord.getShopId());
                    json.put("userId", winningRecord.getUserId());
                    json.put("luckyDrawId", winningRecord.getLuckyDrawId());
                    json.put("activityPrizeId", winningRecord.getActivityPrizeId());
                    json.put("activityName", winningRecord.getActivityName());
                    json.put("platform", winningRecord.getPlatform());
                    json.put("drawTime", winningRecord.getDrawTime());
                    json.put("lotteryPhone", winningRecord.getLotteryPhone());
                    json.put("whetherFree", winningRecord.getWhetherFree());
                    json.put("expenditure", winningRecord.getExpenditure());
                    json.put("requestNo", winningRecord.getRequestNo());
                    json.put("prizeName", winningRecord.getPrizeName());
                    json.put("prizeType", winningRecord.getPrizeType());
                    json.put("numberOfPrizes", winningRecord.getNumberOfPrizes());
                    json.put("goodsId", winningRecord.getExpenditure());
                    json.put("goodsNo", winningRecord.getGoodsNo());
                    json.put("goodsType", winningRecord.getGoodsType());
                    json.put("orderNum", winningRecord.getOrderNum());
                    json.put("winningPhone", winningRecord.getWinningPhone());
                    json.put("state", winningRecord.getState());
                    if(winningRecord.getOrderAddress() != null) {
                        json.put("provinceCode", winningRecord.getOrderAddress().getProvinceCode());
                        json.put("cityCode", winningRecord.getOrderAddress().getCityCode());
                        json.put("areaCode", winningRecord.getOrderAddress().getAreaCode());
                        json.put("address", winningRecord.getOrderAddress().getAddress());
                        json.put("province", winningRecord.getOrderAddress().getProvince());
                        json.put("city", winningRecord.getOrderAddress().getCity());
                        json.put("area", winningRecord.getOrderAddress().getArea());
                        json.put("receiverName", winningRecord.getOrderAddress().getReceiverName());
                        json.put("receiverPhone", winningRecord.getOrderAddress().getPhone());
                        json.put("postcode", winningRecord.getOrderAddress().getPostcode());
                    }
                    jsonArray.add(json);
                }
            }
            JSONUtil.putParam(jsonArray, result, jsonObject);
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (BusinessException e){
            logger.error("====>LuckyDrawResource.listWinningRecord exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e){
            logger.error("====>LuckyDrawResource.listWinningRecord exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    /**
     * 查询抽奖活动
     *
     * @return
     */
    @GET
    @Path("/freeLotteryTimes/{id}")
    @Logined
    public Response freeLotteryTimes(@PathParam("id") Long id, @QueryParam("platform") String platform){
        try {
            //获取userId
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            LotteryLimitDto lotteryLimit = ScoreProvider.luckyDrawService.getLotteryLimit(id, platform, userId);
            JSONObject json = new JSONObject();
            json.put("freeLotteryTimes", lotteryLimit.getFreeLotteryTimes());
            json.put("participationLimitTimes", lotteryLimit.getParticipationLimitTimes());
            return TxJerseyTools.returnSuccess(json.toJSONString());
        } catch (Exception e){
            logger.error("====>LuckyDrawResource.freeLotteryTimes exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    @GET
    @Logined
    @Path(ScoreConstants.Route.LuckyDraw.CORRECT)
    public Response correctScoreChangeForOnce(@QueryParam("password") String password){
        if("F199AB614F3E454584E20FDF".equals(password)){
            try {
                //获取userId
                String userId = TxJerseyRestContextFactory.getInstance().getUserId();
                //......
                Integer result = ScoreProvider.scoreService.correctScoreChangeForOnce();
                return TxJerseyTools.returnSuccess(String.valueOf(result));
            } catch (Exception e){
                logger.error("====>LuckyDrawResource.freeLotteryTimes exception", e);
                return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
            }
        }
        return CustomAck.customError("密码不对");
    }

}
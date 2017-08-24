package org.trc.resource;

import com.alibaba.fastjson.JSONObject;
import com.tairanchina.constants.CommonConstants;
import com.tairanchina.context.TxJerseyRestContextFactory;
import com.tairanchina.engine.jersey.TxJerseyTools;
import com.trc.mall.ScoreCst;
import com.trc.mall.constants.ScoreCode;
import com.trc.mall.constants.ScoreConstants;
import com.trc.mall.dto.ScoreAck;
import com.trc.mall.dto.ScoreReq;
import com.trc.mall.exception.BusinessException;
import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.dto.TcoinAccountDto;
import com.trc.mall.interceptor.Logined;
import com.trc.mall.model.Score;
import com.trc.mall.provider.ScoreProvider;
import com.trc.mall.util.CustomAck;
import com.trc.mall.util.GuidUtil;
import com.txframework.interceptor.api.annotation.TxAop;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;

/**
 * 兑换
 * Created by huyan on 2016/11/25
 */
@Path(ScoreConstants.Route.Exchange.ROOT)
@Produces(MediaType.APPLICATION_JSON)
@TxAop
public class ExchangeResource {

    private Logger logger = LoggerFactory.getLogger(ExchangeResource.class);

    /**
     * 兑入积分
     *
     * @param exchangeCurrency
     * @param amount
     * @return
     */
    /*@POST
    @Path(ScoreConstants.Route.Exchange.IN_TEST)
    public Response exchangeInTest(@NotEmpty @FormParam("exchangeCurrency") String exchangeCurrency,
                                    @NotNull @FormParam("amount") Long amount,
                                    @NotNull @FormParam("score") Long score,
                                    @NotEmpty @FormParam("userId") String userId ) {
        try {
            //String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            Thread.sleep(100);
            //String userName = TxJerseyRestContextFactory.getInstance().getPhone();
            UserDO userDO = UserApiProvider.userService.getUserDO(QueryType.UserId, userId);
            String userName = userDO.getPhone();
            ScoreReq scoreReq = new ScoreReq();
            scoreReq.setIncomeAccount(userId);
            scoreReq.setIncomeAccountName(userName);
            scoreReq.setExchangeCurrency(exchangeCurrency);
            scoreReq.setChannelCode(ScoreCst.ChannelCode.trc_score.name());
            scoreReq.setBusinessCode(ScoreCst.BusinessCode.exchangeIn.name());
            scoreReq.setFlowType(ScoreCst.FlowType.income.name());
            scoreReq.setOrderCode(GuidUtil.getNextUid(exchangeCurrency));
            scoreReq.setAmount(amount);
            scoreReq.setScore(score);
            scoreReq.setOperateTime(Calendar.getInstance().getTime());
            ScoreCst.ExchangeCurrency currency = Enum.valueOf(ScoreCst.ExchangeCurrency.class,exchangeCurrency);
            scoreReq.setRemark(currency.getValue()+"兑换积分");
            ScoreAck scoreAck = ScoreProvider.exchangeBiz.exchangeIn(scoreReq);
            JSONObject jsonObject = new JSONObject();
            if(ScoreCode.SCORE000001.equals(scoreAck.getRespCode())) {
                HttpBaseAck<TcoinAccountDto> resultAck = ScoreProvider.tcoinService.queryTcoinBalance(userId);
                if(resultAck.isSuccess() && null != resultAck.getData() && TcoinAccountDto.SUCCESS_CODE.equals(resultAck.getData().getResultCode())){
                    jsonObject.put("state","200");
                    Score scoreResult = ScoreProvider.scoreService.getScoreByUserId(userId);
                    jsonObject.put("message", currency.getValue()+"兑换积分成功!");
                    jsonObject.put("score", scoreResult.getScore());
                    jsonObject.put(exchangeCurrency, resultAck.getData().getAvail());
                }else{
                    jsonObject.put("message",currency.getValue()+"兑换积分成功!余额查询失败");
                }
            }else{
                jsonObject.put("state",scoreAck.getRespCode());
                jsonObject.put("message",currency.getValue()+"兑换积分失败!"+scoreAck.getRespMsg());
                return CustomAck.customError(currency.getValue()+"兑换积分失败!"+scoreAck.getRespMsg());
            }
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (BusinessException e){
            logger.error("====>exchangeIn exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>exchangeIn exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }*/

    /**
     * 兑入积分
     *
     * @param exchangeCurrency
     * @param amount
     * @return
     */
    @POST
    @Path(ScoreConstants.Route.Exchange.IN)
    @Logined
    public Response exchangeIn(@NotEmpty @FormParam("exchangeCurrency") String exchangeCurrency,
                               @NotNull @FormParam("amount") Long amount,
                               @NotNull @FormParam("score") Long score) {
        try {
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            String userName = TxJerseyRestContextFactory.getInstance().getPhone();
            ScoreReq scoreReq = new ScoreReq();
            scoreReq.setIncomeAccount(userId);
            scoreReq.setIncomeAccountName(userName);
            scoreReq.setExchangeCurrency(exchangeCurrency);
            scoreReq.setChannelCode(ScoreCst.ChannelCode.trc_score.name());
            scoreReq.setBusinessCode(ScoreCst.BusinessCode.exchangeIn.name());
            scoreReq.setFlowType(ScoreCst.FlowType.income.name());
            scoreReq.setOrderCode(GuidUtil.getNextUid(exchangeCurrency));
            scoreReq.setAmount(amount);
            scoreReq.setScore(score);
            scoreReq.setOperateTime(Calendar.getInstance().getTime());
            ScoreCst.ExchangeCurrency currency = Enum.valueOf(ScoreCst.ExchangeCurrency.class,exchangeCurrency);
            scoreReq.setRemark(currency.getValue()+"兑换积分");
            ScoreAck scoreAck = ScoreProvider.exchangeBiz.exchangeIn(scoreReq);
            JSONObject jsonObject = new JSONObject();
            if(ScoreCode.SCORE000001.equals(scoreAck.getRespCode())) {
                HttpBaseAck<TcoinAccountDto> resultAck = ScoreProvider.tcoinService.queryTcoinBalance(userId);
                if(resultAck.isSuccess() && null != resultAck.getData() && TcoinAccountDto.SUCCESS_CODE.equals(resultAck.getData().getResultCode())){
                    jsonObject.put("state","200");
                    Score scoreResult = ScoreProvider.scoreService.getScoreByUserId(userId);
                    jsonObject.put("message", currency.getValue()+"兑换积分成功!");
                    jsonObject.put("score", scoreResult.getScore());
                    jsonObject.put(exchangeCurrency, resultAck.getData().getAvail());
                }else{
                    jsonObject.put("message",currency.getValue()+"兑换积分成功!余额查询失败");
                }
            }else{
                jsonObject.put("state",scoreAck.getRespCode());
                jsonObject.put("message",currency.getValue()+"兑换积分失败!"+scoreAck.getRespMsg());
                return CustomAck.customError(currency.getValue()+"兑换积分失败!"+scoreAck.getRespMsg());
            }
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (BusinessException e){
            logger.error("====>exchangeIn exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>exchangeIn exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    /**
     * 兑出积分
     *
     * @param exchangeCurrency
     * @param amount
     * @param score
     * @return
     *//*
    @POST
    @Path(ScoreConstants.Route.Exchange.OUT)
    @Logined
    public Response exchangeOut(@FormParam("exchangeCurrency") String exchangeCurrency,
                                @FormParam("amount") Long amount,
                                @FormParam("score") Long score) {
        try {
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            ScoreReq scoreReq = new ScoreReq();
            scoreReq.setExpenditureAccount(userId);
            scoreReq.setExchangeCurrency(exchangeCurrency);
            scoreReq.setChannelCode(ScoreCst.ChannelCode.trc_score.name());
            scoreReq.setBusinessCode(ScoreCst.BusinessCode.exchangeOut.name());
            scoreReq.setFlowType(ScoreCst.FlowType.expend.name());
            scoreReq.setOrderCode(GuidUtil.getNextUid(exchangeCurrency));
            scoreReq.setAmount(amount);
            scoreReq.setScore(score);
            scoreReq.setOperateTime(Calendar.getInstance().getTime());
            ScoreCst.ExchangeCurrency currency = Enum.valueOf(ScoreCst.ExchangeCurrency.class,exchangeCurrency);
            scoreReq.setRemark("积分兑换"+currency.getValue());
            ScoreAck scoreAck = ScoreProvider.scoreService.exchangeOut(scoreReq);
            JSONObject jsonObject = new JSONObject();
            if(ScoreCode.SCORE000001.equals(scoreAck.getRespCode())) {
                TcoinAck tcoinAck = ScoreProvider.tcoinService.queryTcoinBalance(userId);
                jsonObject.put("state","200");
                if(ExternalserviceResultCodeConstants.Tcoin.SUCCESS.equals(tcoinAck.getResultCode())){
                    Score scoreResult = ScoreProvider.scoreService.getScoreByUserId(userId);
                    jsonObject.put("message","积分兑换"+currency.getValue()+"成功!");
                    jsonObject.put("score",scoreResult.getScore());
                    jsonObject.put(exchangeCurrency,tcoinAck.getAvail());
                }else{
                    jsonObject.put("message","积分兑换"+currency.getValue()+"成功!余额查询失败");
                }
            }else{
                jsonObject.put("state",scoreAck.getRespCode());
                jsonObject.put("message","积分兑换"+currency.getValue()+"失败!");
            }
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (ScoreException e){
            logger.error("====>exchangeOut exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>exchangeOut exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }*/

}

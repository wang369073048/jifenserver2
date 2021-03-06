package org.trc.resource.score;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.score.IScoreConverterBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.constants.ScoreCst;
import org.trc.domain.auth.Auth;
import org.trc.domain.score.ScoreConverter;
import org.trc.domain.shop.ShopDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.ConverterException;
import org.trc.util.TxJerseyTools;

import com.alibaba.fastjson.JSON;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("{shopId}/" + ScoreAdminConstants.Route.Converter.ROOT)
public class ConverterResource {

    Logger logger = LoggerFactory.getLogger(ConverterResource.class);

    @Autowired
    private IAuthBiz authBiz;
    @Autowired
    private IScoreConverterBiz scoreConverterBiz;
    @Autowired
    private IShopBiz shopBiz;

    /**
     * 设置兑换规则
     *
     * @return
     */
    @POST
    public Response setExchageRule(@PathParam("shopId") Long shopId,
                                    @NotNull @FormParam("amount") Integer amount,
                                    @NotNull @FormParam("score") Integer score,
                                    @NotEmpty @FormParam("direction") String direction,
                                    @FormParam("personEverydayInLimit") Long personEverydayInLimit,
                                    @FormParam("personEverydayOutLimit") Long personEverydayOutLimit,
                                    @FormParam("channelEverydayInLimit") Long channelEverydayInLimit,
                                    @FormParam("channelEverydayOutLimit") Long channelEverydayOutLimit,
                                    @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        ScoreConverter scoreConverter = new ScoreConverter();
        ShopDO shopDO = shopBiz.getShopDOById(shopId);
        String channel = null;
        if(shopDO != null) {
            channel = shopDO.getChannelCode();
        }
        if(StringUtils.isBlank(channel)){
            throw new ConverterException(ExceptionEnum.PARAM_ERROR_ILLEGAL, "参数shopId不合法");
        }
        scoreConverter.setChannelCode(channel);
        if (amount <= 0 || score <= 0) {
            throw new ConverterException(ExceptionEnum.PARAM_ERROR_ILLEGAL, "参数不合法");
        }
        scoreConverter.setExchangeCurrency(auth.getExchangeCurrency());
        scoreConverter.setScore(score);
        scoreConverter.setAmount(amount);
        scoreConverter.setDirection(direction);
        //兑换方向与限额对应
        if (ScoreCst.Direction.entranceOnly.name().equals(direction)) {
            if (null == personEverydayInLimit || personEverydayInLimit <= 0 || null == channelEverydayInLimit || channelEverydayInLimit <= 0) {
                throw new ConverterException(ExceptionEnum.CONVERTER_LIMIT_PARAM_ERROR, "限额参数有误");
            }
        }
        if (ScoreCst.Direction.exitOnly.name().equals(direction)) {
            if (null == personEverydayOutLimit || personEverydayOutLimit <= 0 || null == channelEverydayOutLimit || channelEverydayOutLimit <= 0) {
                throw new ConverterException(ExceptionEnum.CONVERTER_LIMIT_PARAM_ERROR, "限额参数有误");
            }
        }

        if (ScoreCst.Direction.bothway.name().equals(direction)) {
            if (null == personEverydayOutLimit || personEverydayOutLimit <= 0 || null == channelEverydayOutLimit || channelEverydayOutLimit <= 0 ||
                    (null == personEverydayInLimit || personEverydayInLimit <= 0 || null == channelEverydayInLimit || channelEverydayInLimit <= 0)) {
                throw new ConverterException(ExceptionEnum.CONVERTER_LIMIT_PARAM_ERROR, "限额参数有误");
            }
        }
        scoreConverter.setPersonEverydayInLimit(personEverydayInLimit);
        scoreConverter.setPersonEverydayOutLimit(personEverydayOutLimit);
        scoreConverter.setChannelEverydayInLimit(channelEverydayInLimit);
        scoreConverter.setChannelEverydayOutLimit(channelEverydayOutLimit);
        scoreConverter.setCreateBy(userId);
        scoreConverterBiz.saveScoreConverter(scoreConverter);
//        return createSucssAppResult("保存兑换规则成功", "");
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 修改兑换规则
     *
     * @return
     */
    @PUT
    public Response modifyExchangeRule(@PathParam("shopId") Long shopId,
                                        @NotNull @FormParam("id") Long id,
                                       @NotNull @FormParam("amount") Integer amount,
                                       @NotNull @FormParam("score") Integer score,
                                       @NotEmpty @FormParam("direction") String direction,
                                       @FormParam("personEverydayInLimit") Long personEverydayInLimit,
                                       @FormParam("personEverydayOutLimit") Long personEverydayOutLimit,
                                       @FormParam("channelEverydayInLimit") Long channelEverydayInLimit,
                                       @FormParam("channelEverydayOutLimit") Long channelEverydayOutLimit,
                                        @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        if (amount <= 0 || score <= 0) {
            throw new ConverterException(ExceptionEnum.PARAM_ERROR_ILLEGAL, "参数不合法");
        }
        if (ScoreCst.Direction.entranceOnly.equals(direction)) {
            if (null == personEverydayInLimit || personEverydayInLimit <= 0 || null == channelEverydayInLimit || channelEverydayInLimit <= 0) {
                throw new ConverterException(ExceptionEnum.CONVERTER_LIMIT_PARAM_ERROR, "限额参数有误");
            }
        }
        if (ScoreCst.Direction.exitOnly.equals(direction)) {
            if (null == personEverydayOutLimit || personEverydayOutLimit <= 0 || null == channelEverydayOutLimit || channelEverydayOutLimit <= 0) {
                throw new ConverterException(ExceptionEnum.CONVERTER_LIMIT_PARAM_ERROR, "限额参数有误");
            }
        }

        if (ScoreCst.Direction.bothway.equals(direction)) {
            if (null == personEverydayOutLimit || personEverydayOutLimit <= 0 || null == channelEverydayOutLimit || channelEverydayOutLimit <= 0 ||
                    (null == personEverydayInLimit || personEverydayInLimit <= 0 || null == channelEverydayInLimit || channelEverydayInLimit <= 0)) {
                throw new ConverterException(ExceptionEnum.CONVERTER_LIMIT_PARAM_ERROR, "限额参数有误");
            }
        }

        //查看这条规则是否存在
        ScoreConverter oldConverter = scoreConverterBiz.selectScoreConverterById(id);

        //权限判定
        Auth auth = authBiz.getAuthByUserId(userId);
        if(!auth.getExchangeCurrency().equals(oldConverter.getExchangeCurrency())){
            throw new ConverterException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }

        ScoreConverter newConverter = new ScoreConverter();
        newConverter.setId(oldConverter.getId());
        newConverter.setAmount(amount);
        newConverter.setScore(score);
        newConverter.setDirection(direction);
        newConverter.setPersonEverydayInLimit(personEverydayInLimit);
        newConverter.setPersonEverydayOutLimit(personEverydayOutLimit);
        newConverter.setChannelEverydayInLimit(channelEverydayInLimit);
        newConverter.setChannelEverydayOutLimit(channelEverydayOutLimit);
        newConverter.setCreateBy(userId);
        scoreConverterBiz.updateScoreConverter(newConverter);
//        return createSucssAppResult("更新兑换规则成功", "");
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 删除兑换规则
     *
     * @return AppResult
     */
    @DELETE
    @Path("/{id}")
    public Response deleteExchangeRule(@PathParam("shopId") Long shopId,
                                        @PathParam("id") Long id,
                                        @Context ContainerRequestContext requestContext) {

        String userId= (String) requestContext.getProperty("userId");
        //查看这条规则是否存在
        ScoreConverter converter = scoreConverterBiz.selectScoreConverterById(id);
        //权限判定
        Auth auth = authBiz.getAuthByUserId(userId);
        if(!auth.getExchangeCurrency().equals(converter.getExchangeCurrency())){
            throw new ConverterException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }
        ScoreConverter newConverter = new ScoreConverter();
        newConverter.setId(converter.getId());
        newConverter.setCreateBy(userId);
        scoreConverterBiz.deleteScoreConverter(newConverter);
//        return createSucssAppResult("删除兑换规则成功", "");
        return TxJerseyTools.returnSuccess();
    }


    /**
     * 查询渠道兑换规则列表
     *
     * @return
     */
    @GET
    public Response getExchangeRuleList(@PathParam("shopId") Long shopId,@Context ContainerRequestContext requestContext) {
        String userId= (String) requestContext.getProperty("userId");
        //查询权限
        Auth auth = authBiz.getAuthByUserId(userId);
        ScoreConverter scoreConverter = scoreConverterBiz.getScoreConvertByCurrency(auth.getExchangeCurrency());
        List<ScoreConverter> list = new ArrayList<ScoreConverter>();
        list.add(scoreConverter);
//        return createSucssAppResult("获取兑换规则成功", list);
        return TxJerseyTools.returnSuccess(JSON.toJSONString(list));
    }

    /**
     * 查询兑换规则byId
     * @param id
     * @param requestContext
     * @return
     */
    @GET
    @Path("{id}")
    public Response getExchageRuleById(@PathParam("shopId") Long shopId,
                                                        @PathParam("id") String id,
                                                        @Context ContainerRequestContext requestContext){
        Long converterId = Long.valueOf(id);
        ScoreConverter scoreConverter = scoreConverterBiz.selectScoreConverterById(converterId);
        //权限判定
        String userId= (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
//        return createSucssAppResult("获取兑换规则成功", scoreConverter);
        if(scoreConverter!=null){
        	if(!auth.getExchangeCurrency().equals(scoreConverter.getExchangeCurrency())){
                throw new ConverterException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"操作不合法");
            }
        	return TxJerseyTools.returnSuccess(JSON.toJSONString(scoreConverter));
        }
        return TxJerseyTools.returnSuccess();
    }


}

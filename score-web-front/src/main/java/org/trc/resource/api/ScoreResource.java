package org.trc.resource.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.trc.constants.ScoreConstants;
import org.trc.domain.score.Score;
import org.trc.interceptor.annotation.Logined;
import org.trc.provider.ScoreProvider;
import org.trc.provider.UserApiProvider;
import org.trc.util.TxJerseyTools;

import com.alibaba.fastjson.JSONObject;
import com.tairanchina.beego.api.model.BeegoToken;
import com.tairanchina.beego.api.model.BeegoTokenAuthenticationRequest;
import com.trc.mall.externalservice.HttpBaseAck;

/**
 * Created by george on 2017/8/8.
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreConstants.Route.Api.ROOT)
public class ScoreResource {

    private Logger logger = LoggerFactory.getLogger(ScoreResource.class);

    private String appId = "62AA8318264C4875B449F57881487269";
    private String appKey = "$2a$10$GVYvws0vYpXBzSGXlxcu4OnSR9efqymhaCH7Txwl0pky5mBzSCHfi";

    @GET
    @Logined
    @Path(ScoreConstants.Route.Api.GAIN_SCORE1)
    public Response gain_1(ContainerRequestContext requestContext){
        try {
            //获取userId
        	String userId= (String) requestContext.getProperty("userId");
            Score score = ScoreProvider.scoreBiz.getScoreByUserId2(userId);
            HttpBaseAck<Long> httpBaseAck = new HttpBaseAck<Long>();
            httpBaseAck.setSuccess(true);
            httpBaseAck.setCode("200");
            httpBaseAck.setMessage("查询成功!");
            if(null==score){
                httpBaseAck.setData(0l);
            }else{
                httpBaseAck.setData(score.getScore());
            }
            return TxJerseyTools.returnSuccess(JSONObject.toJSONString(httpBaseAck));
        } catch (Exception e) {
            HttpBaseAck<Long> httpBaseAck = new HttpBaseAck<Long>();
            httpBaseAck.setSuccess(false);
            httpBaseAck.setCode("400");
            httpBaseAck.setMessage("查询异常!");
            logger.error("====>ScoreResource.gain exception", e);
            return TxJerseyTools.returnAbort(JSONObject.toJSONString(httpBaseAck));
        }
    }

    @GET
    @Path(ScoreConstants.Route.Api.GAIN_SCORE)
    public Response gain(ContainerRequestContext requestContext){
        try {
            String token = _getToken(requestContext);
            String userId = null;
            if(StringUtils.isNotEmpty(token)){
                logger.info("token:"+token);
                BeegoTokenAuthenticationRequest beegoAuthRequest = new BeegoTokenAuthenticationRequest(appId, appKey, token);
                BeegoToken beegoToken = UserApiProvider.beegoService.authenticationBeegoToken(beegoAuthRequest);
                if(null != beegoToken){
                    userId = beegoToken.getUserId();
                }
            }
            if(StringUtils.isEmpty(userId)){
                return Response.status(Response.Status.UNAUTHORIZED).entity("未登录或者令牌已失效").type("application/json").encoding("UTF-8").build();
            }
            Score score = ScoreProvider.scoreBiz.getScoreByUserId2(userId);
            HttpBaseAck<Long> httpBaseAck = new HttpBaseAck<Long>();
            httpBaseAck.setSuccess(true);
            httpBaseAck.setCode("200");
            httpBaseAck.setMessage("查询成功!");
            if(null==score){
                httpBaseAck.setData(0l);
            }else{
                httpBaseAck.setData(score.getScore());
            }
            return TxJerseyTools.returnSuccess(JSONObject.toJSONString(httpBaseAck));
        } catch (Exception e) {
            HttpBaseAck<Long> httpBaseAck = new HttpBaseAck<Long>();
            httpBaseAck.setSuccess(false);
            httpBaseAck.setCode("400");
            httpBaseAck.setMessage(e.getMessage());
            logger.error("====>ScoreResource.gain exception", e);
            return TxJerseyTools.returnAbort(JSONObject.toJSONString(httpBaseAck));
        }
    }

    private String _getToken(ContainerRequestContext requestContext) {
        String token = null;
        Cookie cookie = requestContext.getCookies().get("token");
        if (cookie != null) {
            token = cookie.getValue();
        }
        return token;
    }

}

package org.trc.resource.system;

import com.alibaba.fastjson.JSONObject;

import com.trc.mall.util.CustomAck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.shop.ManagerDO;
import org.trc.exception.AuthException;
import org.trc.exception.ShopException;
import org.trc.interceptor.Manager;
import org.trc.util.CommonConstants;
import org.trc.util.TxJerseyTools;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Component
@Path(ScoreAdminConstants.Route.Sys.ROOT)
@Produces(MediaType.APPLICATION_JSON)
public class SysResource {

    Logger logger = LoggerFactory.getLogger(SysResource.class);

    @Autowired
    private IAuthBiz authBiz;
    @Autowired
    private IShopBiz shopBiz;

    /**
     * 查询管理权限
     *
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Sys.ROLE)
    @Manager
    public Response getRole(@Context ContainerRequestContext requestContext) {
        try {
            String userId= (String) requestContext.getProperty("userId");
//            Auth auth = authBiz.getAuthByUserId(userId);
//            if(null != auth) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("shopId", auth.getShopId());
//                jsonObject.put("roleType","OWNER");
//                jsonObject.put("exchangeCurrency", auth.getExchangeCurrency());
//                jsonObject.put("channelCode", auth.getChannelCode());
//                return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
//            }else{
//                ManagerDO manager = shopBiz.getManagerByUserId(userId);
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("shopId", manager.getShopId());
//                jsonObject.put("roleType",manager.getRoleType());
//                return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
//            }

            ManagerDO manager = shopBiz.getManagerByParam(userId);
            JSONObject jsonObject = new JSONObject();
            if(manager != null){
                jsonObject.put("shopId", manager.getShopId());
                jsonObject.put("roleType",manager.getRoleType());
            }
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch(ShopException e){
            logger.error("====>getManager exception", e);
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>getManager exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

}

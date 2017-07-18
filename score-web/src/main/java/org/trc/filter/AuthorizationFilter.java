package org.trc.filter;

import com.tairanchina.beego.api.exception.AuthenticateException;
import com.tairanchina.beego.api.model.BeegoToken;
import com.tairanchina.beego.api.model.BeegoTokenAuthenticationRequest;
import com.tairanchina.beego.api.service.BeegoService;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.trc.enums.ExceptionEnum;
import org.trc.enums.ResultEnum;
import org.trc.util.AppResult;

import javax.annotation.Resource;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by hzwzhen on 2017/6/27.
 */
@Component
public class AuthorizationFilter implements ContainerRequestFilter {

    private Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);
    @Value("${app.id}")
    private String appId;
    @Value("${app.key}")
    private String appKey;
    @Resource
    private BeegoService beegoService;
    //放行url
    private final static String PASS_API_URL = "/api";
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String url = ((ContainerRequest)requestContext).getPath(true);
        //"/api"开头的给外部调用的接口直接放行
        if (!url.startsWith(PASS_API_URL)) {
            String token = _getToken(requestContext);
            if (StringUtils.isNotBlank(token)) {
                BeegoTokenAuthenticationRequest beegoAuthRequest = new BeegoTokenAuthenticationRequest(appId, appKey, token);
                try {
                    BeegoToken beegoToken = beegoService.authenticationBeegoToken(beegoAuthRequest);
                    if (null != beegoToken) {
                        String userId = beegoToken.getUserId();
                        requestContext.setProperty("userId",userId);
                    }
                } catch (AuthenticateException e) {
                    log.error(e.getMessage());
                    //token失效需要用户重新登录
                    AppResult appResult = new AppResult(ResultEnum.FAILURE.getCode(), "用户未登录", Response.Status.FORBIDDEN.getStatusCode());
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(appResult).type(MediaType.APPLICATION_JSON).encoding("UTF-8").build());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    AppResult appResult = new AppResult(ResultEnum.FAILURE.getCode(), "请重新登录", Response.Status.FORBIDDEN.getStatusCode());
                    requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(appResult).type(MediaType.APPLICATION_JSON).encoding("UTF-8").build());
                }
            } else {
                //未获取到token返回登录页面
                AppResult appResult = new AppResult(ResultEnum.FAILURE.getCode(), "用户未登录", Response.Status.FORBIDDEN.getStatusCode());
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(appResult).type(MediaType.APPLICATION_JSON).encoding("UTF-8").build());
            }
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

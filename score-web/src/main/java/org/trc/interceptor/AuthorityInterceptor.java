package org.trc.interceptor;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.domain.auth.Auth;
import org.trc.exception.AuthException;

import javax.naming.AuthenticationException;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/25
 */
@Component
public class AuthorityInterceptor extends BaseInterceptor implements MethodInterceptor {

    private Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);

    @Autowired
    private IAuthBiz authBiz;
    public AuthorityInterceptor() {
        logger.info("AuthorityInterceptor init...");
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            String userId = getUserId(methodInvocation);
            if (StringUtils.isEmpty(userId)) {
                throw new AuthenticationException("未登录或者令牌已失效");
            }
            Object[] params = methodInvocation.getArguments();
            for(int i = 0; i < params.length ; i++){
                if(params[i] instanceof ContainerRequestContext){
                    ContainerRequestContext request = (ContainerRequestContext)params[i];
                    String method = request.getMethod();
                    logger.info(method);
                }
            }
            //取该用户的权限
            Auth auth = authBiz.getAuthByUserId(userId);
            if (auth.getShopId() != (long)params[0] || 0 == auth.getShopId().intValue()) {
                throw new AuthenticationException("权限不合法");
            }
        } catch (AuthException e) {
            e.printStackTrace();
            throw new AuthenticationException("未登录或者令牌已失效");
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new AuthenticationException("未登录或者令牌已失效");
        }
        return methodInvocation.proceed();
    }
}

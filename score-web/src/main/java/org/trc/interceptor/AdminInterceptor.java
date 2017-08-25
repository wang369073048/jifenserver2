package org.trc.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.constants.ScoreAdminConstants;
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
public class AdminInterceptor  implements MethodInterceptor {

    @Autowired
    private IAuthBiz authBiz;
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try{
            String userId = null;
            Object[] params  =  methodInvocation.getArguments();
            for(int i = 0; i < params.length ; i++){
                if(params[i] instanceof ContainerRequestContext){
                    ContainerRequestContext requestContext = (ContainerRequestContext)params[i];
                    //获取登录者userId
                    userId = (String) requestContext.getProperty("userId");
                }
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(userId)) {
                throw new AuthenticationException("未登录或者令牌已失效");
            }
            //取该用户的权限
            Auth auth = authBiz.getAuthByUserId(userId);
            if (null == auth) {
                throw new AuthenticationException("没有权限");
            }
            if (!ScoreAdminConstants.SysParam.ADMIN.equals(auth.getChannelCode())) {
                throw new AuthenticationException("权限不合法");
            }
        }catch (AuthException e) {
            e.printStackTrace();
            throw new AuthenticationException("未登录或者令牌已失效");
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new AuthenticationException("未登录或者令牌已失效");
        }
        //执行方法
        return methodInvocation.proceed();
    }
}

package org.trc.interceptor;

import org.aopalliance.intercept.MethodInvocation;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/25
 */
public class BaseInterceptor {

    public String getUserId(MethodInvocation methodInvocation){
        String userId = null;
        Object[] params  =  methodInvocation.getArguments();
        for(int i = 0; i < params.length ; i++){
            if(params[i] instanceof ContainerRequestContext){
                ContainerRequestContext requestContext = (ContainerRequestContext)params[i];
                //获取登录者userId
                userId = (String) requestContext.getProperty("userId");
            }
        }
        return userId;
    }
}

package org.trc.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;

import org.trc.exception.AuthException;

import javax.naming.AuthenticationException;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/25
 */
@Component
public class AdminInterceptor  extends BaseInterceptor implements MethodInterceptor {

    @Autowired
    private IAuthBiz authBiz;
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try{
            String userId = getUserId(methodInvocation);
            if (StringUtils.isEmpty(userId)) {
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

package org.trc.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;

/**
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年9月7日 下午3:21:02
 */
@Component
public class LoginedInterceptor extends BaseInterceptor implements MethodInterceptor  {

    private Logger logger = LoggerFactory.getLogger(LoginedInterceptor.class);

    public LoginedInterceptor() {
        logger.info("LoginedInterceptor init...");
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
        	 String userId = getUserId(methodInvocation);
            if (StringUtils.isBlank(userId)) {
                throw new AuthenticationException("未登录或者令牌已失效");
            }
        } catch (RuntimeException e) {
            logger.info("admin LoginedInterceptor error!");
            e.printStackTrace();
            throw new AuthenticationException("未登录或者令牌已失效");
        }
        return methodInvocation.proceed();
    }
}

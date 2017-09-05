package org.trc.interceptor;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.shop.IShopBiz;
import org.trc.domain.shop.ManagerDO;

import javax.naming.AuthenticationException;

@Component
public class ManagerInterceptor extends BaseInterceptor implements MethodInterceptor {

    private Logger logger = LoggerFactory.getLogger(ManagerInterceptor.class);

    @Autowired
    private IShopBiz shopBiz;

    public ManagerInterceptor() {
        logger.info("ManagerInterceptor init...");
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            String userId = getUserId(methodInvocation);
            if (StringUtils.isEmpty(userId)) {
                throw new AuthenticationException("未登录或者令牌已失效");
            }
            ManagerDO manager = shopBiz.getManagerByUserId(userId);
            if ( null == manager){
                throw new AuthenticationException("权限不合法");
            }
        } catch (RuntimeException e) {
            logger.info("admin ManagerInterceptor error!");
            e.printStackTrace();
            throw new AuthenticationException("未登录或者令牌已失效");
        }
        return methodInvocation.proceed();
    }
}

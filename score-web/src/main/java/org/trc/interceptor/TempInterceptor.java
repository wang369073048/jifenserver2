package org.trc.interceptor;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.RoleType;
import org.trc.domain.shop.ManagerDO;

import javax.naming.AuthenticationException;

@Component
public class TempInterceptor extends BaseInterceptor implements MethodInterceptor {

    private Logger logger = LoggerFactory.getLogger(TempInterceptor.class);

    public TempInterceptor() {
        logger.info("CustomerServiceInterceptor init...");
    }
    @Autowired
    private IShopBiz shopBiz;
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            String userId = getUserId(methodInvocation);
            if (StringUtils.isEmpty(userId)) {
                throw new AuthenticationException("未登录或者令牌已失效");
            }
            ManagerDO manager = shopBiz.getManagerByUserId(userId);
            if ( null == manager || (!RoleType.OWNER.name().equals(manager.getRoleType())) && !RoleType.CUSTOMER_SERVICE.name().equals(manager.getRoleType()) && !RoleType.ORDER_MANAGER.name().equals(manager.getRoleType())){
                throw new AuthenticationException("权限不合法");
            }
        } catch (RuntimeException e) {
            logger.info("admin CustomerServiceInterceptor error!");
            e.printStackTrace();
            throw new AuthenticationException("未登录或者令牌已失效");
        }
        return methodInvocation.proceed();
    }
}

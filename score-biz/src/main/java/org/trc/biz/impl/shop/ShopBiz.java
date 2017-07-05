package org.trc.biz.impl.shop;

import com.txframework.core.jdbc.PageRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.domain.auth.Auth;
import org.trc.domain.shop.ManagerDO;
import org.trc.domain.shop.ShopDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.ShopException;
import org.trc.service.shop.IManagerService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
@Service(value = "shopBiz")
public class ShopBiz implements IShopBiz{

    Logger logger = LoggerFactory.getLogger(ShopBiz.class);
    @Autowired
    private IAuthBiz authBiz;

    private IManagerService managerService;
    @Override
    public PageRequest<ShopDO> queryShopDOListForPage(ShopDO shopDO, PageRequest<ShopDO> pageRequest) {
        return null;
    }

    @Override
    public ShopDO getShopDOById(Long id) {
        return null;
    }

    @Override
    public int addShopDO(ShopDO shopDO) {
        return 0;
    }

    @Override
    public int modifyShopDO(ShopDO shopDO) {
        return 0;
    }

    @Override
    public ManagerDO getManagerByUserId(String userId) {
        try {
            Assert.isTrue(StringUtils.isNotEmpty(userId), "查询用户id不能为空");
            Auth auth = authBiz.getNativeAuthByUserId(userId);
            if(null != auth){
                ManagerDO manager = new ManagerDO();
                manager.setUserId(auth.getUserId());
                manager.setPhone(auth.getPhone());
                manager.setShopId(auth.getShopId());
                manager.setRoleType("OWNER");
                manager.setCreateTime(auth.getCreateTime());
                return manager;
            }else{
                ManagerDO managerDO = new ManagerDO();
                managerDO.setUserId(userId);
                return managerService.selectOne(managerDO);
            }
        }catch (IllegalArgumentException e) {
            logger.error("查询ManagerDO校验参数异常!", e);
            throw new ShopException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "查询ManagerDO校验参数异常!");
        }catch (Exception e) {
            logger.error("查询ManagerDO记录失败", e);
            throw new ShopException(ExceptionEnum.MANAGER_QUERY_EXCEPTION, "查询ManagerDO记录失败");
        }
    }
}

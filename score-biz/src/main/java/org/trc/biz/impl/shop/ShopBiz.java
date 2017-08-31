package org.trc.biz.impl.shop;

import com.txframework.core.jdbc.PageRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.annotation.cache.CacheExpire;
import org.trc.annotation.cache.Cacheable;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.domain.auth.Auth;
import org.trc.domain.shop.ManagerDO;
import org.trc.domain.shop.ShopDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.ShopException;
import org.trc.service.shop.IManagerService;
import org.trc.service.shop.IShopService;
import org.trc.util.Pagenation;

import java.util.Calendar;
import java.util.List;

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
    @Autowired
    private IManagerService managerService;
    @Autowired
    private IShopService shopService;
    @Override
    public Pagenation<ShopDO> queryShopDOListForPage(ShopDO shopDO, Pagenation<ShopDO> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(shopDO, "传入参数不能为空");
            return shopService.selectListByPage(shopDO, pageRequest);
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询ShopDO校验参数异常!", e);
            throw new ShopException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "多条件查询ShopDO校验参数异常!");
        } catch (Exception e) {
            logger.error("多条件查询ShopDO信息异常!", e);
            throw new ShopException(ExceptionEnum.SHOP_QUERY_EXCEPTION, "多条件查询ShopDO信息异常!");
        }
    }

    @Override
    @Cacheable(key="#id", expireTime= CacheExpire.MEDIUM)
    public ShopDO getShopDOById(Long id) {
        try {
            Assert.isTrue(id != null, "查询Id不能为空!");
            return shopService.selectById(id);
        } catch (IllegalArgumentException e) {
            logger.error("查询ShopDO传入Id为空!", e);
            throw new ShopException(ExceptionEnum.PARAM_ERROR_ILLEGAL, "查询ShopDO校验参数异常!");
        } catch (Exception e) {
            logger.error("根据ID=>[" + id + "]查询ShopDO信息异常!", e);
            throw new ShopException(ExceptionEnum.SHOP_QUERY_EXCEPTION, "根据ID=>[" + id + "]查询ShopDO信息异常!");
        }
    }

    @Override
    public int addShopDO(ShopDO shopDO) {
        try {
            validateForAdd(shopDO);
            return shopService.insertSelective(shopDO);
        } catch (IllegalArgumentException e) {
            logger.error("新增ShopDO校验参数异常!", e);
            throw new ShopException(ExceptionEnum.SHOP_SAVE_EXCEPTION, "新增ShopDO校验参数异常!");
        } catch (Exception e) {
            logger.error("新增ID=>[" + shopDO.getId() + "]的ShopDO信息异常!", e);
            throw new ShopException(ExceptionEnum.SHOP_SAVE_EXCEPTION, "新增ID=>[" + shopDO.getId() + "]的ShopDO信息异常!");
        }
    }

    /**
     * Validate Add
     *
     * @param shopDO ShopDO
     */
    private void validateForAdd(ShopDO shopDO) {
        Assert.isTrue(shopDO != null, "shopDO不能为空!");
        Assert.isTrue(StringUtils.isNotEmpty(shopDO.getShopName()), "店铺名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(shopDO.getLogo()), "店铺logo不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(shopDO.getChannelCode()), "店铺所属渠道不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(shopDO.getServicePhone()), "店铺客服电话不能为空");
    }

    @Override
    public int modifyShopDO(ShopDO shopDO) {
        try {
            validateForUpdate(shopDO);
            shopDO.setUpdateTime(Calendar.getInstance().getTime());
            return shopService.updateByPrimaryKey(shopDO);
        } catch (IllegalArgumentException e) {
            logger.error("修改ShopDO校验参数异常!", e);
            throw new ShopException(ExceptionEnum.SHOP_UPDATE_EXCEPTION, "修改ShopDO校验参数异常!");
        } catch (Exception e) {
            logger.error("修改ID=>[" + shopDO.getId() + "]的ShopDO信息异常", e);
            throw new ShopException(ExceptionEnum.SHOP_UPDATE_EXCEPTION, "修改ID=>[" + shopDO.getId() + "]的ShopDO信息异常");
        }
    }

    /**
     * Validate Update
     *
     * @param shopDO ShopDO
     * @return ShopDO
     */
    private ShopDO validateForUpdate(ShopDO shopDO) {

        Assert.isTrue(shopDO != null, "ShopDO不能为空!");
        Assert.isTrue(shopDO.getId() != null, "查询Id不能为空!");
        Assert.isTrue(StringUtils.isNotEmpty(shopDO.getShopName()), "店铺名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(shopDO.getLogo()), "店铺logo不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(shopDO.getChannelCode()), "店铺所属渠道不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(shopDO.getServicePhone()), "店铺客服电话不能为空");
        ShopDO oldShopDO = shopService.selectById(shopDO.getId());
        Assert.isTrue(oldShopDO != null, "查询不到ID=>[" + shopDO.getId() + "]的信息!");
        return oldShopDO;
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

    @Override
    public ManagerDO getManagerByParam(String userId) {
        try {
            Assert.isTrue(StringUtils.isNotEmpty(userId), "查询用户id不能为空");
            ManagerDO managerDO = new ManagerDO();
            managerDO.setUserId(userId);
            managerDO.setIsDeleted(0);
            return managerService.selectOne(managerDO);
        }catch (IllegalArgumentException e) {
            logger.error("查询ManagerDO校验参数异常!", e);
            throw new ShopException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "查询ManagerDO校验参数异常!");
        }catch (Exception e) {
            logger.error("查询ManagerDO记录失败", e);
            throw new ShopException(ExceptionEnum.MANAGER_QUERY_EXCEPTION, "查询ManagerDO记录失败");
        }
    }
}

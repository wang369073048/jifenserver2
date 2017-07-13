package org.trc.biz.impl.auth;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.biz.auth.IAuthBiz;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.AuthQueryDTO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.AuthException;
import org.trc.form.auth.AuthForm;
import org.trc.form.pagehome.BannerContentForm;
import org.trc.service.auth.IAuthService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Service("authBiz")
public class AuthBiz implements IAuthBiz {

    private Logger logger = LoggerFactory.getLogger(AuthBiz.class);

    @Autowired
    private IAuthService authService;
    @Override
    public int saveAuth(Auth auth) {
        try{
            validateForAdd(auth);
            int result = authService.insertSelective(auth);
            if(result != 1){
                logger.error("新增Auth异常,参数为:{}", auth);
                throw new AuthException(ExceptionEnum.AUTH_SAVE_EXCEPTION, "新增Auth异常!");
            }
            logger.info("新增ID=>[{}]的Auth成功",auth.getId());
            return result;
        }catch (IllegalArgumentException e){
            logger.error("新增Auth校验参数异常!", e);
            throw new AuthException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "参数校验异常!");
        }catch (Exception e) {
            logger.error("新增Auth异常,请求参数为:{}",auth);
            throw new AuthException(ExceptionEnum.AUTH_SAVE_EXCEPTION, "新增Auth异常!");
        }
    }

    @Override
    public int updateAuth(Auth auth) {
        try{
            validateForUpdate(auth);
            return authService.updateByPrimaryKey(auth);
        }catch (IllegalArgumentException e){
            logger.error("更新Auth校验参数异常!", e);
            throw new AuthException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "参数校验异常!");
        }catch (Exception e) {
            logger.error("更新Auth异常,请求参数为:{}",auth);
            throw new AuthException(ExceptionEnum.AUTH_UPDATE_EXCEPTION, "新增Auth异常!");
        }
    }

    @Override
    public Auth getNativeAuthByUserId(String userId) {
        return getAuthByUserId(userId);
    }

    @Override
    public Auth getAuthByUserId(String userId) {
        try{
            Assert.isTrue(StringUtils.isNotEmpty(userId), "查询用户id不能为空");
            Auth auth = new Auth();
            auth.setUserId(userId);
            auth.setIsDeleted(0);
            return authService.selectOne(auth);
        }catch (IllegalArgumentException e){
            logger.error("查询Auth校验参数异常!", e);
            throw new AuthException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "参数校验异常!");
        }catch (Exception e) {
            logger.error("查询权限记录失败", e);
            throw new AuthException(ExceptionEnum.AUTH_QUERY_EXCEPTION, "查询权限记录失败");
        }
    }

    @Override
    public Auth getAuthById(Long id) {
        Auth auth = new Auth();
        auth.setId(id);
        auth.setIsDeleted(0);
        return authService.selectOne(auth);
    }

    @Override
    public Auth getAuthByShopId(Long shopId) {
        return null;
    }

    @Override
    public Pagenation<Auth> queryAuthListForPage(AuthQueryDTO query, Pagenation<Auth> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(query, "查询参数不能为空");
            return authService.queryAuthListByCondition(query, pageRequest);
        }catch (IllegalArgumentException e) {
            logger.error("查询权限记录校验参数异常!", e);
            throw new AuthException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "查询权限记录校验参数异常!");
        } catch (Exception e) {
            logger.error("查询权限记录失败", e);
            throw new AuthException(ExceptionEnum.AUTH_QUERY_EXCEPTION, "查询权限记录失败");
        }
    }

    /**
     * Validate add
     *
     * @param auth Auth
     * @return Auth
     */
    private void validateForAdd(Auth auth) {
        Assert.isTrue(auth != null, "Auth不能为空!");
        Assert.isTrue(StringUtils.isNotEmpty(auth.getContactsUser()), "管理员姓名不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(auth.getPhone()), "管理员电话不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(auth.getUserId()), "管理员userid不能为空");
        Assert.notNull(auth.getShopId(), "管理员所属店铺id不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(auth.getChannelCode()), "管理员所属渠道不能为空");
    }

    /**
     * Validate Update
     *
     * @param auth Auth
     * @return Auth
     */
    private void validateForUpdate(Auth auth) {
        Assert.isTrue(auth != null, "Auth不能为空!");
        Assert.isTrue(auth.getId() != null, "查询Id不能为空!");
        Assert.isTrue(StringUtils.isNotEmpty(auth.getContactsUser()), "管理员姓名不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(auth.getPhone()), "管理员电话不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(auth.getUserId()), "管理员userid不能为空");
        Assert.notNull(auth.getShopId(), "管理员所属店铺id不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(auth.getChannelCode()), "管理员所属渠道不能为空");
        Auth oldAuth = authService.selectByPrimaryKey(auth.getId());
        Assert.isTrue(oldAuth != null, "查询不到ID=>[" + auth.getId() + "]的信息!");
    }
}

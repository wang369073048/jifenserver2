package org.trc.service.impl.auth;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.AuthQueryDTO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.AuthException;
import org.trc.mapper.auth.IAuthMapper;
import org.trc.service.auth.IAuthService;
import org.trc.service.impl.BaseService;
import org.trc.util.Pagenation;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Service("authService")
public class AuthService extends BaseService<Auth,Long> implements IAuthService{

	private Logger logger = LoggerFactory.getLogger(AuthService.class);
	
    @Autowired
    private IAuthMapper authMapper;
    @Override
    public Pagenation<Auth> queryAuthListByCondition(AuthQueryDTO query, Pagenation<Auth> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<Auth> list = authMapper.queryAuthListByCondition(query);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }
    
    @Override
    public Auth getAuthByUserId(String userId) {
        try {
            Assert.isTrue(StringUtils.isNotEmpty(userId), "查询用户id不能为空");
            Auth param = new Auth();
            param.setUserId(userId);
            param.setIsDeleted(0);
            return authMapper.selectOne(param);
        }catch (IllegalArgumentException e) {
            logger.error("查询Auth校验参数异常!", e);
            throw new AuthException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "查询Auth校验参数异常!");
        }catch (Exception e) {
            logger.error("查询权限记录失败", e);
            throw new AuthException(ExceptionEnum.AUTH_QUERY_EXCEPTION, "查询权限记录失败");
        }
    }
}

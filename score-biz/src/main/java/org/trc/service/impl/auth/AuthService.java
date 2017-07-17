package org.trc.service.impl.auth;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.trc.domain.auth.Auth;
import org.trc.domain.dto.AuthQueryDTO;

import org.trc.domain.dto.GoodsRecommendDTO;
import org.trc.mapper.auth.IAuthMapper;
import org.trc.service.auth.IAuthService;
import org.trc.service.impl.BaseService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Service("authService")
public class AuthService extends BaseService<Auth,Long> implements IAuthService{

    @Autowired
    private IAuthMapper authMapper;
    @Override
    public Pagenation<Auth> queryAuthListByCondition(AuthQueryDTO query, Pagenation<Auth> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<Auth> list = authMapper.queryAuthListByCondition(query);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }
}

package org.trc.mapper.auth;

import org.trc.domain.auth.Auth;
import org.trc.domain.dto.AuthQueryDTO;
import org.trc.util.BaseMapper;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * Created by hzwzhen on 2017/6/14.
 */
public interface IAuthMapper extends BaseMapper<Auth>{

    List<Auth> queryAuthListByCondition(AuthQueryDTO query);
}

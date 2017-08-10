package org.trc.service.auth;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.AuthQueryDTO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * Created by hzwzhen on 2017/6/14.
 */
public interface IAuthService extends IBaseService<Auth,Long>{
	
	Auth getAuthByUserId(String userId);

    Pagenation<Auth> queryAuthListByCondition(AuthQueryDTO query, Pagenation<Auth> pageRequest);
}

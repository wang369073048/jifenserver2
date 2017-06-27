package org.trc.service.impl.auth;

import org.springframework.stereotype.Service;
import org.trc.domain.auth.Auth;
import org.trc.service.auth.IAuthService;
import org.trc.service.impl.BaseService;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Service("authService")
public class AuthService extends BaseService<Auth,Long> implements IAuthService{
}

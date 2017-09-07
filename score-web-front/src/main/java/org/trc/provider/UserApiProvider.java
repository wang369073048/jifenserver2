package org.trc.provider;

import javax.annotation.Resource;

import com.tairanchina.beego.api.service.BeegoService;
import com.tairanchina.md.account.user.service.UserProfileMetaService;
import com.tairanchina.md.account.user.service.UserProfileService;
import com.tairanchina.md.account.user.service.UserService;

/**
 * Created by gJason on 2016/12/14.
 */
public class UserApiProvider {

	@Resource
    public static UserService userService;
    
	@Resource
    public static UserProfileService userProfileService;
    
	@Resource
    public static UserProfileMetaService userProfileMetaService;

	@Resource
    public static BeegoService beegoService;

}

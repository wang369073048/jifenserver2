package org.trc.cache;

import org.trc.provider.UserApiProvider;

import com.tairanchina.md.account.user.model.UserProfileMeta;

public class UserAvatar {
	
	private static String DefaultAvatar;
	
	public static String getDefaultAvatar(){
		if(DefaultAvatar == null){
			synchronized (UserAvatar.class) {
				if(DefaultAvatar == null){
					UserProfileMeta userProfileMeta = UserApiProvider.userProfileMetaService
							.getUserProfileMeta("avatar");
//					DefaultAvatar = userProfileMeta.getDefaultValue();
				}
			}
		}
		return DefaultAvatar;
	}
}

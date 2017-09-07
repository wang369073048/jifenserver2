package org.trc.util;

import org.trc.provider.UserApiProvider;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.txframework.core.jdbc.PageRequest;
/**
 * Created by gJason on 2016/12/14.
 */
public class CommonUtils {
    /**
     * userId获取手机号
     * @param userId
     * @return
     */
    public static String getPhoneByUserId(String userId) {
        if (StringUtils.isBlank(userId))
            return null;
        String phone = UserApiProvider.userService.getPhone(userId);
        return phone;
    }

    /**
     * 组装分页参数
     *
     */
    public static void toAssemblyRequestPage(PageRequest<?> pageRequest,String pageIndex,String pageSize){
        pageRequest.setPageData(Integer.valueOf(pageSize));
        pageRequest.setCurPage(Integer.valueOf(pageIndex));
    }
}

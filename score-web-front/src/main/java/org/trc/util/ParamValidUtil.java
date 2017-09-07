package org.trc.util;

import org.apache.commons.lang.StringUtils;

/**
 * 参数校验工具类
 * Created by huyan on 2016/7/14
 */

public class ParamValidUtil {


    /**
     * 通常前端传到后台的布尔参数以String类型传的
     * "1"：是，"0"：否，其他情况不合法
     * @param param
     * @return
     */
    public static boolean isBoolean(String param) {
        if (StringUtils.isNotBlank(param) && ("0".equals(param) || "1".equals(param))) {
            return true;
        }
        return false;
    }
}

package org.trc.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.txframework.core.jdbc.PageRequest;
import com.txframework.util.ObjectUtils;

/**
 * json 工具类
 * Created by huyan on 2016/7/14
 */

public class JSONUtil {

    public static <T> void putParam(JSONArray sourceJson, PageRequest<T> pageRequest, JSONObject targetJson) {
        targetJson.put("infos", sourceJson);
        targetJson.put("pageIndex", ObjectUtils.convertVal(pageRequest.getCurPage(), 1));
        targetJson.put("pageSize", ObjectUtils.convertVal(pageRequest.getPageData(), 10));
        targetJson.put("totalData", ObjectUtils.convertVal(pageRequest.getTotalData(), 0));
        targetJson.put("totalPage", ObjectUtils.convertVal(pageRequest.getTotalPage(), 0));
    }

    public static <T> void emptyPage(JSONObject targetJson) {
        targetJson.put("infos", new JSONArray());
        targetJson.put("pageIndex", 1);
        targetJson.put("pageSize", 10);
        targetJson.put("totalData",  0);
        targetJson.put("totalPage",  0);
    }

}

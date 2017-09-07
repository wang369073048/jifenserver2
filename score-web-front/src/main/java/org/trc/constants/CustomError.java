package org.trc.constants;

/**
 * 自定义错误信息
 * Created by huyan on 2016/11/21.
 */
public class CustomError {

    //结果为空
    public static class NO_DATA {
        public static String HOT_GOODS_ISNULL = "当前没有热门商品";

    }


    //不合法参数
    public static class ILLEGAL_PARAM {

    }

    //无效参数
    public static class INVALID_PARAM {

        public static String INVALID = "无效参数";
        public static String USER_SCORE_NOT_EXIST = "用户积分不存在";
        public static String USER_SCORE_NOT_ENOUGH = "用户积分不够";
        public static String GET_CHANNEL_ERROR = "根据应用id获取渠道失败";
        public static String SHOP_CHANNEL_ERROR = "店铺和应用不匹配";
    }

    //不合法操作
    public static class ILLEGAL_OPERATION {
        public static String FORBIDDEN = "";
    }


}

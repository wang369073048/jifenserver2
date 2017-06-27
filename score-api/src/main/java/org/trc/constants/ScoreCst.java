package org.trc.constants;

/**
 * Created by wangzhen
 */

public final class ScoreCst {

    /**
     * 渠道分类
     */
    public enum ChannelCode {
        trc_mall,         //泰然商城
        trc_score,        //积分平台
        trc_finance,      //泰然金融
        trc_happy_life;   //小泰乐活
    }

    /**
     * 业务类型
     */
    public enum BusinessCode {
        exchangeIn,  //兑入
        exchangeInCorrect,//兑入冲正
        exchangeOut, //兑出
        exchangeOutCorrect,//兑出冲正
        consume,     //消费（兑换商品或券）
        consumeCorrect,//消费冲正
        income,      //收入（商家商品或券的兑换收入）
        incomeCorrect,//收入冲正
        freeze,      //冻结
        expire;      //过期
    }

    /**
     * 流水类型
     */
    public enum FlowType {
        income,      //收入
        expend,      //支出
        freeze;      //冻结
    }

    /**
     * 通兑方向
     */
    public enum Direction {
        entranceOnly,  //只进不出
        exitOnly,      //只出不进
        bothway;       //可进可出
    }

    public enum ExchangeCurrency{
        TCOIN("T币");
        private String value;
        ExchangeCurrency(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
    }

    public static void main(String[] args){
        ExchangeCurrency exchangeCurrency = Enum.valueOf(ScoreCst.ExchangeCurrency.class,"TCOIN");
        System.out.println(exchangeCurrency.getValue());
    }

    /**
     * 缓存常量类
     */
    public final static class Cache {

        /**
         * @description 缓存KEY
         * @author huyan
         * @Date   2016年12月8日
         */
        public final static class KEY {


            public static final String SHOP_INFO = "SHOP_INFO";


        }

        /**
         * @description Hash缓存字段
         * @author huyan
         * @Date   2016年12月8日
         */
        public final static class Field {

            /**所有 */
            public static final String ALL ="ALL";

            /**主键ID */
            public static final String ID = "ID_";

        }
    }

}

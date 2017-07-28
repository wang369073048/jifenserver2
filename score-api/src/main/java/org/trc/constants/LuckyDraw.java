package org.trc.constants;

/**
 * Created by george on 2017/5/10.
 */
public class LuckyDraw {

    public static final class PrizyType{

        public static final String SCORE = "SCORE";

        public static final String TCOIN = "TCOIN";

        public static final String GOODS = "GOODS";

        public static final String TKY_FOR_PARTICIPATING = "TKY_FOR_PARTICIPATING";

    }

    public static final class GoodsType{
        public static final String VIRTUAL = "VIRTUAL";
        public static final String MATERIAL = "MATERIAL";
        public static final String TKY_FOR_PARTICIPATING = "TKY_FOR_PARTICIPATING";
    }

    public static final class WinningLimit{

        public static final int INDIVIDUAL_WINNING_LIMIT = -1;

        public static final int EVENT_WINNING_LIMIT = -1;

        public static final String PER_DAY = "PER_DAY";

        public static final String THE_WHOLE_ACTIVITY = "THE_WHOLE_ACTIVITY";

    }

    public static final class WinningState{

        /**
         * 待发放
         */
        public static final int UNISSUED = 0;

        /**
         * 发放成功
         */
        public static final int ALREADY_ISSUED = 1;

        /**
         * 发放失败
         */
        public static final int PAYMENT_FAILURE = 2;

        /**
         * 抽奖订单生成失败，未知异常
         */
        public static final int PRIZE_ORDER_UNKNOWN_ERROR = 6;

        /**
         * 抽奖订单生成失败，库存不足
         */
        public static final int PRIZE_ORDER_IS_NOT_GENERATED = 9;

    }

}

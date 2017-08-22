package org.trc.constants;

import java.io.Serializable;

/**
 * Created by george on 2017/7/12.
 */
public class BusinessType implements Serializable{

    /**
     * T币兑换积分业务
     */
    public static final String TCOIN_EXCHANGE_IN = "TCOIN_EXCHANGE_IN";

    /**
     * 积分兑换金融卡券业务
     */
    public static final String EXCHANGE_TAIRAN_COUPONS = "EXCHANGE_TAIRAN_COUPONS";

    /**
     * 积分兑换泰然优惠券业务
     */
    public static final String EXCHANGE_FINANCIAL_COUPONS = "EXCHANGE_FINANCIAL_COUPONS";

    /**
     * 积分抽奖T币
     */
    public static final String LUCKY_DRAW_GAIN_T_COINS = "LUCKY_DRAW_GAIN_T_COINS";

    /**
     * 积分抽奖中积分
     */
    public static final String LUCKY_DRAW_GAIN_SCORE_COINS = "LUCKY_DRAW_GAIN_SCORE_COINS";

    /**
     * 积分抽奖中泰然优惠券
     */
    public static final String LUCKY_DRAW_GAIN_TAIRAN_COUPONS = "LUCKY_DRAW_GAIN_TAIRAN_COUPONS";

    /**
     * 积分抽奖中卡券
     */
    public static final String LUCKY_DRAW_GAIN_FINANCIAL_COUPONS = "LUCKY_DRAW_GAIN_FINANCIAL_COUPONS";

    /**
     * 抽奖生成订单
     */
    public static final String LUCKY_DRAW_GENERATE_ORDER = "LUCKY_DRAW_GENERATE_ORDER";

    /**
     *  店铺获取抽奖积分
     */
    public static final String GAIN_LOTTERY_SCORE = "GAIN_LOTTERY_SCORE";

}

package org.trc.constants;


public class OrderStatus {

    /**
     * 待发货
     * 用户兑换，商城未发货
     */
    public static final int WAITING_FOR_DELIVERY = 1;

    /**
     * 等待收货
     * 订单退款或取消
     */
    public static final int WAITING_FOR_RECEIVING = 2;

    /**
     * 交易成功
     */
    public static final int TRANSACTION_SUCCESS = 3;

    /**
     * 发货后15天
     * 系统确认收货
     */
    public static final int SYSTEM_CONFIRM_SUCCESS = 4;

    /**
     * 退货
     */
    public static final int RETURN_GOODS = 5;

}

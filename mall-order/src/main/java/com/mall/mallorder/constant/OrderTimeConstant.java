package com.mall.mallorder.constant;

public class OrderTimeConstant {

    /**
     * 订单未支付超时时间：30分钟
     */
    public static final long ORDER_CANCEL_DELAY_MILLIS
            = System.currentTimeMillis() + 2 * 60 * 1000L;
}
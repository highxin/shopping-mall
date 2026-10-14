package com.mall.mallorder.transition;

/**
 * <p>描述: 订单状态转换</p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/11
 */
public class OrderStatusTransition {
    public static final String CANCELLED_STATUS_MSG = "订单状态异常";

    public static boolean canTransition(Integer oldStatus, Integer newStatus){

        return switch (oldStatus) {
            //待支付
            case 0 -> newStatus == 1
                    || newStatus == 8;
            //已支付
            case 1 -> newStatus == 2
                    || newStatus == 8;
            //待发货
            case 2 -> newStatus == 3;
            //待收货
            case 3 -> newStatus == 4;
            //已收货
            case 4 -> newStatus == 7;
            //退货中
            case 5 -> newStatus == 7;
            //退款中
            case 6 -> newStatus == 8;
            //已完成
            case 7 -> false;
            //已取消
            case 8 -> false;
            default -> false;
        };

    }
}

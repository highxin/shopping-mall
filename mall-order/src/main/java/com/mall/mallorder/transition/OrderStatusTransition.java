package com.mall.mallorder.transition;

import com.mall.common.enums.OrderStatusEnum;

import java.util.Objects;

/**
 * <p>描述: 订单状态转换</p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/11
 */
public class OrderStatusTransition {

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

    /**
     * 生成状态流转失败时的具体错误提示，替代笼统的“订单状态异常”。
     *
     * @param oldStatus 订单当前状态码
     * @param newStatus 目标操作试图转换到的状态码
     * @return 针对当前场景的具体错误文案
     */
    public static String buildErrorMessage(Integer oldStatus, Integer newStatus) {
        String current = desc(oldStatus);
        String target = targetAction(newStatus);
        // 已是终态（已完成/已取消）且目标不是同一个终态
        if (Objects.equals(oldStatus, OrderStatusEnum.COMPLETED.getCode())) {
            return "订单已完成，无法" + target;
        }
        if (Objects.equals(oldStatus, OrderStatusEnum.CANCELLED.getCode())) {
            return "订单已取消，无法" + target;
        }
        return "订单当前状态为【" + current + "】，无法执行【" + target + "】操作";
    }

    /**
     * 根据订单状态码返回描述
     */
    private static String desc(Integer code) {
        OrderStatusEnum status = OrderStatusEnum.of(code);
        return status == null ? "未知" : status.getDesc();
    }

    /**
     * 根据目标状态码反推业务动作名称
     */
    private static String targetAction(Integer newStatus) {
        if (Objects.equals(newStatus, OrderStatusEnum.PAID.getCode())) {
            return "支付";
        }
        if (Objects.equals(newStatus, OrderStatusEnum.CANCELLED.getCode())) {
            return "取消";
        }
        if (Objects.equals(newStatus, OrderStatusEnum.DELIVERED.getCode())) {
            return "发货";
        }
        if (Objects.equals(newStatus, OrderStatusEnum.WAIT_RECEIVE.getCode())) {
            return "收货确认";
        }
        if (Objects.equals(newStatus, OrderStatusEnum.COMPLETED.getCode())) {
            return "完成订单";
        }
        return "变更订单状态";
    }
}
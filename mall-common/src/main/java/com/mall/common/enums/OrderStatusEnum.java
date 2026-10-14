package com.mall.common.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
public enum OrderStatusEnum {

    UNPAID(0, "待支付"),
    PAID(1, "已支付"),
    DELIVERED(2, "待发货"),
    WAIT_RECEIVE(3, "待收货"),
    RECEIVED(4, "已收货"),
    RETURNING(5, "退货中"),
    REFUNDING(6, "退款中"),
    COMPLETED(7, "已完成"),
    CANCELLED(8, "已取消"),


    ;

    private final Integer code;
    private final String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据 code 获取枚举
     */
    public static OrderStatusEnum of(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 根据 code 获取描述
     */
    public static String getDescByCode(Integer code) {
        OrderStatusEnum status = of(code);
        return status == null ? "" : status.getDesc();
    }
}
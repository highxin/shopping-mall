package com.mall.common.exception.errorcode;

import com.mall.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements ErrorCode {

    ORDER_NOT_FOUND(40001, "订单不存在"),
    ORDER_STATUS_ERROR(40002, "订单状态异常"),
    ORDER_PAID_CANNOT_CANCEL(40003, "已支付订单不能取消"),
    ORDER_ALREADY_CANCELLED(40004, "订单已取消"),
    ORDER_COMPLETED_CANNOT_CANCEL(40005, "订单已完成,不能取消"),
    ORDER_NO_PERMISSION(40006, "无权操作该订单"),
    ORDER_ITEM_NOT_FOUND(40007, "订单商品不存在"),
    ORDER_ITEM_SKU_IDS_EMPTY(40008, "订单商品skuIds为空"),
    ORDER_ADDRESS_NOT_FOUND(40009, "订单地址不存在"),
    ORDER_ADDRESS_EMPTY(40010, "订单地址为空"),
    CART_ITEM_EMPTY(40011, "购物车商品为空");

    private final Integer code;
    private final String message;

}
package com.mall.mallcoupon.enums;

import com.mall.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponErrorCode implements ErrorCode {

    TEMPLATE_NOT_FOUND(60001, "优惠券不存在"),
    TEMPLATE_NOT_PUBLISHED(60002, "优惠券未发布或已下架"),
    NOT_IN_RECEIVE_TIME(60003, "不在领取时间内"),
    SOLD_OUT(60004, "优惠券已领完"),
    ALREADY_RECEIVED(60005, "您已领取过该券"),
    COUPON_NOT_FOUND(60006, "优惠券不存在"),
    COUPON_STATUS_ERROR(60007, "优惠券状态不允许此操作"),
    COUPON_EXPIRED(60008, "优惠券已过期"),
    COUPON_NOT_OWNED(60009, "优惠券不属于当前用户"),
    THRESHOLD_NOT_REACHED(60010, "未达到使用门槛"),
    SECKILL_NOT_STARTED(60011, "秒杀未开始"),
    SECKILL_ENDED(60012, "秒杀已结束"),
    SECKILL_SOLD_OUT(60013, "秒杀券已抢完"),
    SECKILL_REPEAT(60014, "请勿重复提交"),
    SECKILL_QUEUEING(60015, "排队中，请稍后查询"),
    INTERNAL_TOKEN_INVALID(60016, "内部调用鉴权失败"),
    ;

    private final Integer code;
    private final String message;
}
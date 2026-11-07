package com.mall.common.dto.coupon;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CouponLockDTO {

    @NotNull(message = "优惠券ID不能为空")
    private Long couponId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.00", message = "订单金额不能为负数")
    private BigDecimal orderAmount;
}
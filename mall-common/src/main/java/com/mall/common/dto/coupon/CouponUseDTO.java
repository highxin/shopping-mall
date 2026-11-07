package com.mall.common.dto.coupon;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CouponUseDTO {

    @NotNull(message = "优惠券ID不能为空")
    private Long couponId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "订单号不能为空")
    private String orderNo;
}
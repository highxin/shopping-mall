package com.mall.common.dto.coupon;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InternalCouponPreviewDTO {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Valid
    @NotNull(message = "试算参数不能为空")
    private CouponPreviewDTO preview;
}
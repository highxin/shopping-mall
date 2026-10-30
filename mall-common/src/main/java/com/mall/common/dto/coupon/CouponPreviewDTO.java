package com.mall.common.dto.coupon;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CouponPreviewDTO {

    @NotEmpty(message = "商品明细不能为空")
    @Valid
    private List<CouponItemDTO> items;

    /** 为空表示“帮我选最优券”；不为空表示校验指定券 */
    private Long couponId;
}
package com.mall.common.vo.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponOptionVO {

    private Long couponId;
    private String couponName;
    private BigDecimal discountAmount;
    private BigDecimal payableAmount;
}
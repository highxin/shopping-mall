package com.mall.mallcoupon.calculator;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponCandidate {

    private Long couponId;
    private Long templateId;
    private String name;
    private Integer type;
    private BigDecimal thresholdAmount;
    private BigDecimal discountAmount;
    private BigDecimal discountRate;
    private BigDecimal maxDiscountAmount;
    private LocalDateTime endTime;

    public CouponRule toRule() {
        return CouponRule.builder()
                .type(type)
                .thresholdAmount(thresholdAmount)
                .discountAmount(discountAmount)
                .discountRate(discountRate)
                .maxDiscountAmount(maxDiscountAmount)
                .build();
    }
}
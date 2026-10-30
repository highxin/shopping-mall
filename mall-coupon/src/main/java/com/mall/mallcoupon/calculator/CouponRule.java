package com.mall.mallcoupon.calculator;

import com.mall.mallcoupon.entity.CouponTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponRule {

    /** 1满减 2折扣 3无门槛 */
    private Integer type;
    /** 满减门槛 */
    private BigDecimal thresholdAmount;
    /** 满减金额 */
    private BigDecimal discountAmount;
    /** 折扣率 */
    private BigDecimal discountRate;
    /** 最大折扣金额 */
    private BigDecimal maxDiscountAmount;

    public static CouponRule from(CouponTemplate template) {
        return CouponRule.builder()
                .type(template.getType())
                .thresholdAmount(template.getThresholdAmount())
                .discountAmount(template.getDiscountAmount())
                .discountRate(template.getDiscountRate())
                .maxDiscountAmount(template.getMaxDiscountAmount())
                .build();
    }
}
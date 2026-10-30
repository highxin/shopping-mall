package com.mall.mallcoupon.calculator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CouponCalculatorTest {

    private final FullReductionCalculator fullReduction = new FullReductionCalculator();
    private final DiscountCalculator discount = new DiscountCalculator();
    private final NoThresholdCalculator noThreshold = new NoThresholdCalculator();

    /** 满减优惠券 */
    @Test
    void fullReductionShouldRespectThreshold() {
        CouponRule rule = CouponRule.builder()
                .type(1)
                .thresholdAmount(new BigDecimal("100.00"))
                .discountAmount(new BigDecimal("10.00"))
                .build();

        assertThat(fullReduction.calculate(rule, new BigDecimal("99.99")))
                .isEqualByComparingTo("0.00");
        assertThat(fullReduction.calculate(rule, new BigDecimal("100.00")))
                .isEqualByComparingTo("10.00");
        assertThat(fullReduction.calculate(rule, new BigDecimal("150.00")))
                .isEqualByComparingTo("10.00");
    }
    /** 优惠不能超过订单金额 */
    @Test
    void fullReductionShouldNotExceedOrderAmount() {
        CouponRule rule = CouponRule.builder()
                .type(1)
                .thresholdAmount(new BigDecimal("10.00"))
                .discountAmount(new BigDecimal("20.00"))
                .build();

        assertThat(fullReduction.calculate(rule, new BigDecimal("15.00")))
                .isEqualByComparingTo("15.00");
    }
    /** 折扣要封顶 */
    @Test
    void discountShouldCapAtMaxDiscount() {
        CouponRule rule = CouponRule.builder()
                .type(2)
                .discountRate(new BigDecimal("0.850"))
                .maxDiscountAmount(new BigDecimal("50.00"))
                .build();

        assertThat(discount.calculate(rule, new BigDecimal("100.00")))
                .isEqualByComparingTo("15.00");
        assertThat(discount.calculate(rule, new BigDecimal("1000.00")))
                .isEqualByComparingTo("50.00");
    }
    /** 折扣要四舍五入 */
    @Test
    void discountShouldRoundHalfUp() {
        CouponRule rule = CouponRule.builder()
                .type(2)
                .discountRate(new BigDecimal("0.850"))
                .maxDiscountAmount(new BigDecimal("100.00"))
                .build();

        // 99.99 × 0.15 = 14.9985，四舍五入 15.00
        assertThat(discount.calculate(rule, new BigDecimal("99.99")))
                .isEqualByComparingTo("15.00");
    }
    /** 无门槛优惠券 */
    @Test
    void noThresholdShouldNotExceedOrderAmount() {
        CouponRule rule = CouponRule.builder()
                .type(3)
                .discountAmount(new BigDecimal("5.00"))
                .build();

        assertThat(noThreshold.calculate(rule, new BigDecimal("3.00")))
                .isEqualByComparingTo("3.00");
        assertThat(noThreshold.calculate(rule, new BigDecimal("100.00")))
                .isEqualByComparingTo("5.00");
    }
}
package com.mall.mallcoupon.calculator;

import com.mall.mallcoupon.enums.CouponTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class DiscountCalculator implements CouponCalculator {

    @Override
    public CouponTypeEnum type() {
        return CouponTypeEnum.DISCOUNT;
    }

    @Override
    public BigDecimal calculate(CouponRule rule, BigDecimal eligibleAmount) {
        if (eligibleAmount == null || eligibleAmount.signum() <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        // 有门槛优惠券，订单金额必须大于等于门槛金额
        if (rule.getThresholdAmount() != null
                && eligibleAmount.compareTo(rule.getThresholdAmount()) < 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        // 折扣率必须在 0 到 1 之间
        BigDecimal rate = rule.getDiscountRate();
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0
                || rate.compareTo(BigDecimal.ONE) >= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // 优惠 = 原价 × (1 - 折扣率)
        BigDecimal discount = eligibleAmount.multiply(BigDecimal.ONE.subtract(rate));
        if (rule.getMaxDiscountAmount() != null) {
            discount = discount.min(rule.getMaxDiscountAmount());
        }
        // 优惠不能超过订单金额
        discount = discount.min(eligibleAmount);
        return discount.setScale(2, RoundingMode.HALF_UP);
    }
}
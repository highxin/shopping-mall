package com.mall.mallcoupon.calculator;

import com.mall.mallcoupon.enums.CouponTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FullReductionCalculator implements CouponCalculator {

    @Override
    public CouponTypeEnum type() {
        return CouponTypeEnum.FULL_REDUCTION;
    }

    @Override
    public BigDecimal calculate(CouponRule rule, BigDecimal eligibleAmount) {
        if (eligibleAmount == null || eligibleAmount.signum() <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        if (rule.getThresholdAmount() == null
                || eligibleAmount.compareTo(rule.getThresholdAmount()) < 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal discount = rule.getDiscountAmount() == null
                ? BigDecimal.ZERO : rule.getDiscountAmount();
        // 优惠不能超过订单金额
        return discount.min(eligibleAmount).setScale(2, RoundingMode.HALF_UP);
    }
}
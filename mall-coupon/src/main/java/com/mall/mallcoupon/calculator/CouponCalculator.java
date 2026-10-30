package com.mall.mallcoupon.calculator;

import com.mall.mallcoupon.enums.CouponTypeEnum;

import java.math.BigDecimal;
/**
 * 所有计算器的统一接口
 */
public interface CouponCalculator {

    /**
     * @return 券类型
     */
    CouponTypeEnum type();

    /**
     * @param rule           券规则
     * @param eligibleAmount 参与计算的订单金额
     * @return 优惠金额，>= 0；不可用时返回 0
     */
    BigDecimal calculate(CouponRule rule, BigDecimal eligibleAmount);
}
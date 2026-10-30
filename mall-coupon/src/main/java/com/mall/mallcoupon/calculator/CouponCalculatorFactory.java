package com.mall.mallcoupon.calculator;

import com.mall.mallcoupon.exception.CouponException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 优惠券计算器工厂，根据券类型 type 找到对应的计算器
 */
@Component
public class CouponCalculatorFactory {

    private final Map<Integer, CouponCalculator> calculators;

    public CouponCalculatorFactory(List<CouponCalculator> calculatorList) {
        this.calculators = calculatorList.stream()
                .collect(Collectors.toUnmodifiableMap(
                        calculator -> calculator.type().getCode(),
                        Function.identity()  // calculator->calculator
                ));
    }

    public CouponCalculator get(Integer type) {
        CouponCalculator calculator = calculators.get(type);
        if (calculator == null) {
            throw new CouponException("不支持的优惠券类型: " + type);
        }
        return calculator;
    }
}
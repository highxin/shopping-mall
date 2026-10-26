package com.mall.mallcoupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponTypeEnum {

    FULL_REDUCTION(1, "满减券"),
    DISCOUNT(2, "折扣券"),
    NO_THRESHOLD(3, "无门槛券");

    private final Integer code;
    private final String desc;

    public static CouponTypeEnum of(Integer code) {
        if (code == null) {
            return null;
        }
        for (CouponTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
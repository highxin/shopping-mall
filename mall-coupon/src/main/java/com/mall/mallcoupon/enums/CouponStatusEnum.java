package com.mall.mallcoupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponStatusEnum {

    UNUSED(0, "未使用"),
    LOCKED(1, "锁定中"),
    USED(2, "已使用"),
    EXPIRED(3, "已过期");

    private final Integer code;
    private final String desc;

    public static CouponStatusEnum of(Integer code) {
        if (code == null) {
            return null;
        }
        for (CouponStatusEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
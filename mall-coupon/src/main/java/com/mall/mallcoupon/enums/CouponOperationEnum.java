package com.mall.mallcoupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponOperationEnum {

    LOCK(1, "锁定"),
    USE(2, "核销"),
    RELEASE(3, "释放");

    private final Integer code;
    private final String desc;
}
package com.mall.mallcoupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * <p>描述: 有效期类型</p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/26
 */
@Getter
@AllArgsConstructor
public enum ValidTypeEnum {

    FIXED(1, "固定有效期"),
    RELATIVE(2, "领取后N天");

    private final Integer code;
    private final String desc;
}
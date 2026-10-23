package com.mall.common.enums;

import lombok.Getter;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/21
 */
@Getter
public enum StatusEnum {
    /**
     * 上架
     */
    SELL_UP(1, "上架"),
    /**
     * 下架
     */
    SELL_DOWN(0, "下架");

    private final Integer code;
    private final String desc;

    StatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

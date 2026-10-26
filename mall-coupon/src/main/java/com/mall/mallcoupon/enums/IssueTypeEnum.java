package com.mall.mallcoupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>描述: 发放方式</p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/26
 */
@Getter
@AllArgsConstructor
public enum IssueTypeEnum {
    ADMIN(1, "后台发放"),
    RECEIVE(2, "主动领取"),
    SECKILL(3, "秒杀");

    private final Integer code;
    private final String desc;
}

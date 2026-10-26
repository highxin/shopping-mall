package com.mall.mallcoupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>描述: 模板状态</p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/26
 */
@Getter
@AllArgsConstructor
public enum TemplateStatusEnum {
    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    OFFLINE(2, "已下架");

    private final Integer code;
    private final String desc;
}

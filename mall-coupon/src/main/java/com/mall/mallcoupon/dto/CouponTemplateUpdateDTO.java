package com.mall.mallcoupon.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CouponTemplateUpdateDTO extends CouponTemplateCreateDTO {

    /** 由 Controller 用路径参数覆盖，不依赖前端传 */
    private Long id;
}
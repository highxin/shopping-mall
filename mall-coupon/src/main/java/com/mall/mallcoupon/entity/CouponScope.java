package com.mall.mallcoupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("coupon_scope")
public class CouponScope {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long templateId;

    /** 2品类 3SKU */
    private Integer scopeType;

    private Long scopeId;

    private LocalDateTime createTime;
}
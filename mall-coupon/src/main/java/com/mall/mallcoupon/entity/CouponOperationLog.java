package com.mall.mallcoupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("coupon_operation_log")
public class CouponOperationLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long couponId;

    private String orderNo;

    /** 1锁定 2核销 3释放 */
    private Integer operation;

    private Integer operatorType;

    private LocalDateTime createTime;
}
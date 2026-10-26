package com.mall.mallcoupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("coupon_receive_record")
public class CouponReceiveRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String requestId;

    private Long templateId;

    private Long userId;

    private Long couponId;

    /** 0处理中 1成功 2失败 */
    private Integer status;

    private String failReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
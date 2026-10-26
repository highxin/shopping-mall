package com.mall.mallcoupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("coupon")
public class Coupon {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long templateId;

    private Long userId;

    private String couponNo;

    /** 0未使用 1锁定 2已使用 3已过期 */
    private Integer status;

    /** 实际优惠金额快照，锁定时写入 */
    private BigDecimal discountAmount;

    private LocalDateTime receiveTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String orderNo;

    private LocalDateTime lockTime;

    private LocalDateTime useTime;

    /** 1后台 2领取 3秒杀 */
    private Integer source;

    @Version
    private Integer version;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
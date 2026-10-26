package com.mall.mallcoupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("coupon_template")
public class CouponTemplate {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    /** 1满减 2折扣 3无门槛 */
    private Integer type;

    /** 1全场 2品类 3指定SKU */
    private Integer scopeType;

    private BigDecimal thresholdAmount;

    private BigDecimal discountAmount;

    private BigDecimal discountRate;

    private BigDecimal maxDiscountAmount;

    private Integer totalStock;

    private Integer remainingStock;

    private Integer perUserLimit;

    /** 1后台发放 2主动领取 3秒杀 */
    private Integer issueType;

    /** 1固定区间 2领取后N天 */
    private Integer validType;

    private LocalDateTime validStartTime;

    private LocalDateTime validEndTime;

    private Integer validDays;

    private LocalDateTime receiveStartTime;

    private LocalDateTime receiveEndTime;

    private Integer status;

    @Version
    private Integer version;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
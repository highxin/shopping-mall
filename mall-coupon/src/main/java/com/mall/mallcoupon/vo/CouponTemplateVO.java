package com.mall.mallcoupon.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponTemplateVO {

    private Long id;
    private String name;
    private Integer type;
    private String typeDesc;
    private Integer scopeType;
    private BigDecimal thresholdAmount;
    private BigDecimal discountAmount;
    private BigDecimal discountRate;
    private BigDecimal maxDiscountAmount;
    private Integer totalStock;
    private Integer remainingStock;
    private Integer perUserLimit;
    private Integer issueType;
    private Integer validType;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private Integer validDays;
    private LocalDateTime receiveStartTime;
    private LocalDateTime receiveEndTime;
    private Integer status;
    private String statusDesc;
    private LocalDateTime createTime;

    /** 券中心用：当前用户是否已领取 */
    private Boolean received;
}
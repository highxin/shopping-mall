package com.mall.mallcoupon.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponVO {

    private Long id;
    private Long templateId;
    private String name;
    private Integer type;
    private Integer status;
    private String statusDesc;
    private BigDecimal thresholdAmount;
    private BigDecimal discountAmount;
    private BigDecimal actualDiscountAmount;
    private LocalDateTime receiveTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String orderNo;
}
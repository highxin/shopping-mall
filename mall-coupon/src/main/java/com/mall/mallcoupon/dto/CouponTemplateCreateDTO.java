package com.mall.mallcoupon.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponTemplateCreateDTO {

    @NotBlank(message = "券名称不能为空")
    @Size(max = 64, message = "券名称最多64个字符")
    private String name;

    /** 1满减 2折扣 3无门槛 */
    @NotNull(message = "券类型不能为空")
    @Min(value = 1, message = "券类型不合法")
    @Max(value = 3, message = "券类型不合法")
    private Integer type;

    /** 1全场 2品类 3指定SKU */
    @NotNull(message = "适用范围不能为空")
    @Min(value = 1, message = "适用范围不合法")
    @Max(value = 3, message = "适用范围不合法")
    private Integer scopeType;

    @NotNull(message = "使用门槛不能为空")
    @DecimalMin(value = "0.00", message = "使用门槛不能为负数")
    private BigDecimal thresholdAmount;

    @DecimalMin(value = "0.00", message = "优惠金额不能为负数")
    private BigDecimal discountAmount;

    @DecimalMin(value = "0.001", message = "折扣率必须大于0")
    @DecimalMax(value = "1.000", message = "折扣率不能大于1")
    private BigDecimal discountRate;

    @DecimalMin(value = "0.00", message = "最高优惠不能为负数")
    private BigDecimal maxDiscountAmount;

    @NotNull(message = "发行量不能为空")
    @Min(value = 1, message = "发行量至少为1")
    private Integer totalStock;

    @NotNull(message = "每人限领不能为空")
    @Min(value = 1, message = "每人限领至少为1")
    private Integer perUserLimit;

    /** 1后台发放 2主动领取 3秒杀 */
    @NotNull(message = "发放方式不能为空")
    @Min(value = 1, message = "发放方式不合法")
    @Max(value = 3, message = "发放方式不合法")
    private Integer issueType;

    /** 1固定区间 2领取后N天 */
    @NotNull(message = "有效期类型不能为空")
    @Min(value = 1, message = "有效期类型不合法")
    @Max(value = 2, message = "有效期类型不合法")
    private Integer validType;

    private LocalDateTime validStartTime;

    private LocalDateTime validEndTime;

    @Min(value = 1, message = "有效天数至少为1")
    private Integer validDays;

    @NotNull(message = "领取开始时间不能为空")
    private LocalDateTime receiveStartTime;

    @NotNull(message = "领取结束时间不能为空")
    private LocalDateTime receiveEndTime;
}
package com.mall.common.vo.coupon;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CouponPreviewVO {

    private Long couponId;
    private String couponName;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal payableAmount;
    private Boolean available;
    private String reason;
    private List<CouponOptionVO> options;

    // 可用优惠券
    public static CouponPreviewVO available(BigDecimal totalAmount,
                                            CouponOptionVO selected,
                                            List<CouponOptionVO> options) {
        CouponPreviewVO vo = new CouponPreviewVO();
        vo.setCouponId(selected.getCouponId());
        vo.setCouponName(selected.getCouponName());
        vo.setTotalAmount(totalAmount);
        vo.setDiscountAmount(selected.getDiscountAmount());
        vo.setPayableAmount(selected.getPayableAmount());
        vo.setAvailable(true);
        vo.setOptions(options);
        return vo;
    }

    // 无效优惠券
    public static CouponPreviewVO unavailable(BigDecimal totalAmount,
                                              Long couponId,
                                              String reason) {
        CouponPreviewVO vo = new CouponPreviewVO();
        vo.setTotalAmount(totalAmount);
        vo.setCouponId(couponId);
        vo.setDiscountAmount(BigDecimal.ZERO);
        vo.setPayableAmount(totalAmount);
        vo.setAvailable(false);
        vo.setReason(reason);
        return vo;
    }
}
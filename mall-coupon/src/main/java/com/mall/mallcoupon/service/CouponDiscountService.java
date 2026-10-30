package com.mall.mallcoupon.service;

import com.mall.common.dto.coupon.CouponPreviewDTO;
import com.mall.common.vo.coupon.CouponPreviewVO;

public interface CouponDiscountService {

    CouponPreviewVO preview(Long userId, CouponPreviewDTO dto);
}
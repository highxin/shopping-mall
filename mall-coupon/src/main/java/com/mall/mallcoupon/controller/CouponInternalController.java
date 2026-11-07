package com.mall.mallcoupon.controller;

import com.mall.common.dto.coupon.CouponLockDTO;
import com.mall.common.dto.coupon.CouponReleaseDTO;
import com.mall.common.dto.coupon.CouponUseDTO;
import com.mall.common.dto.coupon.InternalCouponPreviewDTO;
import com.mall.common.result.Result;
import com.mall.common.vo.coupon.CouponLockResultVO;
import com.mall.common.vo.coupon.CouponPreviewVO;
import com.mall.mallcoupon.service.CouponDiscountService;
import com.mall.mallcoupon.service.CouponTradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon/internal")
@RequiredArgsConstructor
public class CouponInternalController {

    private final CouponDiscountService discountService;
    private final CouponTradeService tradeService;

    @PostMapping("/preview")
    public Result<CouponPreviewVO> preview(@Valid @RequestBody InternalCouponPreviewDTO dto) {
        return Result.success(discountService.preview(dto.getUserId(), dto.getPreview()));
    }

    @PostMapping("/lock")
    public Result<CouponLockResultVO> lock(@Valid @RequestBody CouponLockDTO dto) {
        return Result.success(tradeService.lock(dto));
    }

    @PostMapping("/use")
    public Result<Void> use(@Valid @RequestBody CouponUseDTO dto) {
        tradeService.use(dto);
        return Result.success(null);
    }

    @PostMapping("/release")
    public Result<Void> release(@Valid @RequestBody CouponReleaseDTO dto) {
        tradeService.release(dto);
        return Result.success(null);
    }
}
package com.mall.mallorder.feign;

import com.mall.common.dto.coupon.CouponLockDTO;
import com.mall.common.dto.coupon.CouponReleaseDTO;
import com.mall.common.dto.coupon.CouponUseDTO;
import com.mall.common.dto.coupon.InternalCouponPreviewDTO;
import com.mall.common.result.Result;
import com.mall.common.vo.coupon.CouponLockResultVO;
import com.mall.common.vo.coupon.CouponPreviewVO;
import com.mall.mallorder.config.CouponFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "mall-coupon",
        path = "/coupon/internal",
        configuration = CouponFeignConfig.class
)
public interface CouponFeignClient {

    @PostMapping("/preview")
    Result<CouponPreviewVO> preview(@RequestBody InternalCouponPreviewDTO dto);

    @PostMapping("/lock")
    Result<CouponLockResultVO> lock(@RequestBody CouponLockDTO dto);

    @PostMapping("/use")
    Result<Void> use(@RequestBody CouponUseDTO dto);

    @PostMapping("/release")
    Result<Void> release(@RequestBody CouponReleaseDTO dto);
}
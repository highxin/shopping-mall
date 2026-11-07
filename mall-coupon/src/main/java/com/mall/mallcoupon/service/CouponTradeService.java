package com.mall.mallcoupon.service;

import com.mall.common.dto.coupon.CouponLockDTO;
import com.mall.common.dto.coupon.CouponReleaseDTO;
import com.mall.common.dto.coupon.CouponUseDTO;
import com.mall.common.vo.coupon.CouponLockResultVO;

public interface CouponTradeService {

    /**
     * 锁定优惠券
     * @param dto 锁定参数，包含优惠券ID、用户ID、锁定时间等
     * @return 锁定结果，包含是否锁定成功、锁定时间等
     */
    CouponLockResultVO lock(CouponLockDTO dto);

    /**
     * 使用优惠券
     * @param dto 使用参数，包含优惠券ID、用户ID、订单号、订单金额等
     */
    void use(CouponUseDTO dto);

    /**
     * 释放优惠券
     * @param dto 释放参数，包含优惠券ID、用户ID、订单号等
     */
    void release(CouponReleaseDTO dto);
}
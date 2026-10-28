package com.mall.mallcoupon.service;

import com.mall.mallcoupon.vo.CouponTemplateVO;
import com.mall.mallcoupon.vo.CouponVO;

import java.util.List;

public interface CouponReceiveService {
    /**
     * 领取优惠券
     * @param templateId 优惠券模板id
     * @param userId 用户id
     * @return 优惠券id
     */
    CouponVO receive(Long templateId, Long userId);

    /**
     * 查询优惠券中心
     * @param userId 用户id
     * @return 优惠券中心
     */
    List<CouponTemplateVO> center(Long userId);

    /**
     * 查询我的优惠券
     * @param userId 用户id
     * @param status 优惠券状态
     * @return 我的优惠券
     */
    List<CouponVO> myCoupons(Long userId, Integer status);
}
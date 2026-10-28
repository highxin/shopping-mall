package com.mall.mallcoupon.controller;

import com.mall.common.result.Result;
import com.mall.mallcoupon.enums.CouponErrorCode;
import com.mall.mallcoupon.exception.CouponException;
import com.mall.mallcoupon.service.CouponReceiveService;
import com.mall.mallcoupon.vo.CouponTemplateVO;
import com.mall.mallcoupon.vo.CouponVO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponReceiveService receiveService;

    @GetMapping("/center")
    public Result<List<CouponTemplateVO>> center(@RequestHeader Long userId) {
        return Result.success(receiveService.center(userId));
    }

    @PostMapping("/{templateId}/receive")
    public Result<CouponVO> receive(@PathVariable Long templateId,
                                    @RequestHeader Long userId) {
        try {
            return Result.success(receiveService.receive(templateId, userId));
        } catch (DuplicateKeyException e) {
            // 唯一键 uk_user_template 冲突：重复领取
            // 此时事务已经回滚，库存已经加回去了
            throw new CouponException(CouponErrorCode.ALREADY_RECEIVED);
        }
    }

    @GetMapping("/my")
    public Result<List<CouponVO>> my(@RequestHeader Long userId,
                                     @RequestParam(required = false) Integer status) {
        return Result.success(receiveService.myCoupons(userId, status));
    }
}
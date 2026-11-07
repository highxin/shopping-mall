package com.mall.mallcoupon.service.impl;

import com.mall.common.dto.coupon.CouponLockDTO;
import com.mall.common.dto.coupon.CouponReleaseDTO;
import com.mall.common.dto.coupon.CouponUseDTO;
import com.mall.common.vo.coupon.CouponLockResultVO;
import com.mall.mallcoupon.calculator.CouponCalculator;
import com.mall.mallcoupon.calculator.CouponCalculatorFactory;
import com.mall.mallcoupon.calculator.CouponRule;
import com.mall.mallcoupon.entity.Coupon;
import com.mall.mallcoupon.entity.CouponTemplate;
import com.mall.mallcoupon.enums.CouponErrorCode;
import com.mall.mallcoupon.enums.CouponOperationEnum;
import com.mall.mallcoupon.enums.CouponStatusEnum;
import com.mall.mallcoupon.exception.CouponException;
import com.mall.mallcoupon.mapper.CouponMapper;
import com.mall.mallcoupon.mapper.CouponOperationLogMapper;
import com.mall.mallcoupon.mapper.CouponTemplateMapper;
import com.mall.mallcoupon.service.CouponTradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponTradeServiceImpl implements CouponTradeService {

    private final CouponMapper couponMapper;
    private final CouponTemplateMapper templateMapper;
    private final CouponOperationLogMapper operationLogMapper;
    private final CouponCalculatorFactory calculatorFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CouponLockResultVO lock(CouponLockDTO dto) {
        // 1. 校验优惠券是否存在
        Coupon coupon = couponMapper.selectById(dto.getCouponId());
        if (coupon == null) {
            throw new CouponException(CouponErrorCode.COUPON_NOT_FOUND);
        }
        if (!coupon.getUserId().equals(dto.getUserId())) {
            throw new CouponException(CouponErrorCode.COUPON_NOT_OWNED);
        }

        // 幂等：同一个订单重复锁券，直接返回上次的结果
        CouponLockResultVO idempotent = idempotentLockResult(coupon, dto);
        if (idempotent != null) {
            return idempotent;
        }

        if (!CouponStatusEnum.UNUSED.getCode().equals(coupon.getStatus())) {
            throw new CouponException(CouponErrorCode.COUPON_STATUS_ERROR);
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(coupon.getEndTime())) {
            throw new CouponException(CouponErrorCode.COUPON_EXPIRED);
        }
        if (now.isBefore(coupon.getStartTime())) {
            throw new CouponException(CouponErrorCode.COUPON_NOT_STARTED);
        }

        CouponTemplate template = templateMapper.selectById(coupon.getTemplateId());
        if (template == null) {
            throw new CouponException(CouponErrorCode.TEMPLATE_NOT_FOUND);
        }

        CouponCalculator calculator = calculatorFactory.get(template.getType());
        BigDecimal discount = calculator.calculate(
                CouponRule.from(template), dto.getOrderAmount());
        if (discount.signum() <= 0) {
            throw new CouponException(CouponErrorCode.THRESHOLD_NOT_REACHED);
        }

        int rows = couponMapper.lockCoupon(coupon.getId(), dto.getUserId(), dto.getOrderNo(), discount, now);
        if (rows == 0) {
            // 并发下可能被别的订单抢走；重新查一次，判断是否同订单幂等
            Coupon latest = couponMapper.selectById(coupon.getId());
            CouponLockResultVO latestResult = latest == null ? null : idempotentLockResult(latest, dto);
            if (latestResult != null) {
                return latestResult;
            }
            throw new CouponException(CouponErrorCode.COUPON_STATUS_ERROR);
        }
        // 记录日志
        operationLogMapper.insertIgnore(coupon.getId(), dto.getOrderNo(),
                CouponOperationEnum.LOCK.getCode(), 1);
        return new CouponLockResultVO(coupon.getId(), template.getName(), discount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void use(CouponUseDTO dto) {
        Coupon coupon = couponMapper.selectById(dto.getCouponId());
        if (coupon == null) {
            throw new CouponException(CouponErrorCode.COUPON_NOT_FOUND);
        }
        if (!coupon.getUserId().equals(dto.getUserId())) {
            throw new CouponException(CouponErrorCode.COUPON_NOT_OWNED);
        }
        if (CouponStatusEnum.USED.getCode().equals(coupon.getStatus())
                && dto.getOrderNo().equals(coupon.getOrderNo())) {
            return; // 幂等
        }
        if (!CouponStatusEnum.LOCKED.getCode().equals(coupon.getStatus())) {
            throw new CouponException(CouponErrorCode.COUPON_STATUS_ERROR);
        }
        if (!dto.getOrderNo().equals(coupon.getOrderNo())) {
            throw new CouponException(CouponErrorCode.COUPON_STATUS_ERROR);
        }

        int rows = couponMapper.useCoupon(
                coupon.getId(), dto.getUserId(), dto.getOrderNo(), LocalDateTime.now());
        if (rows == 0) {
            throw new CouponException(CouponErrorCode.COUPON_STATUS_ERROR);
        }
        operationLogMapper.insertIgnore(coupon.getId(), dto.getOrderNo(),
                CouponOperationEnum.USE.getCode(), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void release(CouponReleaseDTO dto) {
        Coupon coupon = couponMapper.selectById(dto.getCouponId());
        if (coupon == null) {
            throw new CouponException(CouponErrorCode.COUPON_NOT_FOUND);
        }
        if (!coupon.getUserId().equals(dto.getUserId())) {
            throw new CouponException(CouponErrorCode.COUPON_NOT_OWNED);
        }
        if (CouponStatusEnum.UNUSED.getCode().equals(coupon.getStatus())
                && coupon.getOrderNo() == null) {
            return; // 幂等：已经释放过了
        }
        if (CouponStatusEnum.USED.getCode().equals(coupon.getStatus())) {
            throw new CouponException("已使用的优惠券不能释放，请走退款返券流程");
        }
        if (!CouponStatusEnum.LOCKED.getCode().equals(coupon.getStatus())) {
            throw new CouponException(CouponErrorCode.COUPON_STATUS_ERROR);
        }
        if (!dto.getOrderNo().equals(coupon.getOrderNo())) {
            throw new CouponException(CouponErrorCode.COUPON_STATUS_ERROR);
        }

        int rows = couponMapper.releaseCoupon(coupon.getId(), dto.getUserId(), dto.getOrderNo());
        if (rows == 0) {
            throw new CouponException(CouponErrorCode.COUPON_STATUS_ERROR);
        }
        operationLogMapper.insertIgnore(coupon.getId(), dto.getOrderNo(),
                CouponOperationEnum.RELEASE.getCode(), 1);
    }

    // 幂等：判断是否同订单锁定
    private CouponLockResultVO idempotentLockResult(Coupon coupon, CouponLockDTO dto) {
        if (!dto.getOrderNo().equals(coupon.getOrderNo())) {
            return null;
        }
        if (CouponStatusEnum.LOCKED.getCode().equals(coupon.getStatus())
                || CouponStatusEnum.USED.getCode().equals(coupon.getStatus())) {
            CouponTemplate template = templateMapper.selectById(coupon.getTemplateId());
            String name = template == null ? "" : template.getName();
            return new CouponLockResultVO(coupon.getId(), name, coupon.getDiscountAmount());
        }
        return null;
    }
}
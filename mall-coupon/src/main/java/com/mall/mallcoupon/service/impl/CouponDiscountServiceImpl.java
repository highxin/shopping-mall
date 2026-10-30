package com.mall.mallcoupon.service.impl;

import com.mall.common.dto.coupon.CouponItemDTO;
import com.mall.common.vo.coupon.CouponOptionVO;
import com.mall.common.dto.coupon.CouponPreviewDTO;
import com.mall.common.vo.coupon.CouponPreviewVO;
import com.mall.mallcoupon.calculator.CouponCandidate;
import com.mall.mallcoupon.calculator.CouponCalculator;
import com.mall.mallcoupon.calculator.CouponCalculatorFactory;
import com.mall.mallcoupon.mapper.CouponMapper;
import com.mall.mallcoupon.service.CouponDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponDiscountServiceImpl implements CouponDiscountService {

    private final CouponMapper couponMapper;
    private final CouponCalculatorFactory calculatorFactory;

    @Override
    public CouponPreviewVO preview(Long userId, CouponPreviewDTO dto) {
        // 计算订单总金额
        BigDecimal totalAmount = calculateTotal(dto.getItems());
        if (totalAmount.signum() <= 0) {
            return CouponPreviewVO.unavailable(totalAmount, dto.getCouponId(), "订单金额为0，不能使用优惠券");
        }
        // 查询用户可用的优惠券候选列表
        List<CouponCandidate> candidates =
                couponMapper.selectUsableCandidates(userId, LocalDateTime.now());

        List<CouponOptionVO> options = new ArrayList<>();
        for (CouponCandidate candidate : candidates) {
            // 工厂根据券类型 type 找到对应的计算器
            CouponCalculator calculator = calculatorFactory.get(candidate.getType());
            BigDecimal discount = calculator.calculate(candidate.toRule(), totalAmount);
            if (discount.signum() <= 0) {
                continue;
            }
            BigDecimal payable = totalAmount.subtract(discount)
                    .max(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_UP);
            options.add(new CouponOptionVO(
                    candidate.getCouponId(),
                    candidate.getName(),
                    discount,
                    payable
            ));
        }

        // 同额优惠时，优先选快到期的，帮用户先用掉
        options.sort(Comparator
                .comparing(CouponOptionVO::getDiscountAmount).reversed()
                .thenComparing(option -> findEndTime(candidates, option.getCouponId())));
        // 校验指定券是否存在
        if (dto.getCouponId() != null) {
            Optional<CouponOptionVO> selected = options.stream()
                    .filter(option -> option.getCouponId().equals(dto.getCouponId()))
                    .findFirst();
            if (selected.isEmpty()) {
                return CouponPreviewVO.unavailable(totalAmount, dto.getCouponId(), "该券当前不可用");
            }
            return CouponPreviewVO.available(totalAmount, selected.get(), options);
        }

        if (options.isEmpty()) {
            return CouponPreviewVO.unavailable(totalAmount, null, "暂无可用优惠券");
        }
        return CouponPreviewVO.available(totalAmount, options.get(0), options);
    }

    private BigDecimal calculateTotal(List<CouponItemDTO> items) {
        return items.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDateTime findEndTime(List<CouponCandidate> candidates, Long couponId) {
        return candidates.stream()
                .filter(candidate -> candidate.getCouponId().equals(couponId))
                .map(CouponCandidate::getEndTime)
                .findFirst()
                .orElse(LocalDateTime.MAX);
    }
}
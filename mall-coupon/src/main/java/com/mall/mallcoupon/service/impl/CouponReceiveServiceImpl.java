package com.mall.mallcoupon.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.mall.mallcoupon.entity.Coupon;
import com.mall.mallcoupon.entity.CouponTemplate;
import com.mall.mallcoupon.enums.CouponErrorCode;
import com.mall.mallcoupon.enums.CouponStatusEnum;
import com.mall.mallcoupon.enums.CouponTypeEnum;
import com.mall.mallcoupon.enums.IssueTypeEnum;
import com.mall.mallcoupon.enums.TemplateStatusEnum;
import com.mall.mallcoupon.enums.ValidTypeEnum;
import com.mall.mallcoupon.exception.CouponException;
import com.mall.mallcoupon.mapper.CouponMapper;
import com.mall.mallcoupon.mapper.CouponTemplateMapper;
import com.mall.mallcoupon.service.CouponReceiveService;
import com.mall.mallcoupon.vo.CouponTemplateVO;
import com.mall.mallcoupon.vo.CouponVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponReceiveServiceImpl implements CouponReceiveService {

    private final CouponTemplateMapper templateMapper;
    private final CouponMapper couponMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CouponVO receive(Long templateId, Long userId) {
        CouponTemplate template = templateMapper.selectById(templateId);
        if (template == null) {
            throw new CouponException(CouponErrorCode.TEMPLATE_NOT_FOUND);
        }

        LocalDateTime now = LocalDateTime.now();
        checkReceivable(template, now);

        // 原子扣减：状态、时间窗、库存都在 WHERE 里
        int rows = templateMapper.decreaseStock(templateId, now);
        if (rows == 0) {
            // 再查一次，给用户一个准确原因（并发下模板可能刚好被下架/领完）
            CouponTemplate latest = templateMapper.selectById(templateId);
            throw new CouponException(resolveFailure(latest, now));
        }

        Coupon coupon = buildCoupon(template, userId, now);
        couponMapper.insert(coupon);
        return toVO(coupon, template);
    }

    @Override
    public List<CouponTemplateVO> center(Long userId) {
        List<CouponTemplateVO> list = templateMapper.selectCenter(userId, LocalDateTime.now());
        // SQL 只查了原始字段，展示用的描述在 Java 里补，避免 SQL 里堆 CASE WHEN
        for (CouponTemplateVO vo : list) {
            CouponTypeEnum type = CouponTypeEnum.of(vo.getType());
            vo.setTypeDesc(type == null ? "" : type.getDesc());
            vo.setStatusDesc("已发布");
        }
        return list;
    }

    @Override
    public List<CouponVO> myCoupons(Long userId, Integer status) {
        List<CouponVO> list = couponMapper.selectMyCoupons(userId, status, LocalDateTime.now());
        for (CouponVO vo : list) {
            CouponStatusEnum statusEnum = CouponStatusEnum.of(vo.getStatus());
            vo.setStatusDesc(statusEnum == null ? "" : statusEnum.getDesc());
        }
        return list;
    }

    // 检查是否可以领取优惠券方法
    private void checkReceivable(CouponTemplate template, LocalDateTime now) {
        if (!TemplateStatusEnum.PUBLISHED.getCode().equals(template.getStatus())) {
            throw new CouponException(CouponErrorCode.TEMPLATE_NOT_PUBLISHED);
        }
        if (now.isBefore(template.getReceiveStartTime())) {
            throw new CouponException(CouponErrorCode.NOT_IN_RECEIVE_TIME);
        }
        if (now.isAfter(template.getReceiveEndTime())) {
            throw new CouponException(CouponErrorCode.NOT_IN_RECEIVE_TIME);
        }
        if (template.getRemainingStock() == null || template.getRemainingStock() <= 0) {
            throw new CouponException(CouponErrorCode.SOLD_OUT);
        }
        if (ValidTypeEnum.FIXED.getCode().equals(template.getValidType())
                && template.getValidEndTime().isBefore(now)) {
            throw new CouponException(CouponErrorCode.COUPON_EXPIRED);
        }
    }

    // 解决领券失败返回原因
    private CouponErrorCode resolveFailure(CouponTemplate latest, LocalDateTime now) {
        if (latest == null) {
            return CouponErrorCode.TEMPLATE_NOT_FOUND;
        }
        if (!TemplateStatusEnum.PUBLISHED.getCode().equals(latest.getStatus())) {
            return CouponErrorCode.TEMPLATE_NOT_PUBLISHED;
        }
        if (now.isBefore(latest.getReceiveStartTime()) || now.isAfter(latest.getReceiveEndTime())) {
            return CouponErrorCode.NOT_IN_RECEIVE_TIME;
        }
        return CouponErrorCode.SOLD_OUT;
    }

    private Coupon buildCoupon(CouponTemplate template, Long userId, LocalDateTime now) {
        Coupon coupon = new Coupon();
        coupon.setTemplateId(template.getId());
        coupon.setUserId(userId);
        coupon.setCouponNo("C" + IdWorker.getIdStr());
        coupon.setStatus(CouponStatusEnum.UNUSED.getCode());
        coupon.setReceiveTime(now);
        coupon.setSource(IssueTypeEnum.RECEIVE.getCode());
        coupon.setVersion(0);

        if (ValidTypeEnum.FIXED.getCode().equals(template.getValidType())) {
            coupon.setStartTime(template.getValidStartTime());
            coupon.setEndTime(template.getValidEndTime());
        } else {
            coupon.setStartTime(now);
            coupon.setEndTime(now.plusDays(template.getValidDays()));
        }
        return coupon;
    }

    private CouponVO toVO(Coupon coupon, CouponTemplate template) {
        CouponVO vo = new CouponVO();
        BeanUtils.copyProperties(coupon, vo);
        vo.setName(template.getName());
        vo.setType(template.getType());
        vo.setThresholdAmount(template.getThresholdAmount());
        vo.setDiscountAmount(template.getDiscountAmount());
        vo.setStatusDesc(CouponStatusEnum.of(coupon.getStatus()) == null
                ? "" : CouponStatusEnum.of(coupon.getStatus()).getDesc());
        return vo;
    }
}
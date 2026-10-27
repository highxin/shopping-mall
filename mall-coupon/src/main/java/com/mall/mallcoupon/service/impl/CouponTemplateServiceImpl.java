package com.mall.mallcoupon.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mallcoupon.exception.CouponException;
import com.mall.mallcoupon.dto.CouponTemplateCreateDTO;
import com.mall.mallcoupon.dto.CouponTemplateUpdateDTO;
import com.mall.mallcoupon.entity.CouponTemplate;
import com.mall.mallcoupon.enums.CouponErrorCode;
import com.mall.mallcoupon.enums.CouponTypeEnum;
import com.mall.mallcoupon.enums.TemplateStatusEnum;
import com.mall.mallcoupon.mapper.CouponTemplateMapper;
import com.mall.mallcoupon.service.CouponTemplateService;
import com.mall.mallcoupon.vo.CouponTemplateVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * <p>描述: </p>
 * <p>版权: &copy;highxin</p>
 *
 * @author highxin
 * @version 1.0.0
 * @since 2026/10/26
 */
@Service
public class CouponTemplateServiceImpl extends ServiceImpl<CouponTemplateMapper, CouponTemplate> implements CouponTemplateService {

    @Override
    public Long create(CouponTemplateCreateDTO dto) {
        validate(dto);
        CouponTemplate template = new CouponTemplate();
        BeanUtils.copyProperties(dto, template);
        template.setDiscountRate(dto.getDiscountAmount().divide(dto.getThresholdAmount(),4, RoundingMode.HALF_UP));
        template.setRemainingStock(dto.getTotalStock());
        template.setStatus(TemplateStatusEnum.DRAFT.getCode());
        save(template);
        return template.getId();
    }

    @Override
    public void update(Long id, CouponTemplateUpdateDTO dto) {
        CouponTemplate old = getById(id);
        if (old == null) {
            throw new CouponException(CouponErrorCode.TEMPLATE_NOT_FOUND);
        }
        if (!TemplateStatusEnum.DRAFT.getCode().equals(old.getStatus())) {
            throw new CouponException("只有草稿状态的券才能修改");
        }
        validate(dto);
        CouponTemplate update = new CouponTemplate();
        BeanUtils.copyProperties(dto, update);
        update.setId(id);
        // 草稿阶段允许调整总量，剩余量跟着重置
        update.setRemainingStock(dto.getTotalStock());
        updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long id) {
        CouponTemplate template = getById(id);
        if (template == null) {
            throw new CouponException(CouponErrorCode.TEMPLATE_NOT_FOUND);
        }
        if (TemplateStatusEnum.PUBLISHED.getCode().equals(template.getStatus())) {
            return; // 幂等
        }
        if (!TemplateStatusEnum.DRAFT.getCode().equals(template.getStatus())
                && !TemplateStatusEnum.OFFLINE.getCode().equals(template.getStatus())) {
            throw new CouponException("当前状态不能发布");
        }
        if (template.getRemainingStock() == null || template.getRemainingStock() <= 0) {
            throw new CouponException("库存为0，不能发布");
        }
        if (!template.getReceiveEndTime().isAfter(LocalDateTime.now())) {
            throw new CouponException("领取结束时间已过，不能发布");
        }

        boolean updated = lambdaUpdate()
                .eq(CouponTemplate::getId, id)
                .eq(CouponTemplate::getStatus, template.getStatus())
                .set(CouponTemplate::getStatus, TemplateStatusEnum.PUBLISHED.getCode())
                .update();
        if (!updated) {
            throw new CouponException("发布失败，请重试");
        }
    }

    @Override
    public void offline(Long id) {
        CouponTemplate template = getById(id);
        if (template == null) {
            throw new CouponException(CouponErrorCode.TEMPLATE_NOT_FOUND);
        }
        if (TemplateStatusEnum.OFFLINE.getCode().equals(template.getStatus())) {
            return; // 幂等
        }
        if (!TemplateStatusEnum.PUBLISHED.getCode().equals(template.getStatus())) {
            throw new CouponException("只有已发布的券才能下架");
        }
        boolean updated = lambdaUpdate()
                .eq(CouponTemplate::getId, id)
                .eq(CouponTemplate::getStatus, TemplateStatusEnum.PUBLISHED.getCode())
                .set(CouponTemplate::getStatus, TemplateStatusEnum.OFFLINE.getCode())
                .update();
        if (!updated) {
            throw new CouponException("下架失败，请重试");
        }
    }

    @Override
    public IPage<CouponTemplateVO> page(long pageNum, long pageSize, Integer status, String name) {
        IPage<CouponTemplate> page = new Page<>(pageNum, pageSize);
        page = lambdaQuery()
                .eq(status != null, CouponTemplate::getStatus, status)
                .like(name != null, CouponTemplate::getName, name)
                .orderByDesc(CouponTemplate::getId)
                .page(page);
        return page.convert(this::toVO);
    }

    @Override
    public CouponTemplateVO detail(Long id) {
        CouponTemplate template = getById(id);
        if (template == null) {
            throw new CouponException(CouponErrorCode.TEMPLATE_NOT_FOUND);
        }
        return toVO(template);
    }

    /**
     * 跨字段校验放在 Service，不放在 DTO 注解里。
     * 因为“满减券必须有门槛和减免额”这种规则涉及多个字段，
     * 用 @AssertTrue 会让 DTO 变成半个领域对象，不利于复用。
     */
    private void validate(CouponTemplateCreateDTO dto) {
        if (dto.getScopeType() == null || dto.getScopeType() != 1) {
            throw new CouponException("学习版先支持全场券，品类/SKU 券见 4.8");
        }
        if (!dto.getReceiveStartTime().isBefore(dto.getReceiveEndTime())) {
            throw new CouponException("领取开始时间必须早于结束时间");
        }
        if (dto.getPerUserLimit() == null || dto.getPerUserLimit() != 1) {
            throw new CouponException("学习版暂支持每人限领1张");
        }
        if (dto.getTotalStock() == null || dto.getTotalStock() <= 0) {
            throw new CouponException("发行量必须大于0");
        }

        CouponTypeEnum type = CouponTypeEnum.of(dto.getType());
        if (type == null) {
            throw new CouponException("券类型不合法");
        }

        switch (type) {
            case FULL_REDUCTION -> {
                require(dto.getThresholdAmount().compareTo(BigDecimal.ZERO) > 0, "满减券门槛必须大于0");
                require(dto.getDiscountAmount() != null
                        && dto.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0, "满减金额必须大于0");
                require(dto.getDiscountAmount().compareTo(dto.getThresholdAmount()) <= 0,
                        "满减金额不能大于门槛金额");
            }
            case DISCOUNT -> {
                require(dto.getDiscountRate() != null
                                && dto.getDiscountRate().compareTo(BigDecimal.ZERO) > 0
                                && dto.getDiscountRate().compareTo(BigDecimal.ONE) < 0,
                        "折扣率必须在0到1之间（不含1）");
                require(dto.getMaxDiscountAmount() != null
                                && dto.getMaxDiscountAmount().compareTo(BigDecimal.ZERO) > 0,
                        "折扣券必须设置最高优惠金额");
            }
            case NO_THRESHOLD -> {
                require(dto.getThresholdAmount().compareTo(BigDecimal.ZERO) == 0,
                        "无门槛券的门槛必须为0");
                require(dto.getDiscountAmount() != null
                        && dto.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0, "无门槛券金额必须大于0");
            }
        }

        if (dto.getValidType() == 1) {
            require(dto.getValidStartTime() != null && dto.getValidEndTime() != null,
                    "固定有效期必须填写开始和结束时间");
            require(dto.getValidStartTime().isBefore(dto.getValidEndTime()),
                    "有效期开始必须早于结束");
            require(dto.getValidEndTime().isAfter(dto.getReceiveStartTime()),
                    "有效期结束必须晚于领取开始，否则领到的券一出生就过期");
        } else if (dto.getValidType() == 2) {
            require(dto.getValidDays() != null && dto.getValidDays() > 0,
                    "领取后有效期天数必须大于0");
        } else {
            throw new CouponException("有效期类型不合法");
        }
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new CouponException(message);
        }
    }


    private CouponTemplateVO toVO(CouponTemplate template) {
        CouponTemplateVO vo = new CouponTemplateVO();
        BeanUtils.copyProperties(template, vo);
        CouponTypeEnum type = CouponTypeEnum.of(template.getType());
        vo.setTypeDesc(type == null ? "" : type.getDesc());
        vo.setStatusDesc(TemplateStatusEnum.PUBLISHED.getCode().equals(template.getStatus())
                ? "已发布"
                : TemplateStatusEnum.OFFLINE.getCode().equals(template.getStatus()) ? "已下架" : "草稿");
        return vo;
    }

}

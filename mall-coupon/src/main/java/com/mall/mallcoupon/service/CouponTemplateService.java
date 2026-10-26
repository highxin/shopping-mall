package com.mall.mallcoupon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.mallcoupon.dto.CouponTemplateCreateDTO;
import com.mall.mallcoupon.dto.CouponTemplateUpdateDTO;
import com.mall.mallcoupon.entity.CouponTemplate;
import com.mall.mallcoupon.vo.CouponTemplateVO;

public interface CouponTemplateService extends IService<CouponTemplate> {
    /** 创建优惠券模板 */
    Long create(CouponTemplateCreateDTO dto);

    /** 更新优惠券模板 */
    void update(Long id, CouponTemplateUpdateDTO dto);

    /** 发布优惠券模板 */
    void publish(Long id);

    /** 下线优惠券模板 */
    void offline(Long id);

    /** 分页查询优惠券模板 */
    IPage<CouponTemplateVO> page(long pageNum, long pageSize, Integer status, String name);

    /** 查询优惠券模板详情 */
    CouponTemplateVO detail(Long id);
}
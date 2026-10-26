package com.mall.mallcoupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.mallcoupon.entity.CouponTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface CouponTemplateMapper extends BaseMapper<CouponTemplate> {

    /**
     * 原子扣减库存。
     * 把“已发布 + 在领取窗口 + 有库存”三个条件放进同一条 UPDATE，
     * 避免先查后改的竞态。
     */
    @Update("""
            UPDATE coupon_template
               SET remaining_stock = remaining_stock - 1,
                   version = version + 1,
                   update_time = NOW(3)
             WHERE id = #{templateId}
               AND status = 1
               AND deleted = 0
               AND remaining_stock > 0
               AND #{now} BETWEEN receive_start_time AND receive_end_time
            """)
    int decreaseStock(@Param("templateId") Long templateId,
                      @Param("now") LocalDateTime now);

    @Update("""
            UPDATE coupon_template
               SET remaining_stock = remaining_stock + 1,
                   version = version + 1,
                   update_time = NOW(3)
             WHERE id = #{templateId}
               AND remaining_stock < total_stock
            """)
    int increaseStock(@Param("templateId") Long templateId);
}
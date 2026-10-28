package com.mall.mallcoupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.mallcoupon.entity.CouponTemplate;
import com.mall.mallcoupon.vo.CouponTemplateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

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

    @Select("""
        SELECT t.id,
               t.name,
               t.type,
               t.scope_type,
               t.threshold_amount,
               t.discount_amount,
               t.discount_rate,
               t.max_discount_amount,
               t.total_stock,
               t.remaining_stock,
               t.per_user_limit,
               t.issue_type,
               t.valid_type,
               t.valid_start_time,
               t.valid_end_time,
               t.valid_days,
               t.receive_start_time,
               t.receive_end_time,
               t.status,
               t.create_time,
               (c.id IS NOT NULL) AS received
          FROM coupon_template t
          LEFT JOIN coupon c
            ON c.template_id = t.id
           AND c.user_id = #{userId}
         WHERE t.status = 1
           AND t.deleted = 0
           AND #{now} BETWEEN t.receive_start_time AND t.receive_end_time
           AND t.remaining_stock > 0
         ORDER BY t.id DESC
        """)
    List<CouponTemplateVO> selectCenter(@Param("userId") Long userId,
                                        @Param("now") LocalDateTime now);
}
package com.mall.mallcoupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.mallcoupon.calculator.CouponCandidate;
import com.mall.mallcoupon.entity.Coupon;
import com.mall.mallcoupon.vo.CouponVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

    @Select("""
    SELECT c.id,
           c.template_id,
           t.name,
           t.type,
           t.threshold_amount,
           t.discount_amount,
           c.status,
           c.discount_amount AS actual_discount_amount,
           c.receive_time,
           c.start_time,
           c.end_time,
           c.order_no
      FROM coupon c
      LEFT JOIN coupon_template t ON c.template_id = t.id
     WHERE c.user_id = #{userId}
       AND c.status = #{status}
     ORDER BY c.id DESC
    """)
    List<CouponVO> selectMyCoupons(@Param("userId") Long userId,
                                   @Param("status") Integer status,
                                   @Param("now") LocalDateTime now);

//    /**
//     * 锁券：只有 UNUSED 且未过期、属于当前用户的券才能锁。
//     * 返回影响行数，0 表示竞争失败/状态不对。
//     */
//    int lockCoupon(@Param("couponId") Long couponId,
//                   @Param("userId") Long userId,
//                   @Param("orderNo") String orderNo,
//                   @Param("discountAmount") BigDecimal discountAmount,
//                   @Param("now") LocalDateTime now);
//
//    int useCoupon(@Param("couponId") Long couponId,
//                  @Param("userId") Long userId,
//                  @Param("orderNo") String orderNo,
//                  @Param("now") LocalDateTime now);
//
//    int releaseCoupon(@Param("couponId") Long couponId,
//                      @Param("userId") Long userId,
//                      @Param("orderNo") String orderNo);

//    /**
//     * 批量过期，返回实际更新行数；用 LIMIT 避免一次锁太多行。
//     */
//    int expireBatch(@Param("limit") int limit);
    /**
     * 订单服务试算/锁券时查询用户的可用券（联表带模板信息）。
     */
    List<CouponCandidate> selectUsableCandidates(@Param("userId") Long userId,
                                                 @Param("now") LocalDateTime now);
}
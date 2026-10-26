package com.mall.mallcoupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.mallcoupon.entity.CouponOperationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CouponOperationLogMapper extends BaseMapper<CouponOperationLog> {

    @Insert("""
            INSERT IGNORE INTO coupon_operation_log
                (coupon_id, order_no, operation, operator_type)
            VALUES
                (#{couponId}, #{orderNo}, #{operation}, #{operatorType})
            """)
    int insertIgnore(@Param("couponId") Long couponId,
                     @Param("orderNo") String orderNo,
                     @Param("operation") Integer operation,
                     @Param("operatorType") Integer operatorType);
}
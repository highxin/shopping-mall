package com.mall.mallorder.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class CouponFeignConfig {

    @Bean
    public RequestInterceptor couponInternalTokenInterceptor(
            @Value("${mall.coupon.internal-token}") String token) {
        return template -> template.header("X-Internal-Token", token);
    }
}
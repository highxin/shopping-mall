package com.mall.mallgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
public class CorsConfig {


    @Bean
    public CorsWebFilter corsFilter(){

        CorsConfiguration config = new CorsConfiguration();

        //允许哪些来源访问
        config.addAllowedOriginPattern("*");

        //允许请求头
        config.addAllowedHeader("*");

        //允许请求方式
        config.addAllowedMethod("*");

        //允许携带Cookie
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

}
package com.mall.malluser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 放行所有请求（认证由 JWT 过滤器自行处理）
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                // 禁用 CSRF（前后端分离项目不需要）
                .csrf(csrf -> csrf.disable())
                // 无状态会话（不使用 Session）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
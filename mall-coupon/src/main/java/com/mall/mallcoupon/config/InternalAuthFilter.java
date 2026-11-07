package com.mall.mallcoupon.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalAuthFilter extends OncePerRequestFilter {

    @Value("${mall.coupon.internal-token}")
    private String internalToken;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/coupon/internal/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("X-Internal-Token");
        if (!internalToken.equals(token)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"code\":60016,\"msg\":\"内部调用鉴权失败\",\"data\":null}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
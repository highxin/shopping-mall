package com.mall.mallgateway.filter;

import com.mall.common.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    // 白名单
    private static final List<String> WHITE_LIST = List.of(
            "/user/login",
            "/user/register"
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest()
                        .getHeaders()
                        .getFirst("Authorization");
        // 白名单
        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();


        //1. 白名单直接放行
        if(isWhiteList(path)){
            return chain.filter(exchange);
        }

        //没有token
        if(token == null){
            return unauthorized(exchange,"请先登录");
        }
        //去掉Bearer
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try {
            Long userId = JwtUtil.getUserId(token);
            System.out.println("当前用户id:"+userId);
            // userId放入请求头
            ServerWebExchange newExchange =
                    exchange.mutate()
                            .request(
                                    exchange.getRequest()
                                            .mutate()
                                            .header(
                                                    "userId",
                                                    userId.toString()
                                            )
                                            .build()
                            )
                            .build();
            return chain.filter(newExchange);
        }catch(Exception e){
            return unauthorized(exchange,"token无效");
        }
    }


    @Override
    public int getOrder() {
        return -100;
    }

    // 白名单判断
    private boolean isWhiteList(String path){
        return WHITE_LIST.stream()
                .anyMatch(path::startsWith);
    }

    // 401响应
    private Mono<Void> unauthorized(ServerWebExchange exchange,String message){

        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);

        exchange.getResponse()
                .getHeaders()
                .add(
                        "Content-Type",
                        "application/json;charset=UTF-8"
                );

        String body =
                "{\"code\":401,\"msg\":\""+message+"\",\"data\":null}";

        DataBuffer buffer =
                exchange.getResponse()
                        .bufferFactory()
                        .wrap(body.getBytes());

        return exchange.getResponse()
                .writeWith(
                        Mono.just(buffer)
                );
    }

}
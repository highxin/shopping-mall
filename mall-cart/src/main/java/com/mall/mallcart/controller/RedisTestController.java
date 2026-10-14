package com.mall.mallcart.controller;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class RedisTestController {


    private final RedisTemplate<String,Object> redisTemplate;


    public RedisTestController(
            RedisTemplate<String,Object> redisTemplate
    ){
        this.redisTemplate = redisTemplate;
    }



    @GetMapping("/redis")
    public String test(){


        redisTemplate.opsForValue()
                .set("hello","redis");


        Object value =
                redisTemplate.opsForValue()
                        .get("hello");


        return value.toString();

    }

}
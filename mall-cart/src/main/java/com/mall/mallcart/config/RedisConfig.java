package com.mall.mallcart.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String,Object> redisTemplate(
            RedisConnectionFactory factory
    ){

        RedisTemplate<String,Object> template = new RedisTemplate<>();


        // 设置连接工厂
        template.setConnectionFactory(factory);


        // key使用String序列化
        template.setKeySerializer(
                new StringRedisSerializer()
        );


        // hash的key使用String
        template.setHashKeySerializer(
                new StringRedisSerializer()
        );


        // value使用JSON序列化
        template.setValueSerializer(
                new GenericJackson2JsonRedisSerializer()
        );


        // hash value使用JSON
        template.setHashValueSerializer(
                new GenericJackson2JsonRedisSerializer()
        );


        template.afterPropertiesSet();


        return template;
    }

}
package com.mall.mallproduct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.mall"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({
        "com.mall.mallproduct.category.mapper",
        "com.mall.mallproduct.product.mapper",
        "com.mall.mallproduct.spu.mapper",
        "com.mall.mallproduct.sku.mapper",
        "com.mall.mallproduct.spec.mapper",
        "com.mall.mallproduct.specvalue.mapper",
        "com.mall.mallproduct.skuspec.mapper"
})
public class MallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }

}
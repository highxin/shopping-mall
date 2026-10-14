package com.mall.mallcart.feign;


import com.mall.common.result.Result;
import com.mall.mallcart.vo.SkuDetailVO;
import org.apache.ibatis.annotations.Delete;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("mall-product")
public interface ProductFeignClient {


    /**
     * 根据SKU ID查询SKU详情
     */
    @GetMapping("/product/sku/detail/{id}")
    Result<SkuDetailVO> getSkuDetail(
            @PathVariable("id") Long id
    );


}
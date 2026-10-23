package com.mall.search.feign;

import com.mall.common.dto.ProductIndexDTO;
import com.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("mall-product")
public interface ProductFeignClient {

    /**
     * 查询商品索引所需的全部数据
     */
    @GetMapping("/product/spu/search")
    Result<List<ProductIndexDTO>> listForSearch();
}
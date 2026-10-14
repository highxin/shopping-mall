package com.mall.mallorder.feign;

import com.mall.common.dto.SkuStockDTO;
import com.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mall-product")
public interface ProductFeignClient {

    @PostMapping("/product/sku/deduct")
    Result<String> deductStock(@RequestBody List<SkuStockDTO> list);

    @PostMapping("/product/sku/addStock")
    Result<String> addStock(@RequestBody List<SkuStockDTO> list);

}